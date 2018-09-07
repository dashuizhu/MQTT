package com.zj.mqtt.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.zj.mqtt.R;
import com.zj.mqtt.utils.AppUtils;

/**
 * 输入框
 *
 * @author zhuj 2018/9/5 下午7:39
 */
public class InputNameDialog extends Dialog {

    @BindView(R.id.tv_title) TextView mTvTipsTitle;
    @BindView(R.id.tv_confirm) TextView mTvTipsConfirm;
    @BindView(R.id.tv_cancel) TextView mTvTipsCancel;
    @BindView(R.id.et_name) EditText mEtName;
    private String mTipsTitle;
    private Context mContext;
    private String mContentText;

    public InputNameDialog(Context context, String tipsTitle) {
        super(context, R.style.dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTipsTitle = tipsTitle;
        this.mContext = context;
    }

    public InputNameDialog(Context context, String tipsTitle, String contentText) {
        super(context, R.style.dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTipsTitle = tipsTitle;
        this.mContext = context;
        this.mContentText = contentText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_input);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        mTvTipsTitle.setText(mTipsTitle);
        if (!TextUtils.isEmpty(mContentText)) {
            mEtName.setText(mContentText);
            AppUtils.initSelecton(mEtName);
        }
    }

    @OnClick({ R.id.tv_cancel, R.id.tv_confirm })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                mOnOkClickListener.clickCancel();
                //}
                dismiss();
                break;
            case R.id.tv_confirm:
                String str = mEtName.getText().toString();
                mOnOkClickListener.clickConfirm(str);
                break;
            default:
        }
    }

    public void setContent(String content) {
        mContentText = content;
        if (mEtName != null) {
            mEtName.setText(mContentText);
            AppUtils.initSelecton(mEtName);
        }
    }

    public interface OnOkClickListener {
        void clickCancel();

        void clickConfirm(String str);
    }

    private OnOkClickListener mOnOkClickListener;

    public void setOnOkClickListener(OnOkClickListener listener) {
        mOnOkClickListener = listener;
    }
}