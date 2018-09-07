package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

@Data
public class ClusterInfoBean implements Parcelable {
    /**
     * clusterId : 1
     * clusterType : 1
     */

    private int clusterId;
    private String clusterType;

    public ClusterInfoBean () {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clusterId);
        dest.writeString(this.clusterType);
    }

    protected ClusterInfoBean(Parcel in) {
        this.clusterId = in.readInt();
        this.clusterType = in.readString();
    }

    public static final Parcelable.Creator<ClusterInfoBean> CREATOR =
            new Parcelable.Creator<ClusterInfoBean>() {
                @Override
                public ClusterInfoBean createFromParcel(Parcel source) {
                    return new ClusterInfoBean(source);
                }

                @Override
                public ClusterInfoBean[] newArray(int size) {
                    return new ClusterInfoBean[size];
                }
            };
}