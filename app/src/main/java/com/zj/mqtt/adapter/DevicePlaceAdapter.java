package com.zj.mqtt.adapter;

import android.text.TextUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;

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

        helper.getView(R.id.iv).setSelected(helper.getLayoutPosition() == mSelectPosition);
    }

    public int getSelectPosition() {
        return mSelectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        notifyItemChanged(mSelectPosition);
        mSelectPosition = selectPosition;
        notifyItemChanged(mSelectPosition);
    }

    public void initSelect(String nowPlace) {
        if (TextUtils.isEmpty(nowPlace)) {
            return;
        }
        int size = getData().size();
        for (int i = 0; i < size; i++) {
            if (getData().get(i).equals(nowPlace)) {
                mSelectPosition = i;
            }
        }
    }
}
