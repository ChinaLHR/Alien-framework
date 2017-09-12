package org.alien.framework.aop.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 代理管理器：提供创建代理对象的方法
 */
public class ProxyManager {

    /**
     * 创建代理对象
     * @param targetClass
     * @param proxyList
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){

        return (T) Enhancer.create(targetClass, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {

                return new ProxyChain(targetClass,targetObject,targetMethod,methodProxy,methodParams,proxyList).doProxyChain();
            }
        });
    }

}
