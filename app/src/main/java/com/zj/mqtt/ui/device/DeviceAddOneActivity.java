package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.zj.mqtt.R;
import com.zj.mqtt.bean.toapp.CmdNodeResult;
import com.zj.mqtt.bean.toapp.CmdResult;
import com.zj.mqtt.constant.AppConstants;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.protocol.CmdPackage;
import com.zj.mqtt.protocol.CmdParse;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.view.RoundProgressView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.concurrent.TimeUnit;

/**
 * @author zhuj 2018/9/9 下午5:33
 */
public class DeviceAddOneActivity extends BaseActivity {

    @BindView(R.id.btn_next) Button mBtnAdd;
    @BindView(R.id.roundProgressView) RoundProgressView mProgressView;
    @BindView(R.id.tv_device) TextView mTvDevice;
    @BindView(R.id.tv_progress) TextView mTvProgress;

    private String mMac;
    private Disposable mDisposable;

    private final static int MAX_PROGRESS = AppConstants.DEVICE_ADD_TIME_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devcie_add_one);
        ButterKnife.bind(this);
        registerRxBus();
        mProgressView.setMaxProgress(MAX_PROGRESS);
        startAdd();
    }

    @OnClick(R.id.layout_header_back)
    public void onViewNextClicked() {
        finish();
    }

    @OnClick(R.id.btn_next)
    public void onViewBackClicked() {
        startAdd();
    }

    @Override
    protected void onDestroy() {
        unRegisterRxBus();
        disposale();
        super.onDestroy();
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = @Tag(RxBusString.RXBUS_PARSE))
    public void onReceive(CmdResult cmdResult) {
        if (cmdResult.getCmd().equals(CmdParse.CMD_DEVICE_JOIN)) {
            // TODO: 2018/9/10 这里就要加入数据库了
            CmdNodeResult nodeResult = (CmdNodeResult) cmdResult;
            mMac = nodeResult.getDevice().getDeviceEndpoint().getMac();

            getApp().publishMsgToServer(CmdPackage.stopDevice());

            Intent intent = new Intent(this, DeviceNameActivity.class);
            intent.putExtra(AppString.KEY_MAC, mMac);
            startActivity(intent);
            finish();
        }
    }

    private void startAdd() {
        mProgressView.setVisibility(View.VISIBLE);
        mTvProgress.setVisibility(View.VISIBLE);
        mTvDevice.setText("");
        mProgressView.setProgress(0);
        mDisposable = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mProgressView.setProgress(aLong.intValue());

                        if (aLong >= MAX_PROGRESS) {
                            mProgressView.setVisibility(View.INVISIBLE);
                            mTvProgress.setVisibility(View.INVISIBLE);
                            mTvDevice.setText(R.string.label_device_add_fail);
                            disposale();
                        }
                    }
                });
        mBtnAdd.setVisibility(View.GONE);
        getApp().publishMsgToServer(CmdPackage.addDevice());
    }

    private void disposale() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
            mDisposable = null;
        }
        mBtnAdd.setVisibility(View.VISIBLE);
    }
}
