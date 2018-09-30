package com.zj.mqtt.adapter;

import android.text.TextUtils;
import android.view.View;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class DeviceAdapter extends BaseQuickAdapter<DeviceBean, BaseViewHolder> {

    private boolean mShowPlace;

    public DeviceAdapter() {
        super(R.layout.recycler_item_device);
    }

    public DeviceAdapter(boolean showPlace) {
        super(R.layout.recycler_item_device);
        mShowPlace = showPlace;
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.tv_name, ""+item.getDeviceMac())
                .setImageResource(R.id.iv, item.getPictureRes());
        if (mShowPlace) {
            if (TextUtils.isEmpty(item.getPlace())) {
                helper.setVisible(R.id.tv_place, false);
            } else {
                helper.setVisible(R.id.tv_place, true);
                helper.setText(R.id.tv_place, item.getPlace());
            }
        }
        //helper.setVisible(R.id.tv_status, !item.isMoreDevice());
    }
}
