package com.zj.mqtt.bean.toapp;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

@Data
public class NodeBean implements Parcelable {
    /**
     * mac : string
     * endpoint : 1
     */

    private String mac;
    private Integer endpoint;
    /**
     * 开关控制，只有在cmd = onoff
     */
    private Integer value;

    private Integer clusterId;

    private Integer attributeId;

    private String key;

    public NodeBean() {}

    /**
     * get is开头方法，会生成json key value
     * @return
     */
    public  boolean value2onOff() {
        if (value == null) {
            return false;
        }
        return value == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mac);
        dest.writeValue(this.endpoint);
        dest.writeValue(this.value);
        dest.writeValue(this.clusterId);
        dest.writeValue(this.attributeId);
        dest.writeString(this.key);
    }

    protected NodeBean(Parcel in) {
        this.mac = in.readString();
        this.endpoint = (Integer) in.readValue(Integer.class.getClassLoader());
        this.value = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clusterId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attributeId = (Integer) in.readValue(Integer.class.getClassLoader());
        this.key = in.readString();
    }

    public static final Parcelable.Creator<NodeBean> CREATOR = new Parcelable.Creator<NodeBean>() {
        @Override
        public NodeBean createFromParcel(Parcel source) {
            return new NodeBean(source);
        }

        @Override
        public NodeBean[] newArray(int size) {
            return new NodeBean[size];
        }
    };
}