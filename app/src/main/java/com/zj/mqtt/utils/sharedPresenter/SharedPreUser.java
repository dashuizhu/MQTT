package com.zj.mqtt.utils.sharedPresenter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用来保存user相关数据， 切换用户，直接清除文件
 *
 * @author zhuj
 * @date 2017/6/15 下午6:11
 */
public class SharedPreUser extends BaseSharedPre {
    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "buddyTeacher_share_user_data";
    /**
     * 用户昵称
     */
    public static final String KEY_USER_NAME = "key_user_nick";

    /**
     * 默认用户名
     */
    public static final String KEY_USER_NAME_SYS = "key_user_name";
    /**
     * 用户头像
     */
    public static final String KEY_USER_AVATOR = "key_userinfo_avator";

    public static final String KEY_IS_PASSWORD = "key_is_password";

    public static final String KEY_USER_INVITE_CODE = "key_user_invite_code";

    private static SharedPreUser mSharedPre;

    /**
     * userId
     */
    public static final String KEY_USER_ID = "userId";

    /**
     * accessToken
     */
    public static final String KEY_TOKEN = "accessToken";
    /**
     * token过期时间
     */
    public static final String KEY_TOKEN_EXPIRED = "accessTokenExpired";

    @Override
    SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreUser getInstance() {
        if (mSharedPre == null) {
            synchronized (SharedPreUser.class) {
                if (mSharedPre == null) {
                    mSharedPre = new SharedPreUser();
                }
            }
        }
        return mSharedPre;
    }
}
