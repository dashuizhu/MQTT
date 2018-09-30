package com.zj.mqtt.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.TextureView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.person.commonlib.utils.ToastUtils;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.R;
import com.zj.mqtt.utils.sharedPresenter.SharedPreApp;
import com.zj.mqtt.view.ClearEditText;

public class MacInputActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.et) ClearEditText mEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mac_input);
        ButterKnife.bind(this);

        String  server = SharedPreApp.getInstance().getServerMac();
        mEt.setText(server);

    }

    @OnClick(R.id.layout_header_back)
    public void onBack() {
        finish();
    }

    @OnClick(R.id.layout_header_right)
    public void onSave() {

        String input = mEt.getText().toString().trim();
        if (input.length() != 16) {
            ToastUtils.showToast(this, "mac格式错误");
            return;
        }
        SharedPreApp.getInstance().put(this, SharedPreApp.KEY_SERVER_MAC, input);
        setResult(RESULT_OK);
        finish();
    }
}
