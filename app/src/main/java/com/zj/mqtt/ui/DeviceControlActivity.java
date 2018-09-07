package com.zj.mqtt.ui;

import android.os.Bundle;
import android.text.TextUtils;
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
    @BindView(R.id.tv_level_value) TextView mTvLevelValue;
    @BindView(R.id.sb_level_value) SeekBar mSbLevelValue;
    @BindView(R.id.tv_level_time) TextView mTvLevelTime;
    @BindView(R.id.sb_level_time) SeekBar mSbLevelTime;
    @BindView(R.id.layout_level) LinearLayout mLayoutLevel;
    @BindView(R.id.rb_group) RadioGroup mRbGroup;
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
        mControlBean = getIntent().getParcelableExtra(AppString.KEY_BEAN);
        mDeviceBean = getApp().getDevice(mControlBean.getDeviceMac());
        if (mDeviceBean == null) {
            showToast("未找到设备");
            finish();
            return;
        }
        mHeaderView.setTitle(mDeviceBean.getName());

        initSeekBarListener();
        initData();
    }

    private void initData() {
        if (TextUtils.isEmpty(mControlBean.getCmd())) {
            mControlBean.setCmd(CmdString.DEV_ONOFF);
        }
        switch (mControlBean.getCmd()) {
            case CmdString.DEV_ONOFF:
                mRbSwitch.setChecked(true);
                if (mControlBean.getNode() != null) {
                    mCbSwitch.setChecked(mControlBean.getNode().getValue() == 1);
                }
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

        mSbLevelValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvLevelValue.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                mControlBean = CmdPackage.setOnOff(mCbSwitch.isChecked(),
                        mDeviceBean.getDeviceMac(),
                        mDeviceBean.getDeviceEndpoint().getEndpoint());
                break;
            case R.id.rb_color:
                HuaSetBean huaSetBean = new HuaSetBean();
                huaSetBean.setTime(mSbColorHua.getProgress());
                huaSetBean.setSat(mSbColorSet.getProgress());
                huaSetBean.setHua(mSbColorTime.getProgress());

                mControlBean = CmdPackage.setColorControl(mDeviceBean.getDeviceMac(),
                        mDeviceBean.getDeviceEndpoint().getEndpoint(), huaSetBean);
                break;
            case R.id.rb_level:
                LevelBean levelBean = new LevelBean();
                levelBean.setLevel(mSbLevelValue.getProgress());
                levelBean.setTime(mSbLevelTime.getProgress());
                mControlBean = CmdPackage.setLevelControl(mDeviceBean.getDeviceMac(), mDeviceBean.getDeviceEndpoint().getEndpoint(), levelBean);
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
