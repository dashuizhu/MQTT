package com.zj.mqtt.ui;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.person.commonlib.view.HeaderView;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.R;
import com.zj.mqtt.adapter.ScenesListAdapter;
import com.zj.mqtt.constant.AppString;
import com.zj.mqtt.constant.RxBusString;
import com.zj.mqtt.database.ScenesDao;

/**
 * @author zhuj 2018/8/27 下午3:51.
 */
public class ScenesListActivity extends BaseActivity {

    @BindView(R.id.headerView) HeaderView mHeaderView;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    private ScenesListAdapter mAdapter;
    private ItemTouchHelper mItemTouchHelper;
    private ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

    private boolean mRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenes_list);
        ButterKnife.bind(this);
        initViews();
        registerRxBus();
    }

    @OnClick(R.id.layout_header_right) public void onClickEdit() {
        boolean isEdit = !mAdapter.isEdit();
        mAdapter.setEdit(isEdit);

        if (isEdit) {
            mHeaderView.setRightText(R.string.label_save);
            mAdapter.enableSwipeItem();
            mAdapter.enableDragItem(mItemTouchHelper);
        } else {
            mHeaderView.setRightText(R.string.label_edit);
            //保存
            ScenesDao.saveOrUpdate(mAdapter.getData());
            mAdapter.disableDragItem();
            mAdapter.disableSwipeItem();
        }
    }

    /**
     * Rxbus， post只有TAG ，没有传递value时， 不能使用注释段tags 过滤
     * 使用注意， 这里接收，不能使用boolean 接收， 要使用Boolean
     * 类似， int类型，接收要用Integer，
     * 所以要做null判断
     */
    @Subscribe(thread = EventThread.MAIN_THREAD
            //, tags = @Tag(AppConstants.RXBUS_KID_SELECT)
    )
    public void onKidChange(String action) {
        if (action.equals(RxBusString.RXBUS_SCENES)) {
            mRefresh = true;
        }
    }

    @Override
    protected void onStart() {
        if (mRefresh) {
            mAdapter.setNewData(getApp().getScenesList());
            mRefresh = false;
        }
        super.onStart();
    }

    private void initViews() {
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

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
        paint.setColor(Color.BLACK);
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
                canvas.drawColor(
                        ContextCompat.getColor(ScenesListActivity.this, R.color.colorPrimary));
                canvas.drawText("Just some text", 0, 40, paint);
            }
        };

        mAdapter = new ScenesListAdapter(((AppApplication) getApplication()).getScenesList());
        mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        //mItemDragAndSwipeCallback.setDragMoveFlags(ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN);
        mItemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);
        //mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        //mAdapter.enableDragItem(mItemTouchHelper);
        mAdapter.setOnItemDragListener(listener);
                //mRecyclerView.addItemDecoration(new GridItemDecoration(this ,R.drawable.list_divider));

        mRecyclerView.setAdapter(mAdapter);


        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ScenesListActivity.this, ActionListActivity.class);
                intent.putExtra(AppString.KEY_BEAN, mAdapter.getData().get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        unRegisterRxBus();
        super.onDestroy();
    }


}
