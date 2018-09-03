package com.zj.mqtt.bean;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.JSON;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhuj 2018/8/27 下午2:33.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ActionBean implements Parcelable {

    /**
     * 对应的设备
     */
    private String deviceMac;

    private String deviceName;

    /**
     * 对应的操作， 避免CmdControlBean parcelable麻烦，用个字符串代替。
     * 并存在于数据库中
     */
    //private String cmdJson;

    private CmdControlBean controlBean;

    public ActionBean() {

    }

    //public CmdControlBean getControlBean() {
    //    controlBean = JSON.parseObject(cmdJson, CmdControlBean.class);
    //    return controlBean;
    //}


    public String getControlBeanJson() {
        return JSON.toJSONString(controlBean);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.deviceMac);
        dest.writeString(this.deviceName);
        dest.writeParcelable(this.controlBean, flags);
    }

    protected ActionBean(Parcel in) {
        this.deviceMac = in.readString();
        this.deviceName = in.readString();
        this.controlBean = in.readParcelable(CmdControlBean.class.getClassLoader());
    }

    public static final Creator<ActionBean> CREATOR = new Creator<ActionBean>() {
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
