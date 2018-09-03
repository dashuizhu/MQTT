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
    private int dataType;
    private int data;

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
        dest.writeInt(this.dataType);
        dest.writeInt(this.data);
    }

    public NodedataBean() {
    }

    protected NodedataBean(Parcel in) {
        this.mac = in.readString();
        this.endpoint = in.readInt();
        this.clusterId = in.readInt();
        this.attributeId = in.readInt();
        this.dataType = in.readInt();
        this.data = in.readInt();
    }

    public static final Parcelable.Creator<NodedataBean> CREATOR =
            new Parcelable.Creator<NodedataBean>() {
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