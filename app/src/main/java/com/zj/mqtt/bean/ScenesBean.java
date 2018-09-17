package com.zj.mqtt.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import com.zj.mqtt.R;
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

    public static final int SCENES_HOME = 1;
    public static final int SCENES_READ = 2;
    public static final int SCENES_SLEEP = 3;
    public static final int SCENES_OUT = 4;


    private String id;
    private String name;
    private int seq;
    private int picture;

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

    public int getPictureRes() {
        @DrawableRes int resId;
        switch (picture) {
            case SCENES_HOME:
                resId = R.mipmap.btn_scenes_home_normal;
                break;
            case SCENES_READ:
                resId = R.mipmap.btn_scenes_read_normal;
                break;
            case SCENES_SLEEP:
                resId = R.mipmap.btn_scenes_sleep_normal;
                break;
            case SCENES_OUT:
                resId = R.mipmap.btn_scenes_out_normal;
                break;
            default:
                resId = R.mipmap.btn_scenes_custom_press;
        }
        return resId;
    }

    public int getPictureResByList() {
        @DrawableRes int resId;
        switch (picture) {
            case SCENES_HOME:
                resId = R.mipmap.btn_scenes_home_press;
                break;
            case SCENES_READ:
                resId = R.mipmap.btn_scenes_read_press;
                break;
            case SCENES_SLEEP:
                resId = R.mipmap.btn_scenes_sleep_press;
                break;
            case SCENES_OUT:
                resId = R.mipmap.btn_scenes_out_press;
                break;
            default:
                resId = R.mipmap.btn_scenes_custom_press;
        }
        return resId;
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
        dest.writeInt(this.picture);
        dest.writeTypedList(this.actionList);
    }

    protected ScenesBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.seq = in.readInt();
        this.picture = in.readInt();
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
