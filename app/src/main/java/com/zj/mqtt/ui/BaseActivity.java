package com.zj.mqtt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.hwangjr.rxbus.RxBus;
import com.person.commonlib.utils.DensityHelp;
import com.person.commonlib.utils.ToastUtils;
import com.zj.mqtt.AppApplication;

/**
 * @author zhuj 2018/8/27 下午3:51.
 */
public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();

    private boolean isRxRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DensityHelp.setDefault(this);
    }

    protected void registerRxBus() {
        if (!isRxRegister) {
            synchronized (this) {
                if (!isRxRegister) {
                    RxBus.get().register(this);
                    isRxRegister = true;
                }
            }
        }
    }

    protected void unRegisterRxBus() {
        if (isRxRegister) {
            synchronized (this) {
                if (isRxRegister) {
                    RxBus.get().unregister(this);
                    isRxRegister = false;
                }
            }
        }
    }

    protected void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    protected void showToast(String str) {
        ToastUtils.showToast(this, str);
    }

    public AppApplication getApp() {
        return (AppApplication) getApplication();
    }

}
