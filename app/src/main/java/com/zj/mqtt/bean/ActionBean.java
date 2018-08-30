package com.zj.mqtt.bean;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/27 下午2:33.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActionBean implements Parcelable {

    private String id;
    /**
     * 对应的设备
     */
    private String deviceMac;

    private String deviceName;

    /**
     * 对应的操作
     */
    private String cmdJson;

    public ActionBean() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.deviceMac);
        dest.writeString(this.deviceName);
        dest.writeString(this.cmdJson);
    }

    protected ActionBean(Parcel in) {
        this.id = in.readString();
        this.deviceMac = in.readString();
        this.deviceName = in.readString();
        this.cmdJson = in.readString();
    }

    public static final Parcelable.Creator<ActionBean> CREATOR =
            new Parcelable.Creator<ActionBean>() {
                @Override
                public ActionBean createFromParcel(Parcel source) {
                    return new ActionBean(source);
                }

                @Override
                public ActionBean[] newArray(int size) {
                    return new ActionBean[size];
                }
            };
}
