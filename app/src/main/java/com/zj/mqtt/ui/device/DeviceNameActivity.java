package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DevicePlaceAdapter;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.constant.AppConstants;
import com.zj.mqtt.ui.BaseActivity;

public class DeviceNameActivity extends BaseActivity {

    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private DevicePlaceAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_name);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {

        mAdapter = new DevicePlaceAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setNewData(AppConstants.getDevicePlaceList());

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelectPosition(position);
            }
        });
    }

    @OnClick(R.id.btn_next)
    public void onNextClick() {
        String name = mEtName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast(R.string.toast_input_empty);
            return;
        }
        int postion = mAdapter.getSelectPosition();
        String place = mAdapter.getData().get(postion);

        DeviceBean bean = new DeviceBean();
        bean.setName(name);

        DeviceEndpointBean endpointBean = new DeviceEndpointBean();
        endpointBean.setMac(place + "devicemac");

        bean.setDeviceEndpoint(endpointBean);
        getApp().addDevice(bean);
        finish();
    }
}
