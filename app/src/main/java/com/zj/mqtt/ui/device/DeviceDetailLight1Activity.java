package com.zj.mqtt.ui.device;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
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
import com.zj.mqtt.constant.AppType;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.protocol.CmdParse;
import com.zj.mqtt.utils.StatusBarUtil;
import java.util.List;

/**
 * @author zhuj 2018/9/13 下午7:35.
 */
public class DeviceDetailLight1Activity extends DeviceDetailActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.tv_light1) TextView mTvLight1;
    @BindView(R.id.tv_onoff1) TextView mTvOnoff1;

    private DeviceEndpointBean mEndpointBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_detail_light1);
        ButterKnife.bind(this);

        StatusBarUtil.immersive(this);
        StatusBarUtil.setPadding(this, mHeaderView);
        initViews();
    }

    private void initViews() {
        initLight(mTvLight1, false);
        initLight(mTvOnoff1, false);

    }

    @Override
    void sendReadnodeCmd() {
        //读取数据
        List<DeviceEndpointBean> list = mDeviceBean.getEndpointList();
        DeviceEndpointBean endpointBean;
        for (int i = 0; i < list.size(); i++) {
            endpointBean = list.get(i);
            getApp().publishMsgToServer(
                    CmdPackage.getReadNode(mDeviceBean.getDeviceMac(), endpointBean.getEndpoint(),
                            AppType.CLUSTER_ONOFF, 0));
        }
    }

    @OnClick(R.id.tv_onoff1)
    public void onClickSwitch(View view) {
        if (!mDeviceBean.isOnline()) {
            showToast(R.string.label_unonline);
            return;
        }
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

            initLight(mTvOnoff1, isControlOnoff);
            initLight(mTvLight1, isControlOnoff);
        } else if (result.getCmd().equals(CmdString.DEV_READ_NODE)) {
            //读取属性
            CmdReadNodeResult nodeResult = (CmdReadNodeResult) result;

            //mac过滤
            if (!mDeviceBean.getDeviceMac().equals(nodeResult.getNodedata().getMac())) {
                return;
            }

            int clusterId = nodeResult.getNodedata().getClusterId();
            int data = nodeResult.getNodedata().getAttributeBuffer();
            if (AppType.CLUSTER_ONOFF == clusterId) {
                int endPoint = nodeResult.getNodedata().getEndpoint();
                boolean isOnoff = (data == 1);
                //是否一致 endpoint
                boolean islight = mDeviceBean.getEndpointList().get(0).getEndpoint() == endPoint;
                if (islight) {
                    initLight(mTvOnoff1, isOnoff);
                    initLight(mTvLight1, isOnoff);
                }
            }
        } else if (result.getCmd().equals(CmdParse.CMD_ZCL_ATTRIBUTE)) {
            CmdZclAttributeResult zclResult = (CmdZclAttributeResult) result;
            if (!mDeviceBean.getDeviceMac().equals(zclResult.getNode().getMac())) {
                return;
            }
            int clusterId = zclResult.getClusterId();
            int data = zclResult.getAttributeBuffer();
            if (AppType.CLUSTER_ONOFF == clusterId) {
                int endPoint = zclResult.getNode().getEndpoint();
                boolean isOnoff = (data == 1);

                boolean islight = mDeviceBean.getEndpointList().get(0).getEndpoint() == endPoint;
                if (islight) {
                    initLight(mTvOnoff1, isOnoff);
                    initLight(mTvLight1, isOnoff);
                }
            }
        }
    }

    private boolean isLight1(int endpoint) {
        if (mDeviceBean.getEndpointList().get(0).getEndpoint() == endpoint) {
            return true;
        }
        return false;
    }

    private void initLight(TextView tv, boolean isOnoff) {
        tv.setSelected(isOnoff);
        tv.setText(isOnoff ? R.string.label_click_off : R.string.label_click_on);
    }
}
