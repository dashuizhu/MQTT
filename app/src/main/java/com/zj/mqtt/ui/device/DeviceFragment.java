package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.AppType;
import com.zj.mqtt.database.DeviceDao;
import java.util.List;

/**
 * @author zhuj 2018/9/9 下午1:11
 */
public class DeviceFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DeviceAdapter mDeviceAdapter;

    private String mPlace;

    public static DeviceFragment newInstance(String place) {
        DeviceFragment fragment = new DeviceFragment();
        Bundle args = new Bundle();
        args.putString(AppString.KEY_PLACE, place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlace = getArguments().getString(AppString.KEY_PLACE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        initViews();
        return view;
    }

    private void initViews() {
        mDeviceAdapter = new DeviceAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(mDeviceAdapter);

        mDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppApplication app = (AppApplication) getActivity().getApplication();
                String mac = mDeviceAdapter.getData().get(position).getDeviceMac();
                int endPoint = mDeviceAdapter.getData().get(position).getDeviceEndpoint().getEndpoint();
                DeviceBean bean = app.getDevice(mac, endPoint);
                if (bean == null) {
                    return;
                }
                if (bean.isMoreDevice()) {
                    Intent intent = new Intent(getContext(), DeviceListActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = null;

                    switch (bean.getDeviceType()) {
                        case AppType.DEVICE_SWITCH:
                            intent = new Intent(getContext(), DeviceDetailSwitchActivity.class);
                            break;
                        case AppType.DEVICE_LIGHT:
                            //int endPoint = bean.getDeviceEndpoint().getEndpoint();
                            if (endPoint == 3) {
                                intent = new Intent(getContext(), DeviceDetailLight1Activity.class);
                            } else if (endPoint == 4) {
                                intent = new Intent(getContext(), DeviceDetailLight2Activity.class);
                            } else if (endPoint == 5) {
                                intent = new Intent(getContext(), DeviceDetailLight3Activity.class);
                            }
                            break;
                        default:
                    }
                    if (intent != null) {
                        intent.putExtra(AppString.KEY_MAC,
                                mDeviceAdapter.getData().get(position).getDeviceMac());
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.w("Test", " onHiddenChanged " + hidden);
    }

    public void refresh() {
        List<DeviceBean> list;
        if (getString(R.string.label_all).equals(mPlace)) {
            list = DeviceDao.queryList();
        } else {
            list = DeviceDao.queryListByPlace(mPlace);
        }
        Log.w("Test", " onresumt " + mPlace + " " + list.size());
        mDeviceAdapter.setNewData(list);
    }
}
