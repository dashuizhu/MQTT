package com.zj.mqtt.adapter;

import android.text.TextUtils;
import android.util.Log;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.ActionBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.constant.CmdString;

/**
 * @author zhuj 2018/8/27 下午3:08.
 */
public class ActionAdapter extends BaseQuickAdapter<ActionBean, BaseViewHolder> {

    private boolean mEdit;

    public ActionAdapter() {
        super(R.layout.recycler_item_action);
    }

    @Override
    protected void convert(BaseViewHolder helper, ActionBean item) {
        Log.w("test", helper.getAdapterPosition() + " " + item.toString());
        helper.setText(R.id.tv_name, item.getDeviceName()+"\n" + getControlDetail(item.getControlBean()))
                .setImageResource(R.id.iv, item.getPictureRes());
    }

    public void setEdit(boolean edit) {
        mEdit = edit;
    }

    public boolean isEdit() {
        return mEdit;
    }

    public String getControlDetail(CmdControlBean controlBean) {
        if (controlBean == null || TextUtils.isEmpty(controlBean.getCmd())) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        switch (controlBean.getCmd()) {
            case CmdString.DEV_ONOFF:
                if (controlBean.getNode() != null) {
                    sb.append(controlBean.getNode().value2onOff() ? "开" : "关");
                } else {
                    sb.append("关");
                }
                break;
            case CmdString.DEV_COLOR_CONTROL:

                sb.append("hua:" + controlBean.getMovetohueandsat().getHua());
                sb.append(" sat:" + controlBean.getMovetohueandsat().getSat());
                sb.append(" time:" + controlBean.getMovetohueandsat().getTime());
                break;
            case CmdString.DEV_LEVEL_CONTROL:
                sb.append("level:" + controlBean.getMv_to_level().getLevel());
                sb.append(" time:" + controlBean.getMv_to_level().getTime());
                break;
            default:
        }
        return sb.toString();
    }
}
