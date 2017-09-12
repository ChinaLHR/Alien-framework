package org.alien.framework.core;

import org.alien.framework.utils.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 类操作帮助类
 */
public final class ClassHelper {

    private static final List<Class<?>> CLASS_LIST;

    /**
     * 初始化扫描集合
     */
    static {
        String appBasePackage = ConfigHelper.getAppBasePackage();
        CLASS_LIST = ClassUtil.getClassList(appBasePackage);
    }

    /**
     * 获得应用包下所有类
     *
     * @return
     */
    public static List<Class<?>> getClassList() {
        return CLASS_LIST;
    }

    /**
     * 获取应用包下某个父类（或接口）的所有子类（或实现类）
     * @param superClass
     * @return
     */
    public static List<Class<?>> getClassListBySuper(Class<?> superClass){
       List<Class<?>> classList =  new ArrayList<>();
        for (Class<?> cls:CLASS_LIST) {
            if (superClass.isAssignableFrom(cls)&&!superClass.equals(cls)){
                classList.add(cls);
            }
        }
        return classList;
    }

    /**
     * 获取基础包中指定注解的相关类
     * @param aClass
     * @return
     */
    public static List<Class<?>> getClassByAnnotation(Class<? extends Annotation> aClass) {
        ArrayList<Class<?>> list = new ArrayList<>();
        for (Class<?> cls : CLASS_LIST) {
            if (cls.isAnnotationPresent(aClass)) {
                list.add(cls);
            }
        }
        return list;
    }
}
