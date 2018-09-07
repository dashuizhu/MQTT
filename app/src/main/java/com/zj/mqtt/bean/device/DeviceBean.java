package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import com.zj.mqtt.bean.todev.CmdControlBean;
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

    private String place;
    private boolean moreDevice;
    private String deviceMac;
    private int seq;

    private String cmd;
    private boolean controlOnOff;
    private int controlLevel;
    private int controlTime;
    private int controlHua;
    private int controlSet;


    public DeviceBean() { }

    public DeviceEndpointBean getDeviceEndpoint() {
        if (deviceEndpoint == null) {
            deviceEndpoint = new DeviceEndpointBean();
            deviceEndpoint.setMac(deviceMac);
        }
        return deviceEndpoint;
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
        dest.writeString(this.place);
        dest.writeByte(this.moreDevice ? (byte) 1 : (byte) 0);
        dest.writeString(this.deviceMac);
        dest.writeInt(this.seq);
        dest.writeString(this.cmd);
        dest.writeByte(this.controlOnOff ? (byte) 1 : (byte) 0);
        dest.writeInt(this.controlLevel);
        dest.writeInt(this.controlTime);
        dest.writeInt(this.controlHua);
        dest.writeInt(this.controlSet);
    }

    protected DeviceBean(Parcel in) {
        this.name = in.readString();
        this.nodeId = in.readInt();
        this.deviceState = in.readInt();
        this.deviceType = in.readString();
        this.deviceEndpoint = in.readParcelable(DeviceEndpointBean.class.getClassLoader());
        this.place = in.readString();
        this.moreDevice = in.readByte() != 0;
        this.deviceMac = in.readString();
        this.seq = in.readInt();
        this.cmd = in.readString();
        this.controlOnOff = in.readByte() != 0;
        this.controlLevel = in.readInt();
        this.controlTime = in.readInt();
        this.controlHua = in.readInt();
        this.controlSet = in.readInt();
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


