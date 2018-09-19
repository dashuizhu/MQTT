package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.utils.DensityUtil;
import com.person.commonlib.view.RecyclerViewSpaceItemDecoration;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceAdapter;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.database.DeviceDao;
import com.zj.mqtt.protocol.CmdParse;
import com.zj.mqtt.ui.BaseFragment;
import com.zj.mqtt.utils.ActivityUtils;
import java.util.List;

/**
 * @author zhuj 2018/9/9 下午1:11
 */
public class DeviceFragment extends BaseFragment {

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
        mDeviceAdapter = new DeviceAdapter("全部".equals(mPlace));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        int padding = DensityUtil.dip2px(getContext(), 16);
        mRecyclerView.addItemDecoration(new RecyclerViewSpaceItemDecoration(padding, 3, false));
        mRecyclerView.setAdapter(mDeviceAdapter);

        mDeviceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AppApplication app = (AppApplication) getActivity().getApplication();
                String mac = mDeviceAdapter.getData().get(position).getDeviceMac();
                DeviceBean bean = app.getDevice(mac);
                if (bean == null) {
                    return;
                }
                ActivityUtils.startDeviceDetail(getContext(), bean);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("Test", "fagment onresume " + mPlace);
        if (!isHidden()) {
            refresh();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.w("Test", "fagment onStart " + mPlace);
        if (!isHidden()) {
            registerRxBus();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        unRegisterRxBus();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.w("Test", " onHiddenChanged " + mPlace + hidden);
        if (hidden) {
            unRegisterRxBus();
        } else {
            registerRxBus();
            refresh();
        }

    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {
            @Tag, @Tag(RxBusString.RXBUS_PARSE)
    })
    public void onReceiveAction(CmdResult result) {
        switch (result.getCmd()) {
            case CmdParse.CMD_NODE_LIST:
                refresh();
                break;
            default:
        }
    }

    public void refresh() {
        List<DeviceBean> list;
        if (getString(R.string.label_all).equals(mPlace)) {
            list = DeviceDao.queryList();
        } else {
            list = DeviceDao.queryListByPlace(mPlace);
        }
        mDeviceAdapter.setNewData(list);
    }
}
