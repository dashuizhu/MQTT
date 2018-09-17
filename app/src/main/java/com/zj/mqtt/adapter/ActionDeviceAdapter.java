package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class ActionDeviceAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {

    private boolean mShowPlace;

    public ActionDeviceAdapter() {
        super(R.layout.recycler_item_action_device);
    }

    public ActionDeviceAdapter(boolean showPlace) {
        super(R.layout.recycler_item_device);
        mShowPlace = showPlace;
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.tv_name, "" + item.getName())
                .setImageResource(R.id.iv, item.getPictureRes())
                .setVisible(R.id.tv_place, true);
    }
}
