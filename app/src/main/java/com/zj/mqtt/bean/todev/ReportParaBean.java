package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;
import lombok.Data;

/**
 * 节点自动上报属性
 *
 * @author zhuj 2018/8/30 下午2:44.
 */
@Data
public class ReportParaBean implements Parcelable {

    /**
     * clusterId : 1029
     * attributeId : 0
     * dataType : 37
     * minReportTime : 1
     * maxReportTime : 1800
     * reportableChange : 500
     */

    private int clusterId;
    private int attributeId;
    private int dataType;
    private int minReportTime;
    private int maxReportTime;
    private int reportableChange;

    public ReportParaBean() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.clusterId);
        dest.writeInt(this.attributeId);
        dest.writeInt(this.dataType);
        dest.writeInt(this.minReportTime);
        dest.writeInt(this.maxReportTime);
        dest.writeInt(this.reportableChange);
    }

    protected ReportParaBean(Parcel in) {
        this.clusterId = in.readInt();
        this.attributeId = in.readInt();
        this.dataType = in.readInt();
        this.minReportTime = in.readInt();
        this.maxReportTime = in.readInt();
        this.reportableChange = in.readInt();
    }

    public static final Parcelable.Creator<ReportParaBean> CREATOR =
            new Parcelable.Creator<ReportParaBean>() {
                @Override
                public ReportParaBean createFromParcel(Parcel source) {
                    return new ReportParaBean(source);
                }

                @Override
                public ReportParaBean[] newArray(int size) {
                    return new ReportParaBean[size];
                }
            };
}
