package com.zj.mqtt.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DevicePlaceAdapter;
import com.zj.mqtt.constant.AppConstants;

/**
 * 输入框
 *
 * @author zhuj 2018/9/5 下午7:39
 */
public class PlaceDialog extends Dialog {

    private RecyclerView mRecyclerView;

    private DevicePlaceAdapter mAdapter;
    private String mNowPlace;

    public PlaceDialog(Context context, String place) {
        super(context, R.style.dialog);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mNowPlace = place;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_place);
        ButterKnife.bind(this);

        initView();
        mAdapter.initSelect(mNowPlace);
    }

    private void initView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mAdapter = new DevicePlaceAdapter();

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setNewData(AppConstants.getDevicePlaceList());
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mAdapter.setSelectPosition(position);
            }
        });
    }

    @OnClick(R.id.tv_confirm)
    public void onViewClicked(View view) {
        int pos = mAdapter.getSelectPosition();
        String str = mAdapter.getData().get(pos);
        mOnOkClickListener.clickConfirm(str);
        dismiss();
    }

    public void setContent(String content) {
        mNowPlace = content;
        if (mAdapter != null) {
            mAdapter.initSelect(mNowPlace);
        }
    }

    public interface OnOkClickListener {

        void clickConfirm(String str);
    }

    private OnOkClickListener mOnOkClickListener;

    public void setOnOkClickListener(OnOkClickListener listener) {
        mOnOkClickListener = listener;
    }
}