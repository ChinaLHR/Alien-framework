package org.alien.framework.aop.proxy;

/**
 * 代理接口
 */
public interface Proxy {

    /**
     * 执行链式代理
     * @return
     */
   Object doProxy(ProxyChain proxyChain) throws Throwable;

}
