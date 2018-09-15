package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.view.View;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.ClusterInfoBean;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.ui.BaseActivity;
import java.util.List;

/**
 * 设备控制详情
 *
 * @author zhuj 2018/8/28 下午4:01
 */
public class DeviceDetailActivity extends BaseActivity {

    HeaderView mHeaderView;
    DeviceBean mDeviceBean;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mHeaderView = findViewById(R.id.headerView);
        initViews();
        registerRxBus();
    }

    private void initViews() {
        String mac = getIntent().getStringExtra("mac");
        mDeviceBean = getApp().getDevice(mac);
        if (mDeviceBean == null) {
            showToast(R.string.toast_device_notfound);
            finish();
            return;
        }
        mHeaderView.setTitle(mDeviceBean.getName());

        findViewById(R.id.layout_header_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.layout_header_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailActivity.this, DeviceSettingActivity.class);
                intent.putExtra(AppString.KEY_BEAN, mDeviceBean);
                startActivityForResult(intent, 11);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (data != null && data.hasExtra(AppString.KEY_DELETE)) {
            finish();
        } else {
            mHeaderView.setTitle(mDeviceBean.getName());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterRxBus();
    }
}
