package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.ScenesBean;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class ScenesAdapter extends BaseQuickAdapter<ScenesBean, BaseViewHolder> {

    public ScenesAdapter() {
        super(R.layout.recycler_item_scenes);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenesBean item) {
        helper.setText(R.id.tv_name, item.getName());
    }
}
