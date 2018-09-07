package com.person.commonlib.utils;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * toast 工具
 *
 * @author zhuj 2018/8/1 下午2:28
 */
public class ToastUtils extends Toast {
    private volatile static Toast mToast;

    private ToastUtils(Context context) {
        super(context);
    }

    private static Toast getToast(Context context) {
        if (mToast == null) {
            synchronized (ToastUtils.class) {
                mToast = ToastUtils.makeText(context, "", Toast.LENGTH_LONG);
            }
        }
        return mToast;
    }

    public static void showToast(Context context, @StringRes int resId) {
        showToast(context, context.getString(resId));
    }

    public static void showToast(Context context, String str) {
        getToast(context);
        mToast.setText(str);
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.show();
    }
}