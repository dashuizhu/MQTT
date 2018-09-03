package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zhuj 2018/8/30 上午9:47.
 */
public class LevelBean implements Parcelable {

    /**
     * level : 1
     * time : 1
     */

    private int level;
    private int time;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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
        dest.writeInt(this.level);
        dest.writeInt(this.time);
    }

    public LevelBean() {
    }

    protected LevelBean(Parcel in) {
        this.level = in.readInt();
        this.time = in.readInt();
    }

    public static final Parcelable.Creator<LevelBean> CREATOR =
            new Parcelable.Creator<LevelBean>() {
                @Override
                public LevelBean createFromParcel(Parcel source) {
                    return new LevelBean(source);
                }

                @Override
                public LevelBean[] newArray(int size) {
                    return new LevelBean[size];
                }
            };
}
