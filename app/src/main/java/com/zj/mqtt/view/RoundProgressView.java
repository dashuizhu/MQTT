package com.zj.mqtt.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import com.person.commonlib.utils.DensityUtil;
import com.zj.mqtt.R;

public class RoundProgressView extends View {

    private Paint mBgPaint;//背景圆环
    private Paint mProgressPaint;//进度圆环
    public int mMaxProgress = 36;

    private int nowProgress = 0;

    private int angle;

    public void setProgress(int progress) {
        nowProgress = progress;
        invalidate();
    }

    public void setMaxProgress(int max) {
        mMaxProgress = max;
        angle = 360 / mMaxProgress;
    }

    public RoundProgressView(Context context) {
        super(context);
    }

    public RoundProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    private void initPaint(Context mContext) {

        angle = 360 / mMaxProgress;

        mBgPaint = new Paint();
        mBgPaint.setColor(ContextCompat.getColor(mContext, R.color.blue_alpha));
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setStrokeWidth(1);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeWidth(1);
        mProgressPaint.setTextSize(DensityUtil.sp2px(mContext, 35));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(0, 0);

        String text = String.valueOf(mMaxProgress - nowProgress);

        //绘制文本
        Rect mBound = new Rect();
        mProgressPaint.getTextBounds(text, 0, text.length(), mBound);
        int centerX = getWidth() / 2;
        int centerY = getWidth() / 2;
        int textHalfWidth = mBound.width() / 2;
        int textHalfHeight = mBound.height() / 2;
        canvas.drawText(text, centerX - textHalfWidth, centerY + textHalfHeight, mProgressPaint);

        //以圆心绘画，默认角度是从右边开始，
        canvas.translate(getWidth() / 2, getHeight() / 2);
        canvas.rotate(270);
        canvas.save();
        RectF rectF = new RectF(getWidth() / 2 - 30, 0, getWidth() / 2 - 10, 6);
        //绘画背景
        for (int i = 0; i < mMaxProgress; i++) {
            canvas.drawRoundRect(rectF, 3, 3, mBgPaint);
            canvas.rotate(angle);
        }
        canvas.restore();
        //绘画进度
        for (int i = 0; i < nowProgress; i++) {
            canvas.drawRoundRect(rectF, 3, 3, mProgressPaint);
            canvas.rotate(angle);
        }
    }
}