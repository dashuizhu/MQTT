package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zj.mqtt.R;
import com.zj.mqtt.ui.BaseActivity;

public class DeviceAddOneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devcie_add_one);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.layout_header_back)
    public void onViewNextClicked() {
        finish();
    }

    @OnClick(R.id.btn_next)
    public void onViewBackClicked() {
        Intent intent = new Intent(this, DeviceNameActivity.class);
        startActivity(intent);
        finish();
    }
}
