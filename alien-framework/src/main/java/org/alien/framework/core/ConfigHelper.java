package org.alien.framework.core;

import org.alien.framework.utils.PropsUtil;

import java.util.Properties;

import static org.alien.framework.ConfigConstant.*;

/**
 * 属性文件帮助类
 */
public final class ConfigHelper {

    private static final Properties CONFIG_PROPS = PropsUtil.loadProps(CONFIG_FILE);

    /**
     * 获取JDBC驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS, JDBC_DRIVER);
    }

    /**
     * 获取JDBC URL
     * @return
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,JDBC_URL);
    }

    /**
     * 获取JDBC用户名
     * @return
     */
    public static String getJdbcUsername(){
        return PropsUtil.getString(CONFIG_PROPS,JDBC_USERNAME);
    }

    /**
     * 获取JDBC密码
     * @return
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,JDBC_PASSWORD);
    }

    /**
     * 获取应用基础包名
     * @return
     */
    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,APP_BASE_PACKAGE);
    }

    /**
     * 获取应用JSP路径(默认 /WEN-INF/view)
     * @return
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,APP_JSP_PATH,"/WEB-INF/view");
    }

    /**
     * 静态资源路径
     * @return
     */
    public static String getAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,APP_ASSET_PATH,"/asset/");
    }

}
