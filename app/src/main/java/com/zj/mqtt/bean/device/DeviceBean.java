package com.zj.mqtt.bean.device;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import com.zj.mqtt.R;
import com.zj.mqtt.constant.AppType;
import java.util.List;
import lombok.Data;

/**
 * @author zhuj 2018/8/27 下午2:26.
 */
@Data
public class DeviceBean implements Parcelable {

    public static final int STATE_JOIN = 16;
    /**
     * 无应答
     */
    public static final int STATE_UNRESPONSE = 17;
    /**
     * 离开
     */
    public static final int STATE_LEfT = 30;

    /**
     * nodeId : 1
     * deviceState : 1
     * deviceType : 1
     * deviceEndpoint : {"mac":"1","endpoint":1,"clusterInfo":[{"clusterId":1,"clusterType":"1"}]}
     */
    private String id;
    private int nodeId;
    private int deviceState;
    private String deviceType;
    private String name;
    private String deviceMac;

    private List<DeviceEndpointBean> endpointList;

    private String place;
    //private boolean moreDevice;
    private int seq;


    /**
     * 以下是缓存的控制数据
     */
    private boolean controlOnOff;
    private int controlLevel;
    private int controlTime;
    private int controlHua;
    private int controlSet;

    public String getId() {
        return deviceMac;
    }

    public String getName() {
        if (!TextUtils.isEmpty(name)) {
            return name;
        }
        name = "";
        switch (deviceType) {
            case AppType.DEVICE_SWITCH:
                name = "插座"+getDeviceMac();
                break;
            case AppType.DEVICE_LIGHT:
                name = "开关"+getDeviceMac();
                break;
            case AppType.DEVICE_DIM:
                name = "调光设备"+getDeviceMac();
                break;
            case AppType.DEVICE_SWITCH_LIGHT:
                name = "调光设备"+getDeviceMac();
                break;
            case AppType.DEVICE_SENSOR:
                name = "传感器"+getDeviceMac();
                break;
            case AppType.DEVICE_RGB:
                name = "RGB" + getDeviceMac();
            default:
        }
        return name;
    }

    //public List<DeviceEndpointBean> getEndpointList() {
    //    if (isDeviceLight3()) {
    //
    //    }
    //}

    /**
     * 是否是控制设备
     * @return
     */
    public boolean isDeviceSwith() {
       return AppType.DEVICE_SWITCH.equals(getDeviceType());
    }

    /**
     * 是否是调光设备
     * @return
     */
    public boolean isDeviceDim() {
        return AppType.DEVICE_RGB.equals(getDeviceType());
    }

    public boolean isDeviceRGB() {
        return AppType.DEVICE_RGB.equals(getDeviceType());
    }

    /**
     * 是否是传感器设备
     * @return
     */
    public boolean isDeviceSensor() {
        return AppType.DEVICE_SENSOR.equals(getDeviceType());
    }

    /**
     * 一路开关
     * @return
     */
    public boolean isDeviceLight1() {
        if (endpointList == null) {
            return false;
        }
        if (AppType.DEVICE_LIGHT.equals(getDeviceType())) {
            return endpointList.size() ==1;
        }
        return false;
    }

    public boolean isDeviceLight2() {
        if (endpointList == null) {
            return false;
        }
        if (AppType.DEVICE_LIGHT.equals(getDeviceType())) {
            return endpointList.size() == 2;
        }
        return false;
    }

    public boolean isDeviceLight3() {
        if (endpointList == null) {
            return false;
        }
        if (AppType.DEVICE_LIGHT.equals(getDeviceType())) {
            return endpointList.size() == 3;
        }
        return false;
    }

    public @DrawableRes
    int getPictureRes() {
        return R.mipmap.device_model_switch_img;
    }

    public DeviceBean() {
    }

    //public DeviceEndpointBean getDeviceEndpoint() {
    //    if (deviceEndpoint == null) {
    //        deviceEndpoint = new DeviceEndpointBean();
    //        //deviceEndpoint.setMac(deviceMac);
    //    }
    //    return deviceEndpoint;
    //}
    //
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

    public boolean isOnline() {
        return (deviceState == STATE_JOIN);
    }

    public String getInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("deviceState:");
        sb.append(deviceState);
        sb.append(", deviceMac:");
        sb.append(deviceMac);
        sb.append(", deviceType:");
        sb.append(deviceType);
        sb.append(", endPoint [");
        if (endpointList != null) {
            for (DeviceEndpointBean end : endpointList) {
                sb.append(end.getEndpoint()+", ");
            }
        }
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeInt(this.nodeId);
        dest.writeInt(this.deviceState);
        dest.writeString(this.deviceType);
        dest.writeString(this.name);
        dest.writeString(this.deviceMac);
        dest.writeTypedList(this.endpointList);
        dest.writeString(this.place);
        dest.writeInt(this.seq);
        dest.writeByte(this.controlOnOff ? (byte) 1 : (byte) 0);
        dest.writeInt(this.controlLevel);
        dest.writeInt(this.controlTime);
        dest.writeInt(this.controlHua);
        dest.writeInt(this.controlSet);
    }

    protected DeviceBean(Parcel in) {
        this.id = in.readString();
        this.nodeId = in.readInt();
        this.deviceState = in.readInt();
        this.deviceType = in.readString();
        this.name = in.readString();
        this.deviceMac = in.readString();
        this.endpointList = in.createTypedArrayList(DeviceEndpointBean.CREATOR);
        this.place = in.readString();
        this.seq = in.readInt();
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


