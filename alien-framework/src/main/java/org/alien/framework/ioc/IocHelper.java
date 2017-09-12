package org.alien.framework.ioc;


import org.alien.framework.ioc.annotation.Inject;
import org.alien.framework.utils.ArrayUtil;
import org.alien.framework.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入帮助类
 */
public final class IocHelper {

    /**
     * 初始化字段的注入
     */
    static {
        //获取所有Bean类与Bean实例之间的映射关系
        Map<Class<?>, Object> benaMap = BeanHelper.getBeanMap();
        for (Map.Entry<Class<?>,Object> beanEntry : benaMap.entrySet()) {
            //获取bean类与bean实例
            Class<?> beanClass = beanEntry.getKey();
            Object beanInstance = beanEntry.getValue();
            //获取bean定义的所有成员变量
            Field[] beanFields = beanClass.getDeclaredFields();
            if (ArrayUtil.isNotEmpty(beanFields)){
                //遍历Bean Field
                for (Field beanField:beanFields) {
                    //判断当前bean Field是否带有Inject 注解
                    if (beanField.isAnnotationPresent(Inject.class)){
                        //再Bean Map中获取Bean Field的对应实例
                        Class<?> beanFieldType = beanField.getType();
                        Object beanFieldInstance = benaMap.get(beanFieldType);
                        if (beanFieldInstance!=null){
                            //通过反射初始化BeanField的值
                            ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                        }else{
                            throw new RuntimeException("依赖注入失败，类名："+beanClass.getName()+",字段名："+beanFieldType.getName());
                        }
                    }
                }
            }
        }
    }

}
