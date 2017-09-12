package org.alien.framework.mvc;

import org.alien.framework.core.ClassHelper;
import org.alien.framework.mvc.annotation.Action;
import org.alien.framework.mvc.annotation.Controller;
import org.alien.framework.mvc.bean.Handler;
import org.alien.framework.mvc.bean.Requester;
import org.alien.framework.utils.ArrayUtil;
import org.alien.framework.utils.CollectionUtil;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 控制器帮助类
 */
public final class ControllerHelper {

    /**
     * Action Map（HTTP 请求与 Action 方法的映射）
     */
    private static final Map<Requester, Handler> ACTION_MAP = new LinkedHashMap<>();

    /**
     * 初始化Action
     */
    static {
        //获取所有Controller类
        List<Class<?>> classList = ClassHelper.getClassByAnnotation(Controller.class);
        if (CollectionUtil.isNotEmpty(classList)) {
            for (Class<?> controllerClass : classList) {
                //获取类中的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(Action.class)) {
                        //从Action中获取URL映射规则
                        Action action = method.getAnnotation(Action.class);
                        String mapping = action.value();

                        //验证URL映射规则
                        if (mapping.matches("\\w+:/\\w*")) {
                            String[] array = mapping.split(":");
                            if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                //请求方法与请求路径
                                String requestMethod = array[0];
                                String requestPath = array[1];
                                Requester requester = new Requester(requestMethod, requestPath);
                                Handler handler = new Handler(controllerClass, method);
                                ACTION_MAP.put(requester,handler);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取Handler
     * @param requestMethod
     * @param requestPath
     * @return
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        Requester requester = new Requester(requestMethod, requestPath);
        return ACTION_MAP.get(requester);
    }

}
