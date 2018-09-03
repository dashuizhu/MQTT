package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhuj 2018/8/30 上午9:50.
 */
public class HuaSetBean implements Parcelable {

    /**
     * hua : 120
     * sat : 254
     * time : 0
     */

    private int hua;
    private int sat;
    private int time;

    public int getHua() {
        return hua;
    }

    public void setHua(int hua) {
        this.hua = hua;
    }

    public int getSat() {
        return sat;
    }

    public void setSat(int sat) {
        this.sat = sat;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.hua);
        dest.writeInt(this.sat);
        dest.writeInt(this.time);
    }

    public HuaSetBean() {
    }

    protected HuaSetBean(Parcel in) {
        this.hua = in.readInt();
        this.sat = in.readInt();
        this.time = in.readInt();
    }

    public static final Parcelable.Creator<HuaSetBean> CREATOR =
            new Parcelable.Creator<HuaSetBean>() {
                @Override
                public HuaSetBean createFromParcel(Parcel source) {
                    return new HuaSetBean(source);
                }

                @Override
                public HuaSetBean[] newArray(int size) {
                    return new HuaSetBean[size];
                }
            };
}
