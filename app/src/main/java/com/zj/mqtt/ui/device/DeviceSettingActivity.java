package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.database.DeviceDao;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.view.InputNameDialog;
import com.zj.mqtt.view.MyItemView;
import com.zj.mqtt.view.PlaceDialog;

/**
 * 设备设置
 *
 * @author zhuj 2018/9/5 下午6:40
 */
public class DeviceSettingActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.item_name) MyItemView mItemName;
    @BindView(R.id.item_place) MyItemView mItemPlace;
    @BindView(R.id.item_info) MyItemView mItemInfo;

    private DeviceBean mDeviceBean;
    InputNameDialog mNameDialog;
    PlaceDialog mPlaceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        mDeviceBean = getIntent().getParcelableExtra(AppString.KEY_BEAN);
        mItemPlace.setTextContent(mDeviceBean.getPlace());
        mItemName.setTextContent(mDeviceBean.getName());
    }

    @OnClick(R.id.layout_header_right)
    public void onRightClick() {
        String name = mItemName.getTextContent();
        String place = mItemPlace.getTextContent();
        mDeviceBean.setName(name);
        mDeviceBean.setPlace(place);
        DeviceDao.saveOrUpdate(mDeviceBean);

        getApp().updateDevice(mDeviceBean.getDeviceMac(), name, place);
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.layout_header_back)
    public void onFinishClick() {
        finish();
    }

    @OnClick(R.id.btn_delete)
    public void onDelete() {
        DeviceDao.delete(mDeviceBean);
        getApp().removeDevice(mDeviceBean.getDeviceMac());
        Intent intent = new Intent();
        intent.putExtra(AppString.KEY_DELETE, true);
        setResult(RESULT_OK);
        finish();
    }

    @OnClick(R.id.item_name)
    public void onViewNameClicked() {
        if (mNameDialog == null) {
            mNameDialog = new InputNameDialog(this, getString(R.string.label_device_name),
                    mDeviceBean.getName());
            mNameDialog.setOnOkClickListener(new InputNameDialog.OnOkClickListener() {
                @Override
                public void clickCancel() {

                }

                @Override
                public void clickConfirm(String str) {
                    if (TextUtils.isEmpty(str)) {
                        showToast(R.string.toast_input_empty);
                        return;
                    }
                    mItemName.setTextContent(str);
                    mNameDialog.dismiss();
                }
            });
        } else {
            mNameDialog.setContent(mItemName.getTextContent());
        }
        mNameDialog.show();
    }

    @OnClick(R.id.item_place)
    public void onViewPlaceClicked() {
        if (mPlaceDialog == null) {
            mPlaceDialog = new PlaceDialog(this, mDeviceBean.getPlace());
            mPlaceDialog.setOnOkClickListener(new PlaceDialog.OnOkClickListener() {
                @Override
                public void clickConfirm(String str) {
                    mItemPlace.setTextContent(str);
                }
            });
        } else {
            mPlaceDialog.setContent(mItemPlace.getTextContent());
        }
        mPlaceDialog.show();
    }
}
