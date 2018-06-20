package com.opengl.deng.testnewrelic.analyzesdk;

/**
 * @Description used for user information saving
 *
 * Created by deng on 2018/6/20.
 */
public class Constants {
    public interface UserInfo {
        String USER_ID_ATTRIBUTE = "userId";
        String APP_BUILD_ATTRIBUTE = "appBuild";
        String APP_NAME_ATTRIBUTE = "appName";
        String APPLICATION_PLATFORM_ATTRIBUTE = "platform";
        String APPLICATION_PLATFORM_VERSION_ATTRIBUTE = "platformVersion";
        String OS_NAME_ATTRIBUTE = "osName";
        String OS_VERSION_ATTRIBUTE = "osVersion";
        String DEVICE_ID_ATTRIBUTE = "deviceId";
    }

    public interface Category {
        String EVENT_TYPE_ATTRIBUTE_MOBILE_REQUEST = "MobileRequest";
        String EVENT_TYPE_ATTRIBUTE_MOBILE_REQUEST_ERROR = "MobileRequestError";
        String EVENT_TYPE_ATTRIBUTE_MOBILE_BREADCRUMB = "MobileBreadcrumb";
        String EVENT_TYPE_ATTRIBUTE_MOBILE_CRASH = "MobileCrash";
    }

    public interface App_type {
        String APP_INSTALL_ATTRIBUTE = "install";
        String APP_UPGRADE_ATTRIBUTE = "upgradeFrom";
    }
}
