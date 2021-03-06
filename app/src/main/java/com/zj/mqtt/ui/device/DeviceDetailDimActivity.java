package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceEndpointBean;
import com.zj.mqtt.bean.toapp.CmdReadNodeResult;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.bean.toapp.CmdZclAttributeResult;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.bean.todev.HuaSetBean;
import com.zj.mqtt.constant.AppType;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.protocol.CmdParse;
import com.zj.mqtt.utils.StatusBarUtil;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * 调光类数据
 *
 * @author zhuj 2018/9/13 下午7:35.
 */
public class DeviceDetailDimActivity extends DeviceDetailActivity
        implements SeekBar.OnSeekBarChangeListener {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.tv_switch) TextView mTvSwitch;
    @BindView(R.id.tv_color_set) TextView mTvColorSet;
    @BindView(R.id.sb_color_set) SeekBar mSbColorSet;
    @BindView(R.id.tv_color_hua) TextView mTvColorHua;
    @BindView(R.id.sb_color_hua) SeekBar mSbColorHua;
    @BindView(R.id.tv_color_time) TextView mTvColorTime;
    @BindView(R.id.sb_color_time) SeekBar mSbColorTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail_dim);
        ButterKnife.bind(this);

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPadding(this, mHeaderView);
        initViews();
    }

    private void initViews() {
        initLight(mTvSwitch, false);
        mSbColorHua.setOnSeekBarChangeListener(this);
        mSbColorSet.setOnSeekBarChangeListener(this);
        mSbColorTime.setOnSeekBarChangeListener(this);
    }

    @Override
    void sendReadnodeCmd() {

        //读取数据
        List<DeviceEndpointBean> list = mDeviceBean.getEndpointList();

        try {

            DeviceEndpointBean deviceEndpointBean = list.get(0);
            Observable.just(deviceEndpointBean).observeOn(Schedulers.io()).subscribe(new Consumer<DeviceEndpointBean>() {
                @Override
                public void accept(DeviceEndpointBean endpointBean) throws Exception {
                    getApp().publishMsgToServer(
                            CmdPackage.getReadNode(mDeviceBean.getDeviceMac(), endpointBean.getEndpoint(),
                                    AppType.CLUSTER_ONOFF, 0));
                    Thread.sleep(200);
                    //hue
                    getApp().publishMsgToServer(
                            CmdPackage.getReadNode(mDeviceBean.getDeviceMac(), endpointBean.getEndpoint(),
                                    AppType.CLUSTER_COLOR_CONTROL, 0));

                    Thread.sleep(200);
                    //sat
                    getApp().publishMsgToServer(
                            CmdPackage.getReadNode(mDeviceBean.getDeviceMac(), endpointBean.getEndpoint(),
                                    AppType.CLUSTER_COLOR_CONTROL, 1));
                    Thread.sleep(200);
                    //time
                    getApp().publishMsgToServer(
                            CmdPackage.getReadNode(mDeviceBean.getDeviceMac(), endpointBean.getEndpoint(),
                                    AppType.CLUSTER_COLOR_CONTROL, 2));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
    }

}

    @OnClick(R.id.tv_switch)
    public void onClickSwitch(View view) {
        DeviceEndpointBean endpointBean = mDeviceBean.getEndpointList().get(0);
        if (endpointBean == null) {
            return;
        }
        //通过 isselect 表明当前是否已经被控
        CmdControlBean control = CmdPackage.setOnOff(!view.isSelected(), endpointBean.getMac(),
                endpointBean.getEndpoint());
        getApp().publishMsgToServer(control);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = { @Tag, @Tag(RxBusString.RXBUS_PARSE) })
    public void onKidChange(CmdResult result) {
        if (result.getCmd().equals(CmdString.DEV_ONOFF)) {
            //控制类型的开关
            //拿到之前控制的内容
            CmdControlBean control = getApp().getControlCacheBean(result.getSeq());
            if (control == null) {
                return;
            }
            //控制的endpoint
            int controlEndpoint = control.getNode().getEndpoint();
            boolean isControlOnoff = control.getNode().getValue() == 1;

            initLight(mTvSwitch, isControlOnoff);
        } else if (result.getCmd().equals(CmdString.DEV_READ_NODE)) {
            //读取属性
            //过滤设备mac
            CmdReadNodeResult nodeResult = (CmdReadNodeResult) result;
            if (!nodeResult.getNodedata().getMac().equals(mDeviceBean.getDeviceMac())) {
                return;
            }
            int clusterId = nodeResult.getNodedata().getClusterId();
            int data = nodeResult.getNodedata().getAttributeBuffer();

            switch (clusterId) {
                case AppType.CLUSTER_ONOFF:
                    boolean isOnoff = (data == 1);
                    initLight(mTvSwitch, isOnoff);
                    break;
                case AppType.CLUSTER_COLOR_CONTROL:
                    int attributeId = nodeResult.getNodedata().getAttributeId();
                    if (attributeId == 0) {
                        mSbColorHua.setProgress(data);
                        mTvColorHua.setText("" + data);
                    } else if (attributeId == 1) {
                        mSbColorSet.setProgress(data);
                        mTvColorSet.setText("" + data);
                    } else if (attributeId == 2) {
                        mSbColorTime.setProgress(data);
                        mTvColorTime.setText("" + data);
                    }
                    break;
                default:
            }
        } else if (result.getCmd().equals(CmdParse.CMD_ZCL_ATTRIBUTE)) {
            CmdZclAttributeResult zclResult = (CmdZclAttributeResult) result;
            if (!mDeviceBean.getDeviceMac().equals(zclResult.getNode().getMac())) {
                return;
            }
            int clusterId = zclResult.getClusterId();
            int data = zclResult.getAttributeBuffer();
            if (AppType.CLUSTER_ONOFF == clusterId) {
                boolean isOnoff = (data == 1);
                initLight(mTvSwitch, isOnoff);
            } else if (AppType.CLUSTER_COLOR_CONTROL == clusterId) {
                int attributeId = zclResult.getAttributeId();
                if (attributeId == 0) {
                    mSbColorHua.setProgress(data);
                    mTvColorHua.setText("" + data);
                } else if (attributeId == 1) {
                    mSbColorSet.setProgress(data);
                    mTvColorSet.setText("" + data);
                } else if (attributeId == 2) {
                    mSbColorTime.setProgress(data);
                    mTvColorTime.setText("" + data);
                }
            }
        }
    }

    private void initLight(TextView tv, boolean isOnoff) {
        tv.setSelected(isOnoff);
        tv.setText(isOnoff ? R.string.label_click_off : R.string.label_click_on);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            DeviceEndpointBean endpointBean = mDeviceBean.getEndpointList().get(0);
            if (endpointBean == null) {
                return;
            }
            HuaSetBean bean = new HuaSetBean();
            bean.setTime(mSbColorTime.getProgress());
            bean.setHua(mSbColorHua.getProgress());
            bean.setSat(mSbColorSet.getProgress());

            CmdControlBean control =
                    CmdPackage.setColorControl(endpointBean.getMac(), endpointBean.getEndpoint(),
                            bean);
            getApp().publishMsgToServer(control);
        }
        if (seekBar == mSbColorHua) {
            mTvColorHua.setText("" + progress);
        } else if (seekBar == mSbColorSet) {
            mTvColorSet.setText("" + progress);
        } else if (seekBar == mSbColorTime) {
            mTvColorTime.setText("" + progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
