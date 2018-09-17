package com.zj.mqtt.adapter;

import android.text.TextUtils;
import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import java.util.List;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class DeviceListAdapter extends BaseItemDraggableAdapter<DeviceBean, BaseViewHolder> {

    public DeviceListAdapter(List<DeviceBean> data) {
        super(R.layout.recycler_item_device, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceBean item) {
        helper.setText(R.id.tv_name, item.getName())
                .setImageResource(R.id.iv, item.getPictureRes());

        if (TextUtils.isEmpty(item.getPlace())) {
            helper.setVisible(R.id.tv_place, false);
        } else {
            helper.setVisible(R.id.tv_place, true);
            helper.setText(R.id.tv_place, item.getPlace());
        }
    }
}
