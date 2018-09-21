package com.zj.mqtt.bean.toapp;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

@Data
public class NodedataBean implements Parcelable {
    /**
     * mac : string
     * endpoint : 1
     * clusterId : 1
     * attributeId : 1
     * dataType : 1
     * data : 1
     */

    private String mac;
    private int endpoint;
    private int clusterId;
    private int attributeId;
    private int attributeDataType;
    private int attributeBuffer;

    public NodedataBean() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mac);
        dest.writeInt(this.endpoint);
        dest.writeInt(this.clusterId);
        dest.writeInt(this.attributeId);
        dest.writeInt(this.attributeDataType);
        dest.writeInt(this.attributeBuffer);
    }

    protected NodedataBean(Parcel in) {
        this.mac = in.readString();
        this.endpoint = in.readInt();
        this.clusterId = in.readInt();
        this.attributeId = in.readInt();
        this.attributeDataType = in.readInt();
        this.attributeBuffer = in.readInt();
    }

    public static final Creator<NodedataBean> CREATOR = new Creator<NodedataBean>() {
        @Override
        public NodedataBean createFromParcel(Parcel source) {
            return new NodedataBean(source);
        }

        @Override
        public NodedataBean[] newArray(int size) {
            return new NodedataBean[size];
        }
    };
}