package com.zj.mqtt.utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.person.commonlib.utils.ToastUtils;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.ui.device.DeviceDetailDimActivity;
import com.zj.mqtt.ui.device.DeviceDetailLight1Activity;
import com.zj.mqtt.ui.device.DeviceDetailLight2Activity;
import com.zj.mqtt.ui.device.DeviceDetailLight3Activity;
import com.zj.mqtt.ui.device.DeviceDetailSwitchActivity;

/**
 * @author zhuj 2018/9/16 下午4:15.
 */
public class ActivityUtils {

    public static void startDeviceDetail(Context context, DeviceBean bean) {
        Log.i("mqttDebug", "click bean:" + bean.getInfo());
        //if (!bean.isOnline()) {
        //    ToastUtils.showToast(AppApplication.getApp(), R.string.label_unonline);
        //    return;
        //}
        Intent intent = null;
        if (bean.isDeviceSwith()) {
            intent = new Intent(context, DeviceDetailSwitchActivity.class);
        } else if (bean.isDeviceLight1()) {
            intent = new Intent(context, DeviceDetailLight1Activity.class);
        } else if (bean.isDeviceLight2()) {
            intent = new Intent(context, DeviceDetailLight2Activity.class);
        } else if (bean.isDeviceLight3()) {
            intent = new Intent(context, DeviceDetailLight3Activity.class);
        } else if (bean.isDeviceDim()) {
            intent = new Intent(context, DeviceDetailDimActivity.class);
        } else if (bean.isDeviceSensor()){

        }
        if (intent != null) {
            intent.putExtra(AppString.KEY_MAC, bean.getDeviceMac());
            context.startActivity(intent);
        } else {
            ToastUtils.showToast(AppApplication.getApp(), R.string.label_device_unsupport);
        }
    }
}
