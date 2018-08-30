package com.person.commonlib.view;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * RecyclerView 在使用gradLayoutManager左右padding
 * @author zhuj 2018/8/8 下午6:17
 */
public class RecyclerViewSpaceItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 左右 和中间间隔 都是1：1：1
     */
    public final static int CHAIN_SPEND = 0;
    /**
     * 左右无边距， 中间均匀分布
     * item * item * item
     */
    public final static int CHAIN_SPEND_INSIDE = 1;
    /**
     * 左右边距，中间无边距
     * * item item *
     */
    public final static int CHAIN_PACKED = 2;

    private int space; //边距
    private int row;   //一共多少列
    private boolean hasHeader;//是否有headerView
    private int chain;

    /**
     * @param space 边距
     * @param row l列数
     * @param hasHeader 是否有头布局， true 表示对第一个view 不添加边距
     */
    public RecyclerViewSpaceItemDecoration(int space, int row, boolean hasHeader) {
        if (row < 1) {
            throw new RuntimeException("row can't  less then 1 ");
        }
        this.space = space;
        this.row = row;
        this.hasHeader = hasHeader;
        chain = CHAIN_SPEND;
    }

    public RecyclerViewSpaceItemDecoration(int space, int row, boolean hasHeader, int chain) {
        if (row < 1) {
            throw new RuntimeException("row can't  less then 1 ");
        }
        this.space = space;
        this.row = row;
        this.hasHeader = hasHeader;
        this.chain = chain;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
            RecyclerView.State state) {

        int position = parent.getChildLayoutPosition(view);
        if (hasHeader) {
            //这个是header，不添加左右padding
            if (position == 0) {
                return;
            }
        }
        if (!hasHeader) {
            //有header ，下标是从1开始，  没有header是从0开始，这里统一默认下标都是1开始
            position += 1;
        }
        //这个是子view
        if (position % row == 1) {
            //第一列， 左space， 右space/2
            if (chain == CHAIN_SPEND) {
                //有左边距
                outRect.left = space;
            } else {
                outRect.left = 0;
            }
            outRect.right = space / 2;
            outRect.bottom = space;
        } else if (position % row == 0) {
            //最后一列， 左space/2, 右space
            outRect.left = space / 2;
            if (chain == CHAIN_SPEND) {
                //有右边距
                outRect.right = space;
            } else {
                outRect.right = 0;
            }
            outRect.bottom = space;
        } else {                        //中间的列都是 space/2
            outRect.left = space / 2;
            outRect.right = space / 2;
            outRect.bottom = space;
        }
        if (position <= row) {
            //只有第一排， 添加头部padding， 后续的都有bottom
            outRect.top = space;
        }
    }
}