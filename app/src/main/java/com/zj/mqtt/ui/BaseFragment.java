package com.zj.mqtt.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.View;
import com.hwangjr.rxbus.RxBus;
import com.person.commonlib.utils.ToastUtils;
import com.person.commonlib.view.ProgressDialog;

/**
 * Created by zhuj on 17/6/8.
 */
public abstract class BaseFragment extends Fragment {

    protected final String TAG = getClass().getSimpleName();

    private boolean isRxRegister;
    private ProgressDialog mProgressDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    protected void showToast(String str) {
        ToastUtils.showToast(getContext(), str);
    }

    protected void showToast(@StringRes int resId) {
        showToast(getString(resId));
    }

    @Override
    public void onDestroy() {
        unRegisterRxBus();
        super.onDestroy();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
