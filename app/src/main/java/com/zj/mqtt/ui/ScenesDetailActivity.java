package com.zj.mqtt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.ActionAdapter;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.database.ScenesDao;

/**
 * 场景详情界面， 动作列表
 *
 * @author zhuj 2018/8/28 下午2:58
 */
public class ScenesDetailActivity extends BaseActivity {

    private final int ACTIVITY_DEVICE_LIST = 11;
    private final int ACTIVITY_DEVICE = 12;

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.et_name) EditText mEtName;
    @BindView(R.id.btn_add) Button mBtnAdd;

    private ActionAdapter mAdapter;

    private ScenesBean mScenesBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenes_detail);
        ButterKnife.bind(this);
        initViews();
    }

    @OnClick(R.id.layout_header_back)
    public void onBackClick() {
        finish();
    }

    @OnClick(R.id.btn_add)
    public void onAdd() {
        Intent intent = new Intent(ScenesDetailActivity.this, ActionDeviceListActivity.class);
        startActivityForResult(intent, ACTIVITY_DEVICE_LIST);
    }

    @OnClick(R.id.layout_header_right)
    public void onEdit() {
        boolean edit = !mAdapter.isEdit();
        //编辑界面，再次点击就是保存
        if (!edit) {
            String name = mEtName.getText().toString();
            mScenesBean.setName(name);
            // TODO: 2018/8/28 数据库保存
            RxBus.get().post(RxBusString.RXBUS_SCENES);
            ScenesDao.saveOrUpdate(mScenesBean);
        }
        mEtName.setEnabled(edit);
        mAdapter.setEdit(edit);
        mHeaderView.getTextHeaderRight().setText(edit ? R.string.label_save : R.string.label_edit);
    }

    private void initViews() {

        mScenesBean = getIntent().getParcelableExtra(AppString.KEY_BEAN);
        mEtName.setText(mScenesBean.getName());
        mEtName.setEnabled(false);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new ActionAdapter();
        mAdapter.setNewData(mScenesBean.getActionList());

        mRecyclerView.setAdapter(mAdapter);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ScenesDetailActivity.this, DeviceControlActivity.class);
                intent.putExtra(AppString.KEY_BEAN,
                        mAdapter.getData().get(position).getControlBean());
                startActivityForResult(intent, ACTIVITY_DEVICE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        CmdControlBean bean;
        //String deviceMac;
        switch (requestCode) {
            case ACTIVITY_DEVICE_LIST:
                bean = data.getParcelableExtra(AppString.KEY_BEAN);
                //deviceMac = data.getStringExtra(AppString.KEY_MAC);
                String deviceName = getApp().getDevice(bean.getDeviceMac()).getName();

                ActionBean ab = new ActionBean();
                ab.setDeviceMac(bean.getDeviceMac());
                ab.setDeviceName(deviceName);
                ab.setControlBean(bean);

                mScenesBean.getActionList().add(ab);
                mAdapter.notifyItemInserted(mScenesBean.getActionList().size() - 1);
                break;
            case ACTIVITY_DEVICE:
                bean = data.getParcelableExtra(AppString.KEY_BEAN);
                //deviceMac = data.getStringExtra(AppString.KEY_MAC);
                //cmdJson = JSON.toJSONString(CmdPackage.getCmdByDevice(bean));

                int listSize = mAdapter.getData().size();
                for (int i = 0; i < listSize; i++) {
                    if (mAdapter.getData().get(i).getDeviceMac().equals(bean.getDeviceMac())) {
                        mAdapter.getData().get(i).setControlBean(bean);
                        mAdapter.notifyItemChanged(i);
                        break;
                    }
                }
                break;
            default:
        }
    }
}
