package com.zj.mqtt.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.adapter.ScenesAdapter;
import com.zj.mqtt.bean.ScenesBean;
import com.zj.mqtt.bean.device.DeviceBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhuj 2018/8/27 下午3:06
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.recyclerViewScenes) RecyclerView mRecyclerViewScenes;
    @BindView(R.id.recyclerViewDevice) RecyclerView mRecyclerViewDevice;
    private ScenesAdapter mScenesAdapter;
    private DeviceAdapter mDeviceAdapter;

    private Unbinder mUnbinder;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initViews();
        initData();
        return view;
    }

    private void initViews() {
        mScenesAdapter = new ScenesAdapter();
        mRecyclerViewScenes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerViewScenes.setAdapter(mScenesAdapter);
        mScenesAdapter.setNewData(new ArrayList<ScenesBean>());

        mDeviceAdapter = new DeviceAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerViewDevice.setLayoutManager(gridLayoutManager);
        mRecyclerViewDevice.setAdapter(mDeviceAdapter);
        mDeviceAdapter.setNewData(new ArrayList<DeviceBean>());


    }

    private void initData() {
        ScenesBean bean1 = new ScenesBean();
        bean1.setName("回家");

        ScenesBean bean2 = new ScenesBean();
        bean2.setName("睡眠");

        ScenesBean bean3 = new ScenesBean();
        bean3.setName("离家");

        ScenesBean bean4 = new ScenesBean();
        bean4.setName("阅读");

        mScenesAdapter.addData(bean1);
        mScenesAdapter.addData(bean2);
        mScenesAdapter.addData(bean3);
        mScenesAdapter.addData(bean4);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }
}
