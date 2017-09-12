package org.alien.framework.core;

import org.alien.framework.aop.AopHelper;
import org.alien.framework.ioc.BeanHelper;
import org.alien.framework.ioc.IocHelper;
import org.alien.framework.mvc.ControllerHelper;
import org.alien.framework.utils.ClassUtil;

/**
 * 所有Helper初始化类
 */
public class HelperLoader {

    /**
     * 初始化所有Helper方法
     * 注意：AOP需要在IOC之前初始化，这样才能注入代理类
     */
    public static void init(){
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                AopHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };

        for (Class<?> cls:classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }

}
