package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * 网络参数
 * @author zhuj 2018/8/27 下午2:17.
 */
@Data
public class CmdNetworkParaBean implements Parcelable {
    int channel;
    int power;
    String panId;

    public CmdNetworkParaBean() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.channel);
        dest.writeInt(this.power);
        dest.writeString(this.panId);
    }

    protected CmdNetworkParaBean(Parcel in) {
        this.channel = in.readInt();
        this.power = in.readInt();
        this.panId = in.readString();
    }

    public static final Parcelable.Creator<CmdNetworkParaBean> CREATOR =
            new Parcelable.Creator<CmdNetworkParaBean>() {
                @Override
                public CmdNetworkParaBean createFromParcel(Parcel source) {
                    return new CmdNetworkParaBean(source);
                }

                @Override
                public CmdNetworkParaBean[] newArray(int size) {
                    return new CmdNetworkParaBean[size];
                }
            };
}
