package org.alien.sample.aop;

import org.alien.framework.aop.annotation.Aspect;
import org.alien.framework.aop.proxy.AspectProxy;
import org.alien.framework.mvc.annotation.Controller;


@Aspect(Controller.class)
public class ControllerBeforeAspect extends AspectProxy{

    @Override
    public void begin() {
        System.out.println("before==========aop============");
    }
}
