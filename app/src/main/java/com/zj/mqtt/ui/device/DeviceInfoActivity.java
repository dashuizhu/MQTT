package com.zj.mqtt.ui.device;

import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.view.MyItemView;

/**
 * 设备设置
 *
 * @author zhuj 2018/9/5 下午6:40
 */
public class DeviceInfoActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.item_company) MyItemView mItemCompany;
    @BindView(R.id.item_mac) MyItemView mItemMac;
    @BindView(R.id.item_serial) MyItemView mItemSerial;
    @BindView(R.id.item_model) MyItemView mItemModel;

    private DeviceBean mDeviceBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mDeviceBean = getIntent().getParcelableExtra(AppString.KEY_BEAN);
        mItemMac.setTextContent(mDeviceBean.getDeviceMac());
        mItemModel.setTextContent(mDeviceBean.getDeviceType());
    }

    @OnClick(R.id.layout_header_back)
    public void onFinishClick() {
        finish();
    }
}
