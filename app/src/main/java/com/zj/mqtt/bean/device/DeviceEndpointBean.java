package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DeviceEndpointBean implements Parcelable {
    /**
     * mac : 1
     * endpoint : 1
     * clusterInfo : [{"clusterId":1,"clusterType":"1"}]
     */

    private String mac;
    private int endpoint;
    private List<ClusterInfoBean> clusterInfo;

    public DeviceEndpointBean() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mac);
        dest.writeInt(this.endpoint);
        dest.writeList(this.clusterInfo);
    }

    protected DeviceEndpointBean(Parcel in) {
        this.mac = in.readString();
        this.endpoint = in.readInt();
        this.clusterInfo = new ArrayList<ClusterInfoBean>();
        in.readList(this.clusterInfo, ClusterInfoBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<DeviceEndpointBean> CREATOR =
            new Parcelable.Creator<DeviceEndpointBean>() {
                @Override
                public DeviceEndpointBean createFromParcel(Parcel source) {
                    return new DeviceEndpointBean(source);
                }

                @Override
                public DeviceEndpointBean[] newArray(int size) {
                    return new DeviceEndpointBean[size];
                }
            };
}