package com.zj.mqtt.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceFragmentPagerAdapter;
import com.zj.mqtt.adapter.ScenesAdapter;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.database.DeviceDao;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.ui.device.DeviceAddOneActivity;
import com.zj.mqtt.ui.device.DeviceListActivity;
import com.zj.mqtt.utils.StatusBarUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 首页
 *
 * @author zhuj 2018/9/13 下午5:45
 */
public class HomeActivity extends BaseActivity {

    private static final int ACTIVITY_MAC_RESET = 12;
    private final int ACTIVITY_SCENES = 11;

    @BindView(R.id.recyclerViewScenes) RecyclerView mRecyclerViewScenes;
    @BindView(R.id.tv_status) TextView mTvStatus;
    @BindView(R.id.tabLayout) TabLayout mTabLayout;
    @BindView(R.id.viewPager) ViewPager mViewPager;

    private ScenesAdapter mScenesAdapter;
    private DeviceFragmentPagerAdapter mFragmentPagerAdapter;
    private Unbinder mUnbinder;
    private List<String> mPlaceList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mUnbinder = ButterKnife.bind(this);
        StatusBarUtil.darkMode(this);
        StatusBarUtil.setPaddingSmart(this, mTvStatus);
        initViews();
        registerRxBus();

        MobclickAgent.onEvent(this, "openMain");

        getApp().connectService();
    }

    private void initViews() {
        mScenesAdapter = new ScenesAdapter();
        mRecyclerViewScenes.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewScenes.setAdapter(mScenesAdapter);
        mScenesAdapter.setNewData(getApp().getScenesListShow());

        //initTabLayout();
        mFragmentPagerAdapter = new DeviceFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

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

        new RxPermissions(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {

                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
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

    @OnLongClick(R.id.btn_device_add)
    public boolean onViewServerMac() {
        Intent intent = new Intent(this, MacInputActivity.class);
        startActivityForResult(intent, ACTIVITY_MAC_RESET);
        return true;
    }

    @OnClick(R.id.tv_device_manager)
    public void onDeviceManager() {
        Intent intent = new Intent(this, DeviceListActivity.class);
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
        } else if (requestCode == ACTIVITY_MAC_RESET) {
            getApp().resetService();
        }
    }

    @Override
    protected void onStop() {
        //避免进入，显示了状态栏红色，导致状态栏色调更改，所以隐藏顶部红色
        mTvStatus.setVisibility(View.GONE);
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Observable.timer(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (getApp().isConnect()) {
                            mTvStatus.setVisibility(View.GONE);
                        } else {
                            mTvStatus.setVisibility(View.VISIBLE);
                            if (!getApp().isConnecting()) {
                                boolean isLing = getApp().isConnecting();
                                mTvStatus.setEnabled(true);
                                mTvStatus.setText(
                                        isLing ? R.string.label_linking : R.string.label_link_fail);

                            }
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Set<String> set = DeviceDao.getPlaceSet();
        List<String> list = new ArrayList<>(set);
        list.add(0, getString(R.string.label_all));

        if (mPlaceList != null) {
            mTabLayout.removeAllTabs();
        }
        mPlaceList = list;
        mFragmentPagerAdapter.setData(mPlaceList);
        mFragmentPagerAdapter.notifyDataSetChanged();
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

                getApp().publishMsgToServer(CmdPackage.getNodeList());

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

    //@Subscribe(thread = EventThread.MAIN_THREAD, tags = {
    //        @Tag, @Tag(RxBusString.RXBUS_PARSE)
    //})
    //public void onReceiveAction(CmdResult result) {
    //    switch (result.getCmd()) {
    //        case CmdParse.CMD_NODE_LIST:
    //            int curr = mViewPager.getCurrentItem();
    //            int count = mFragmentPagerAdapter.getCount();
    //            //DeviceFragment deviceFragment =
    //            //        (DeviceFragment) mFragmentPagerAdapter.get(curr);
    //            //if (deviceFragment.isAdded()) {
    //            //    deviceFragment.refresh();
    //            //}
    //            break;
    //        default:
    //    }
    //}

    @OnClick(R.id.tv_status)
    public void onStatusClick() {
        getApp().connectService();
    }

    private void initTabLayout() {
        Set<String> set = DeviceDao.getPlaceSet();
        mPlaceList = new ArrayList<>(set);
        mPlaceList.add(0, getString(R.string.label_all));
    }
}
