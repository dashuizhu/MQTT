package com.zj.mqtt.bean.todev;

import android.os.Parcel;
import android.os.Parcelable;
import com.zj.mqtt.constant.AppConstants;
import lombok.Data;

/**
 * @author zhuj 2018/8/30 下午1:50.
 */
@Data
public class OtaBean implements Parcelable {

    private String url = AppConstants.OTA_URL;

    public OtaBean() {}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
    }

    protected OtaBean(Parcel in) {
        this.url = in.readString();
    }

    public static final Parcelable.Creator<OtaBean> CREATOR = new Parcelable.Creator<OtaBean>() {
        @Override
        public OtaBean createFromParcel(Parcel source) {
            return new OtaBean(source);
        }

        @Override
        public OtaBean[] newArray(int size) {
            return new OtaBean[size];
        }
    };
}
