package com.zj.mqtt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.adapter.ScenesAdapter;
import com.zj.mqtt.ui.device.DeviceAddOneActivity;

public class HomeActivity extends BaseActivity {

    private final int ACTIVITY_SCENES = 11;

    @BindView(R.id.recyclerViewScenes) RecyclerView mRecyclerViewScenes;
    @BindView(R.id.recyclerViewDevice) RecyclerView mRecyclerViewDevice;
    private ScenesAdapter mScenesAdapter;
    private DeviceAdapter mDeviceAdapter;

    private Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUnbinder = ButterKnife.bind(this);
        initViews();
        registerRxBus();
    }

    private void initViews() {
        mScenesAdapter = new ScenesAdapter();
        mRecyclerViewScenes.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewScenes.setAdapter(mScenesAdapter);
        mScenesAdapter.setNewData(getApp().getScenesListShow());

        mDeviceAdapter = new DeviceAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerViewDevice.setLayoutManager(gridLayoutManager);
        mRecyclerViewDevice.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void onDestroy() {
        unRegisterRxBus();
        mUnbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.ll_scenes)
    public void onViewClicked() {
        Intent intent = new Intent(this, ScenesListActivity.class);
        startActivityForResult(intent, ACTIVITY_SCENES);
    }

    @OnClick(R.id.btn_device_add)
    public void onViewAddDeviceClicked() {
        Intent intent = new Intent(this, DeviceAddOneActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ACTIVITY_SCENES) {
            mScenesAdapter.setNewData(getApp().getScenesListShow());
        }
    }

    @Override
    protected void onResume() {
        mDeviceAdapter.setNewData(getApp().getDeviceListShow());
        super.onResume();
    }
}
