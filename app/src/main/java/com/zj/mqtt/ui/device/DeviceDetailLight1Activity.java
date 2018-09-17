package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.utils.StatusBarUtil;

/**
 * @author zhuj 2018/9/13 下午7:35.
 */
public class DeviceDetailLight1Activity extends DeviceDetailActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.tv_light1) TextView mTvLight1;
    @BindView(R.id.tv_onoff1) TextView mTvOnoff1;

    private DeviceEndpointBean mEndpointBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail_light1);
        ButterKnife.bind(this);

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPadding(this, mHeaderView);
        initViews();
    }

    private void initViews() {

        mEndpointBean = mDeviceBean.getEndpointList().get(0);

        mTvLight1.setSelected(true);
        mTvLight1.setText(R.string.label_click_on);

        mTvOnoff1.setText(R.string.label_click_on);
        mTvOnoff1.setSelected(true);
    }

    @OnClick({ R.id.tv_onoff1, R.id.tv_light1 })
    public void onClickSwitch() {
        CmdControlBean control =
                CmdPackage.setOnOff(!mDeviceBean.isControlOnOff(), mDeviceBean.getDeviceMac(),
                        mEndpointBean.getEndpoint());

        getApp().publishMsgToServer(control);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void onKidChange(String action) {
        if (action.equals(CmdString.DEV_ONOFF)) {

        }
    }
}
