package com.zj.mqtt.utils.sharedPresenter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用来保存app的 数据，更换用户时可以不清空
 *
 * @author zhuj
 * @date 2017/6/15 下午6:11
 */
public class SharedPreApp extends BaseSharedPre {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "buddy_teacher_share_app_data";

    private static volatile SharedPreApp mSharedPre;

    /**
     * 版本更新红点提醒
     */
    public static final String KEY_REMIND_VERSION = "key_remind_version";

    public static final String KEY_USER_NAME = "key_user_name";

    public static final String KEY_PASSWORD = "key_password";

    public static final String LAST_LOGIN_STATE = "last_login_state";

    public static final String KEY_LAST_VERSION_TIME = "key_last_version_time";

    @Override
    SharedPreferences getSharedPreferences(Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreApp getInstance() {
        if (mSharedPre == null) {
            synchronized (SharedPreApp.class) {
                if (mSharedPre == null) {
                    mSharedPre = new SharedPreApp();
                }
            }
        }
        return mSharedPre;
    }
}
