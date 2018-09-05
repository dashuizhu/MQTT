package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class DevicePlaceAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int mSelectPosition = 0;

    public DevicePlaceAdapter() {
        super(R.layout.recycler_item_device_place);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_name, item);

        helper.getView(R.id.iv).setSelected( helper.getLayoutPosition() == mSelectPosition);
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        notifyItemChanged(mSelectPosition);
        mSelectPosition = selectPosition;
        notifyItemChanged(mSelectPosition);
    }
}
