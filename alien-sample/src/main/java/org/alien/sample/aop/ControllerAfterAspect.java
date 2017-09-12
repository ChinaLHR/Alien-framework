package org.alien.sample.aop;

import org.alien.framework.aop.annotation.Aspect;
import org.alien.framework.aop.proxy.AspectProxy;
import org.alien.framework.mvc.annotation.Controller;

import java.lang.reflect.Method;

@Aspect(Controller.class)
public class ControllerAfterAspect extends AspectProxy {

    @Override
    public void after(Class<?> cls, Method method, Object[] params, Object result) throws Throwable {
        System.out.println("alter==========aop============");
    }
}
