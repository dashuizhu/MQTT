package com.zj.mqtt.adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.ScenesBean;
import java.util.List;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class ScenesListAdapter extends BaseItemDraggableAdapter<ScenesBean, BaseViewHolder> {

    private boolean mEdit;

    public ScenesListAdapter(List<ScenesBean> data) {
        super(R.layout.recycler_item_scenes_list, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ScenesBean item) {
        helper.setText(R.id.tv_name, item.getName());
        helper.setImageResource(R.id.iv, item.getPictureResByList());
        helper.addOnClickListener(R.id.btn_send);
    }

    public void setEdit(boolean isEdit) {
        mEdit = isEdit;
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return mEdit;
    }
}
