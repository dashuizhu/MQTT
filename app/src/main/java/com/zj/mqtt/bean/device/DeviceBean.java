package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * @author zhuj 2018/8/27 下午2:26.
 */
@Data
public class DeviceBean implements Parcelable {

    private String name;

    /**
     * nodeId : 1
     * deviceState : 1
     * deviceType : 1
     * deviceEndpoint : {"mac":"1","endpoint":1,"clusterInfo":[{"clusterId":1,"clusterType":"1"}]}
     */

    private int nodeId;
    private int deviceState;
    private String deviceType;
    private DeviceEndpointBean deviceEndpoint;

    private boolean controlOnOff;

    public DeviceBean() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.nodeId);
        dest.writeInt(this.deviceState);
        dest.writeString(this.deviceType);
        dest.writeParcelable(this.deviceEndpoint, flags);
        dest.writeByte(this.controlOnOff ? (byte) 1 : (byte) 0);
    }

    protected DeviceBean(Parcel in) {
        this.name = in.readString();
        this.nodeId = in.readInt();
        this.deviceState = in.readInt();
        this.deviceType = in.readString();
        this.deviceEndpoint = in.readParcelable(DeviceEndpointBean.class.getClassLoader());
        this.controlOnOff = in.readByte() != 0;
    }

    public static final Creator<DeviceBean> CREATOR = new Creator<DeviceBean>() {
        @Override
        public DeviceBean createFromParcel(Parcel source) {
            return new DeviceBean(source);
        }

        @Override
        public DeviceBean[] newArray(int size) {
            return new DeviceBean[size];
        }
    };
}


