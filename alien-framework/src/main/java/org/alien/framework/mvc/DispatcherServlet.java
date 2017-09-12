package org.alien.framework.mvc;

import org.alien.framework.core.ConfigHelper;
import org.alien.framework.core.HelperLoader;
import org.alien.framework.ioc.BeanHelper;
import org.alien.framework.mvc.bean.Data;
import org.alien.framework.mvc.bean.Handler;
import org.alien.framework.mvc.bean.Param;
import org.alien.framework.mvc.bean.View;
import org.alien.framework.utils.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //初始化Helper
        HelperLoader.init();
        //获取ServletContext对象，用于注册Servlet
        ServletContext context = servletConfig.getServletContext();
        //动态注册处理JSP的Servlet
        ServletRegistration jspServlet = context.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");
        //动态注册处理静态资源的servlet
        ServletRegistration defaultServlet = context.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //获取请求方法与请求路径
        String requestMethod = req.getMethod().toLowerCase();
        String requestPath = req.getPathInfo();

        ServletHelper.init(req, resp);

        try {
            //获取Action处理器
            Handler handler = ControllerHelper.getHandler(requestMethod, requestPath);
            if (handler != null) {
                //获取Controller实例
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                //创建请求参数对象
                Map<String, Object> paramMap = new HashMap<>();
                Enumeration<String> paramNames = req.getParameterNames();
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    String paramValue = req.getParameter(paramName);
                    paramMap.put(paramName, paramValue);
                }

                String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
                if (StringUtil.isNotEmpty(body)) {
                    String[] params = StringUtil.splitString(body, "&");
                    if (ArrayUtil.isNotEmpty(params)) {
                        for (String param : params) {
                            String[] array = StringUtil.splitString(param, "=");
                            if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                String paramName = array[0];
                                String paramValue = array[1];
                                paramMap.put(paramName, paramValue);
                            }
                        }
                    }
                }

                Param param = new Param(paramMap);
                //调用Action方法
                Method actionMethod = handler.getActionMethod();
                Object result;
                if (paramMap.size() > 0) {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod, param);
                } else {
                    result = ReflectionUtil.invokeMethod(controllerBean, actionMethod);
                }
                //处理Action返回值
                if (result instanceof View) {
                    //返回jsp页面
                    View view = (View) result;
                    String path = view.getPath();
                    if (StringUtil.isNotEmpty(path)) {
                        if (path.startsWith("/")) {
                            //重定向
                            resp.sendRedirect(req.getContextPath() + path);
                        } else {
                            Map<String, Object> model = view.getModel();
                            for (Map.Entry<String, Object> entry : model.entrySet()) {
                                req.setAttribute(entry.getKey(), entry.getValue());
                            }
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req, resp);
                    }
                } else if (result instanceof Data) {
                    //返回json数据
                    Data data = (Data) result;
                    Object model = data.getModel();
                    if (model != null) {
                        resp.setContentType("application/json");
                        resp.setCharacterEncoding("UTF-8");
                        PrintWriter writer = resp.getWriter();
                        String json = JsonUtil.toJSON(model);
                        writer.write(json);
                        writer.flush();
                        writer.close();
                    }
                }

            }
        } finally {
            ServletHelper.destroy();
        }
    }
}
