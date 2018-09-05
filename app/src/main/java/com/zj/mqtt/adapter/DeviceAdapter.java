package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {

    public DeviceAdapter() {
        super(R.layout.recycler_item_device);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setVisible(R.id.tv_status, !item.isMoreDevice());
    }
}
