package com.zj.mqtt.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.Data;

/**
 * 场景
 *
 * @author zhuj 2018/8/27 下午2:27.
 */
@Data
public class ScenesBean implements Parcelable {

    private String id;
    private String name;
    private int seq;

    private List<ActionBean> actionList;

    public List<ActionBean> getActionList() {
        if (actionList == null) {
            actionList = new ArrayList<>();
        }
        return actionList;
    }

    public ScenesBean() {
        this.id = UUID.randomUUID().toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.seq);
        dest.writeTypedList(this.actionList);
    }

    protected ScenesBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.seq = in.readInt();
        this.actionList = in.createTypedArrayList(ActionBean.CREATOR);
    }

    public static final Creator<ScenesBean> CREATOR = new Creator<ScenesBean>() {
        @Override
        public ScenesBean createFromParcel(Parcel source) {
            return new ScenesBean(source);
        }

        @Override
        public ScenesBean[] newArray(int size) {
            return new ScenesBean[size];
        }
    };
}
