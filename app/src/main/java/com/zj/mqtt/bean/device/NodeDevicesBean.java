package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * @author zhuj 2018/8/27 下午2:26.
 */
@Data
public class NodeDevicesBean implements Parcelable {

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

    public NodeDevicesBean() {
    }

    public DeviceEndpointBean getDeviceEndpoint() {
        if (deviceEndpoint == null) {
            deviceEndpoint = new DeviceEndpointBean();
            //deviceEndpoint.setMac(deviceMac);
        }
        return deviceEndpoint;
    }

    //public String getDeviceMac() {
    //    if (deviceEndpoint == null) {
    //        return null;
    //    }
    //    return deviceEndpoint.getMac();
    //}
    //
    //public void setDeviceMac(String mac) {
    //    if (deviceEndpoint == null) {
    //        deviceEndpoint = new DeviceEndpointBean();
    //    }
    //    deviceEndpoint.setMac(mac);
    //}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.nodeId);
        dest.writeInt(this.deviceState);
        dest.writeString(this.deviceType);
        dest.writeParcelable(this.deviceEndpoint, flags);
    }

    protected NodeDevicesBean(Parcel in) {
        this.nodeId = in.readInt();
        this.deviceState = in.readInt();
        this.deviceType = in.readString();
        this.deviceEndpoint = in.readParcelable(DeviceEndpointBean.class.getClassLoader());
    }

    public static final Creator<NodeDevicesBean> CREATOR = new Creator<NodeDevicesBean>() {
        @Override
        public NodeDevicesBean createFromParcel(Parcel source) {
            return new NodeDevicesBean(source);
        }

        @Override
        public NodeDevicesBean[] newArray(int size) {
            return new NodeDevicesBean[size];
        }
    };
}


