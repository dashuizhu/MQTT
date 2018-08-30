package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.device.DeviceBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class ActionAdapter extends BaseQuickAdapter<ActionBean, BaseViewHolder> {

    private boolean mEdit;

    public ActionAdapter() {
        super(R.layout.recycler_item_scenes);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionBean item) {
        helper.setText(R.id.tv_name, item.getDeviceName() + "\n"+ item.getDeviceMac());
    }

    public void setEdit(boolean edit) {
        mEdit = edit;
    }

    public boolean isEdit() {
        return mEdit;
    }
}
