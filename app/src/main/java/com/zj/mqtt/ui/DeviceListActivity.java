package com.zj.mqtt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.bean.toapp.CmdNodeLeftResult;
import com.zj.mqtt.bean.toapp.CmdNodeListResult;
import com.zj.mqtt.bean.toapp.CmdNodeResult;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.bean.toapp.CmdStateChagneResult;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;

/**
 * 设备列表
 * @author zhuj 2018/8/28 下午4:03
 */
public class DeviceListActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    private DeviceAdapter mAdapter;

    private final int ACTIVITY_CONTROL = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        initViews();
    }

    @OnClick(R.id.layout_header_back)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.layout_header_right)
    public void onAdd() {

    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //mRecyclerView.addItemDecoration(new RecyclerViewDivider(this, LinearLayoutManager.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mAdapter = new DeviceAdapter();
        mAdapter.setNewData( getApp().getDevcieList());

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //Intent intent = new Intent();
                //intent.putExtra(AppString.KEY_BEAN, mAdapter.getData().get(position));
                //setResult(RESULT_OK, intent);
                //finish();

                Intent intent = new Intent(DeviceListActivity.this, DeviceControlActivity.class);

                // TODO: 2018/8/31 这里默认值，一定要注意 设备类型， 和 默认控制数据
                CmdControlBean controlBean = new CmdControlBean(CmdString.DEV_ONOFF);
                controlBean.setDeviceMac(mAdapter.getData().get(position).getDeviceEndpoint().getMac());
                intent.putExtra(AppString.KEY_BEAN, controlBean);
                startActivityForResult(intent, ACTIVITY_CONTROL);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceBean bean = mAdapter.getData().get(position);
                bean.setControlOnOff( !bean.isControlOnOff());
                mAdapter.notifyItemChanged(position);
            }
        });

    }

    @Subscribe(thread = EventThread.MAIN_THREAD
            , tags = @Tag(RxBusString.RXBUS_PARSE)
    )
    public void onDeviceChange(CmdResult result) {
        if (result instanceof CmdStateChagneResult
                || result instanceof CmdNodeLeftResult
                || result instanceof CmdNodeResult
                || result instanceof CmdNodeListResult) {
            mAdapter.setNewData(getApp().getDevcieList());
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        mAdapter.setNewData(getApp().getDevcieList());
        mAdapter.notifyDataSetChanged();
        registerRxBus();
        super.onStart();
    }

    @Override
    protected void onStop() {
        unRegisterRxBus();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == ACTIVITY_CONTROL) {
            CmdControlBean bean = data.getParcelableExtra(AppString.KEY_BEAN);

            Intent intent = new Intent();
            intent.putExtra(AppString.KEY_BEAN, bean);
            setResult(RESULT_OK, intent);
            finish();

        }
    }
}
