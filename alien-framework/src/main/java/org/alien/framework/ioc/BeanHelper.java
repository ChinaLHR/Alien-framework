package org.alien.framework.ioc;

import org.alien.framework.aop.annotation.Aspect;
import org.alien.framework.ioc.annotation.Service;
import org.alien.framework.core.ClassHelper;
import org.alien.framework.mvc.annotation.Controller;
import org.alien.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bean帮助类
 */
public final class BeanHelper {

    /**
     * Bean Map（Bean 类  Bean 实例）
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<>();

    /**
     * 初始化Bean
     */
    static {
        List<Class<?>> classList = ClassHelper.getClassList();
        for (Class<?> cls : classList) {
            //处理相关注解类
            if (cls.isAnnotationPresent(Service.class) || cls.isAnnotationPresent(Controller.class) || cls.isAnnotationPresent(Aspect.class)) {
                // 创建 Bean 实例
                Object o = ReflectionUtil.newInstance(cls);
                // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                BEAN_MAP.put(cls, o);
            }
        }
    }


    /**
     * 获取Bean映射
     * @return
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取bean实例
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> cls){
        if (!BEAN_MAP.containsKey(cls)){
            throw new RuntimeException("can not get bean by class :"+cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    /**
     * 添加bean实例
     * @param clazz
     * @param object
     */
    public static void setBean(Class<?> clazz,Object object){
        BEAN_MAP.put(clazz,object);
    }
}
