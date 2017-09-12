package org.alien.framework.aop;

import org.alien.framework.aop.annotation.Aspect;
import org.alien.framework.aop.proxy.AspectProxy;
import org.alien.framework.aop.proxy.Proxy;
import org.alien.framework.aop.proxy.ProxyManager;
import org.alien.framework.core.ClassHelper;
import org.alien.framework.ioc.BeanHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AOP帮助类
 */
public final class AopHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    /**
     * 初始化
     */
    static {
        try {
            // 创建 Proxy Map（用于 存放代理类 与 目标类列表 的映射关系）
            Map<Class<?>, List<Class<?>>> proxyMap = createProxyMap();
            // 创建 Target Map（用于 存放目标类 与 代理类列表 的映射关系）
            Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
            for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
                // 分别获取 map 中的 key 与 value
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                // 创建代理实例
                Object proxy = ProxyManager.createProxy(targetClass, proxyList);
                // 用代理实例覆盖目标实例，并放入 Bean 容器中
                BeanHelper.setBean(targetClass, proxy);
            }
        } catch (Exception e) {
            LOGGER.error("aop failure", e);
        }
    }

    /**
     * 代理类与目标的映射关系
     *
     * @return
     * @throws Exception
     */
    public static Map<Class<?>, List<Class<?>>> createProxyMap() throws Exception {
        HashMap<Class<?>, List<Class<?>>> proxyMap = new HashMap<>();
        List<Class<?>> proxyClasslist = ClassHelper.getClassListBySuper(AspectProxy.class);
        for (Class<?> proxyClass : proxyClasslist) {
            if (proxyClass.isAnnotationPresent(Aspect.class)) {
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                List<Class<?>> targetClassList = createTargetClassList(aspect);
                proxyMap.put(proxyClass, targetClassList);
            }
        }
        return proxyMap;
    }

    /**
     * 获取需要AOP的类
     *
     * @param aspect
     * @return
     */
    private static List<Class<?>> createTargetClassList(Aspect aspect) {
        ArrayList<Class<?>> targetClassList = new ArrayList<>();
        Class<? extends Annotation> annotation = aspect.value();
        if (annotation != null && !annotation.equals(Aspect.class)) {
            targetClassList.addAll(ClassHelper.getClassByAnnotation(annotation));
        }
        return targetClassList;
    }

    /**
     * 目标类与代理对象之间的关系
     *
     * @param proxyMap
     * @return
     * @throws Exception
     */
    private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, List<Class<?>>> proxyMap) throws Exception {
        HashMap<Class<?>, List<Proxy>> targetMap = new HashMap<>();
        // 遍历 Proxy Map
        for (Map.Entry<Class<?>, List<Class<?>>> proxyEntry : proxyMap.entrySet()) {
            // 分别获取 map 中的 key 与 value
            Class<?> proxyClass = proxyEntry.getKey();
            List<Class<?>> targetClassList = proxyEntry.getValue();
            // 遍历目标类列表
            for (Class<?> targetClass : targetClassList) {
                // 创建代理类（切面类）实例
                Proxy proxy = (Proxy) proxyClass.newInstance();
                // 初始化 Target Map
                if (targetMap.containsKey(targetClass)) {
                    targetMap.get(targetClass).add(proxy);
                } else {
                    List<Proxy> proxyList = new ArrayList<>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass, proxyList);
                }
            }
        }
        return targetMap;
    }

}
