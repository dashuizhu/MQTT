package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;
import com.zj.mqtt.bean.toapp.NodeBean;
import com.zj.mqtt.bean.toapp.NodedataBean;
import com.zj.mqtt.constant.CmdString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

/**
 * @author zhuj 2018/8/30 上午9:42.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmdControlBean implements Parcelable {

    private String cmd;
    private int seq;

    private String deviceMac;

    /**
     * 调光控制
     */
    private LevelBean mv_to_level;

    private HuaSetBean movetohueandsat;

    private NodeBean node;

    private OtaBean ota_info;

    private NodedataBean nodedata;

    private ReportParaBean para;

    private CmdNetworkParaBean network_para;

    public CmdControlBean(String cmd) {
        this.cmd = cmd;
    }

    public CmdControlBean() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cmd);
        dest.writeInt(this.seq);
        dest.writeString(this.deviceMac);
        dest.writeParcelable(this.mv_to_level, flags);
        dest.writeParcelable(this.movetohueandsat, flags);
        dest.writeParcelable(this.node, flags);
        dest.writeParcelable(this.ota_info, flags);
        dest.writeParcelable(this.nodedata, flags);
        dest.writeParcelable(this.para, flags);
        dest.writeParcelable(this.network_para, flags);
    }

    protected CmdControlBean(Parcel in) {
        this.cmd = in.readString();
        this.seq = in.readInt();
        this.deviceMac = in.readString();
        this.mv_to_level = in.readParcelable(LevelBean.class.getClassLoader());
        this.movetohueandsat = in.readParcelable(HuaSetBean.class.getClassLoader());
        this.node = in.readParcelable(NodeBean.class.getClassLoader());
        this.ota_info = in.readParcelable(OtaBean.class.getClassLoader());
        this.nodedata = in.readParcelable(NodedataBean.class.getClassLoader());
        this.para = in.readParcelable(ReportParaBean.class.getClassLoader());
        this.network_para = in.readParcelable(CmdNetworkParaBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CmdControlBean> CREATOR =
            new Parcelable.Creator<CmdControlBean>() {
                @Override
                public CmdControlBean createFromParcel(Parcel source) {
                    return new CmdControlBean(source);
                }

                @Override
                public CmdControlBean[] newArray(int size) {
                    return new CmdControlBean[size];
                }
            };

    //public String getControlDetail() {
    //    StringBuffer sb = new StringBuffer();
    //    switch (cmd) {
    //        case CmdString.DEV_ONOFF:
    //            if (getNode() != null) {
    //
    //            }
    //            sb.append(getNode().isOnOff()? "开":"关");
    //            break;
    //        case CmdString.DEV_COLOR_CONTROL:
    //
    //            sb.append("hua:"+getMovetohueandsat().getHua());
    //            sb.append(" sat:"+getMovetohueandsat().getSat());
    //            sb.append(" time:"+getMovetohueandsat().getTime());
    //            break;
    //        case CmdString.DEV_LEVEL_CONTROL:
    //            sb.append("level:"+getMv_to_level().getLevel());
    //            sb.append(" time:"+getMv_to_level().getTime());
    //            break;
    //            default:
    //    }
    //    return sb.toString();
    //}
}
