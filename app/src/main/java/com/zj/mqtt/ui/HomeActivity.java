package com.zj.mqtt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.umeng.analytics.MobclickAgent;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.adapter.ScenesAdapter;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.ui.device.DeviceAddOneActivity;
import com.zj.mqtt.ui.device.DeviceDetailActivity;
import com.zj.mqtt.ui.device.DeviceListActivity;

public class HomeActivity extends BaseActivity {

    private final int ACTIVITY_SCENES = 11;

    @BindView(R.id.recyclerViewScenes) RecyclerView mRecyclerViewScenes;
    @BindView(R.id.recyclerViewDevice) RecyclerView mRecyclerViewDevice;
    @BindView(R.id.tv_status) TextView mTvStatus;
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

        getApp().connectService();

        if (getApp().isConnect()) {
            mTvStatus.setVisibility(View.GONE);
        }
        MobclickAgent.onEvent(this, "openMain");
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

        mDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceBean bean = mDeviceAdapter.getData().get(position);
                if (bean.isMoreDevice()) {
                    Intent intent = new Intent(HomeActivity.this, DeviceListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(HomeActivity.this, DeviceDetailActivity.class);
                    intent.putExtra(AppString.KEY_MAC,
                            mDeviceAdapter.getData().get(position).getDeviceMac());
                    startActivity(intent);
                }
            }
        });

        mScenesAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!getApp().isConnect()) {
                    showToast(R.string.toast_service_unlink);
                    return;
                }
                ScenesBean scenesBean = mScenesAdapter.getData().get(position);
                if (scenesBean.getActionList() != null) {
                    for (ActionBean actionBean : scenesBean.getActionList()) {
                        getApp().publishMsgToServer(actionBean.getControlBean());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        unRegisterRxBus();
        mUnbinder.unbind();
        getApp().stopConnect();
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

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag, @Tag(RxBusString.LINK_SUCCESS), @Tag(RxBusString.LINKING),
            @Tag(RxBusString.LINKFAIL)
    })
    public void onReceiveAction(String action) {
        switch (action) {
            case RxBusString.LINK_SUCCESS:
                mTvStatus.setEnabled(false);
                mTvStatus.setVisibility(View.GONE);
                break;
            case RxBusString.LINKING:
                mTvStatus.setVisibility(View.VISIBLE);
                mTvStatus.setEnabled(false);
                mTvStatus.setText(R.string.label_linking);
                break;
            case RxBusString.LINKFAIL:
                mTvStatus.setEnabled(true);
                mTvStatus.setVisibility(View.VISIBLE);
                mTvStatus.setText(R.string.label_link_fail);
                break;
            default:
        }
    }

    @OnClick(R.id.tv_status)
    public void onStatusClick() {
        getApp().connectService();
    }
}
