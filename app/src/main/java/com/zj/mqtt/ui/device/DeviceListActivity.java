package com.zj.mqtt.ui.device;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.person.commonlib.utils.DensityUtil;
import com.person.commonlib.view.HeaderView;
import com.person.commonlib.view.RecyclerViewSpaceItemDecoration;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.DeviceListAdapter;
import com.zj.mqtt.bean.device.DeviceBean;
import com.zj.mqtt.database.DeviceDao;
import com.zj.mqtt.ui.BaseActivity;
import com.zj.mqtt.utils.ActivityUtils;

/**
 * @author zhuj 2018/8/27 下午3:51.
 */
public class DeviceListActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private DeviceListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

    //private boolean mRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        ButterKnife.bind(this);
        //master chagne 6
        initViews();
        //flow2 change 5
        //flow2 change 5 +++
        //mast1 chagne 5 change++
        //registerRxBus();
    }

    @OnClick(R.id.layout_header_back)
    public void onClickBack() {
        finish();
    }

    @OnClick(R.id.layout_header_right)
    public void onClickEdit() {
        DeviceDao.updateSeq(mAdapter.getData());
        showToast(R.string.toast_save_success);
    }

    @OnClick(R.id.btn_add)
    public void onAdd() {
        Intent intent = new Intent(this, DeviceAddOneActivity.class);
        startActivity(intent);
    }

    /**
     * Rxbus， post只有TAG ，没有传递value时， 不能使用注释段tags 过滤
     * 使用注意， 这里接收，不能使用boolean 接收， 要使用Boolean
     * 类似， int类型，接收要用Integer，
     * 所以要做null判断
     */
    //@Subscribe(thread = EventThread.MAIN_THREAD
    //        //, tags = @Tag(AppConstants.RXBUS_KID_SELECT)
    //)
    //public void onKidChange(String action) {
    //    //if (action.equals(RxBusString.RXBUS_SCENES)) {
    //    //    setResult(RESULT_OK);
    //    //}
    //}
    @Override
    protected void onStart() {
        //if (mRefresh) {
        mAdapter.setNewData(getApp().getDevcieList());
        //    mRefresh = false;
        //}
        super.onStart();
    }

    private void initViews() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        int padding = DensityUtil.dip2px(this, 16);
        mRecyclerView.addItemDecoration(new RecyclerViewSpaceItemDecoration(padding, 3, false));

        OnItemDragListener listener = new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag start");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                //                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from,
                    RecyclerView.ViewHolder target, int to) {
                Log.d(TAG, "move from: "
                        + source.getAdapterPosition()
                        + " to: "
                        + target.getAdapterPosition());
            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "drag end");
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                //                holder.setTextColor(R.id.tv, Color.BLACK);
            }
        };
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(20);
        paint.setColor(Color.WHITE);
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "view swiped start: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                //                holder.setTextColor(R.id.tv, Color.WHITE);
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "View reset: " + pos);
                BaseViewHolder holder = ((BaseViewHolder) viewHolder);
                //                holder.setTextColor(R.id.tv, Color.BLACK);
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                Log.d(TAG, "View Swiped: " + pos);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder,
                    float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(DeviceListActivity.this, R.color.red));
                canvas.drawText("滑动删除", 40, 40, paint);
            }
        };

        mAdapter = new DeviceListAdapter(((AppApplication) getApplication()).getDevcieList());
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        //mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);
        //mRecyclerView.addItemDecoration(new GridItemDecoration(this ,R.drawable.list_divider));

        mRecyclerView.setAdapter(mAdapter);

        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                DeviceBean bean = mAdapter.getData().get(position);
                ActivityUtils.startDeviceDetail(DeviceListActivity.this, bean);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
    }
}
