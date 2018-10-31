package com.zj.mqtt.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.bean.todev.CmdControlBean;
import com.zj.mqtt.bean.todev.HuaSetBean;
import com.zj.mqtt.bean.todev.LevelBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.CmdString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.utils.RadioGroupUtils;

/**
 * 设备控制详情
 *
 * @author zhuj 2018/8/28 下午4:01
 */
public class DeviceControlActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.rb_switch) RadioButton mRbSwitch;
    @BindView(R.id.cb_switch) CheckBox mCbSwitch;
    @BindView(R.id.rb_color) RadioButton mRbColor;
    @BindView(R.id.layout_switch) RelativeLayout mLayoutSwitch;
    @BindView(R.id.tv_color_hua) TextView mTvColorHua;
    @BindView(R.id.sb_color_hua) SeekBar mSbColorHua;
    @BindView(R.id.tv_color_set) TextView mTvColorSet;
    @BindView(R.id.sb_color_set) SeekBar mSbColorSet;
    @BindView(R.id.tv_color_time) TextView mTvColorTime;
    @BindView(R.id.sb_color_time) SeekBar mSbColorTime;
    @BindView(R.id.layout_color) LinearLayout mLayoutColor;
    @BindView(R.id.rb_level) RadioButton mRbLevel;
    //@BindView(R.id.tv_level_value) TextView mTvLevelValue;
    @BindView(R.id.sb_level_value) SeekBar mSbLevelValue;
    @BindView(R.id.tv_level_time) TextView mTvLevelTime;
    @BindView(R.id.sb_level_time) SeekBar mSbLevelTime;
    @BindView(R.id.layout_level) LinearLayout mLayoutLevel;
    @BindView(R.id.rb_group) RadioGroup mRbGroup;
    @BindView(R.id.rb_switch2) RadioButton mRbSwitch2;
    @BindView(R.id.cb_switch2) CheckBox mCbSwitch2;
    @BindView(R.id.layout_switch2) RelativeLayout mLayoutSwitch2;
    @BindView(R.id.rb_switch3) RadioButton mRbSwitch3;
    @BindView(R.id.cb_switch3) CheckBox mCbSwitch3;
    @BindView(R.id.layout_switch3) RelativeLayout mLayoutSwitch3;
    private DeviceBean mDeviceBean;
    private CmdControlBean mControlBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control_detail);
        ButterKnife.bind(this);
        initViews();
        registerRxBus();
    }

    private void initViews() {

        new RadioGroupUtils(mRbGroup).supportNest();
        mControlBean = getIntent().getParcelableExtra(AppString.KEY_BEAN);
        mDeviceBean = getApp().getDevice(mControlBean.getDeviceMac());
        if (mDeviceBean == null) {
            showToast("未找到设备");
            finish();
            return;
        }
        mHeaderView.setTitle(mDeviceBean.getName());

        mRbGroup.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {

            }

            @Override
            public void onChildViewRemoved(View parent, View child) {

            }
        });


        if (mDeviceBean.isDeviceSwith()) {
            mRbSwitch.setVisibility(View.VISIBLE);
            mLayoutSwitch.setVisibility(View.VISIBLE);
            mRbSwitch.setChecked(true);
        } else if (mDeviceBean.isDeviceLight1()) {
            mRbSwitch.setVisibility(View.VISIBLE);
            mLayoutSwitch.setVisibility(View.VISIBLE);
            mRbSwitch.setChecked(true);
        } else if (mDeviceBean.isDeviceLight2()) {
            mRbSwitch.setVisibility(View.VISIBLE);
            mRbSwitch2.setVisibility(View.VISIBLE);
            mLayoutSwitch.setVisibility(View.VISIBLE);
            mLayoutSwitch2.setVisibility(View.VISIBLE);
        } else if (mDeviceBean.isDeviceLight3()) {
            mRbSwitch.setVisibility(View.VISIBLE);
            mRbSwitch2.setVisibility(View.VISIBLE);
            mRbSwitch3.setVisibility(View.VISIBLE);
            mLayoutSwitch.setVisibility(View.VISIBLE);
            mLayoutSwitch2.setVisibility(View.VISIBLE);
            mLayoutSwitch3.setVisibility(View.VISIBLE);
        } else if (mDeviceBean.isDeviceDim()) {
            mRbSwitch.setVisibility(View.VISIBLE);
            mRbColor.setVisibility(View.VISIBLE);
            mLayoutSwitch.setVisibility(View.VISIBLE);
            mLayoutColor.setVisibility(View.VISIBLE);
            initSeekBarListener();
        }

        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(mControlBean.getCmd())) {
            mControlBean.setCmd(CmdString.DEV_ONOFF);
        }
        switch (mControlBean.getCmd()) {
            case CmdString.DEV_ONOFF:
                initDataSwitch();
                break;
            case CmdString.DEV_COLOR_CONTROL:
                mRbColor.setChecked(true);
                mSbColorSet.setProgress(mControlBean.getMovetohueandsat().getSat());
                mSbColorHua.setProgress(mControlBean.getMovetohueandsat().getHua());
                mSbColorTime.setProgress(mControlBean.getMovetohueandsat().getTime());
                break;
            case CmdString.DEV_LEVEL_CONTROL:
                mRbLevel.setChecked(true);
                mSbLevelTime.setProgress(mControlBean.getMv_to_level().getTime());
                mSbLevelValue.setProgress(mControlBean.getMv_to_level().getLevel());
                break;
            default:
        }
    }

    private void initDataSwitch() {
        if (mControlBean.getNode() == null) {
            mRbSwitch.setChecked(true);
            return;
        }
        int controlEndpoint = mControlBean.getNode().getEndpoint();
        boolean isOnoff = mControlBean.getNode().getValue() == 1;
        if (mDeviceBean.isDeviceLight2()) {
            //是第一个endpoint
            if (controlEndpoint == mDeviceBean.getEndpointList().get(0).getEndpoint()) {
                mRbSwitch.setChecked(true);
                mCbSwitch.setChecked(isOnoff);
            } else {
                mRbSwitch2.setChecked(true);
                mCbSwitch2.setChecked(isOnoff);
            }
        } else if (mDeviceBean.isDeviceLight3()) {
            //3路开关设备
            if (controlEndpoint == mDeviceBean.getEndpointList().get(0).getEndpoint()) {
                //是第一个endpoint
                mRbSwitch.setChecked(true);
                mCbSwitch.setChecked(isOnoff);
            } else if (controlEndpoint == mDeviceBean.getEndpointList().get(1).getEndpoint()) {
                //是第一个endpoint
                mRbSwitch2.setChecked(true);
                mCbSwitch2.setChecked(isOnoff);
            } else {
                mRbSwitch3.setChecked(true);
                mCbSwitch3.setChecked(isOnoff);
            }
        } else {
            //其他情况
            mRbSwitch.setChecked(true);
            mCbSwitch.setChecked(isOnoff);
        }
    }

    private void initSeekBarListener() {
        mSbColorHua.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvColorHua.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbColorSet.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvColorSet.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSbColorTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvColorTime.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //mSbLevelValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
        //    @Override
        //    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //        mTvLevelValue.setText(String.valueOf(progress));
        //    }
        //
        //    @Override
        //    public void onStartTrackingTouch(SeekBar seekBar) {
        //
        //    }
        //
        //    @Override
        //    public void onStopTrackingTouch(SeekBar seekBar) {
        //
        //    }
        //});

        mSbLevelTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvLevelTime.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.layout_header_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.layout_header_right)
    public void onRightClick() {
        switch (mRbGroup.getCheckedRadioButtonId()) {
            case R.id.rb_switch:
                mControlBean =
                        CmdPackage.setOnOff(mCbSwitch.isChecked(), mDeviceBean.getDeviceMac(),
                                mDeviceBean.getEndpointList().get(0).getEndpoint());
                break;
            case R.id.rb_switch2:
                mControlBean =
                        CmdPackage.setOnOff(mCbSwitch2.isChecked(), mDeviceBean.getDeviceMac(),
                                mDeviceBean.getEndpointList().get(1).getEndpoint());
                break;
            case R.id.rb_switch3:
                mControlBean =
                        CmdPackage.setOnOff(mCbSwitch3.isChecked(), mDeviceBean.getDeviceMac(),
                                mDeviceBean.getEndpointList().get(2).getEndpoint());
                break;

            case R.id.rb_color:
                HuaSetBean huaSetBean = new HuaSetBean();
                huaSetBean.setTime(mSbColorHua.getProgress());
                huaSetBean.setSat(mSbColorSet.getProgress());
                huaSetBean.setHua(mSbColorTime.getProgress());

                mControlBean = CmdPackage.setColorControl(mDeviceBean.getDeviceMac(),
                        mDeviceBean.getEndpointList().get(0).getEndpoint(), huaSetBean);
                break;
            case R.id.rb_level:
                LevelBean levelBean = new LevelBean();
                levelBean.setLevel(mSbLevelValue.getProgress());
                levelBean.setTime(mSbLevelTime.getProgress());
                mControlBean = CmdPackage.setLevelControl(mDeviceBean.getDeviceMac(),
                        mDeviceBean.getEndpointList().get(0).getEndpoint(), levelBean);
                break;
            default:
        }
        mControlBean.setDeviceMac(mDeviceBean.getDeviceMac());
        getIntent().putExtra(AppString.KEY_BEAN, mControlBean);
        setResult(RESULT_OK, getIntent());
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
