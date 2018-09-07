package com.zj.mqtt.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.zj.mqtt.R;

/**
 * 设置——信息栏
 *
 * @author zhuj
 * @date 2017/6/12 下午2:24
 */
public class MyItemView extends RelativeLayout {

  private ImageView mIvTitle;     //左侧icon
  private TextView mTvTitle;     //文字表述
  private TextView mTvContent;   //要显示的数字
  private ImageView mIvArrows;    //右侧前进的图片
  private ImageView mIvRedTip;    //红色小圆点
  private View mViewDivide;  //分割线

  public MyItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context, attrs);
  }

  private void initView(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyItemView);
    String item_content = ta.getString(R.styleable.MyItemView_item_content);
    String item_title = ta.getString(R.styleable.MyItemView_item_title);
    int item_icon = ta.getResourceId(R.styleable.MyItemView_item_icon, 0);
    int item_arrows = ta.getResourceId(R.styleable.MyItemView_item_arrows, R.mipmap.list_right_arrow);
    int item_content_drawright = ta.getResourceId(R.styleable.MyItemView_item_content_drawright, 0);
    boolean item_enable = ta.getBoolean(R.styleable.MyItemView_item_enable, true);
    boolean item_divide = ta.getBoolean(R.styleable.MyItemView_item_divide, true);
    boolean item_checked = ta.getBoolean(R.styleable.MyItemView_item_checked, false);
    boolean item_arrows_show = ta.getBoolean(R.styleable.MyItemView_item_arrows_show, true);
    int contentColor = ta.getColor(R.styleable.MyItemView_item_content_color, 0);
    float contentSize = ta.getDimension(R.styleable.MyItemView_item_content_size, 0);

    ta.recycle();
    View itemView = LayoutInflater.from(context).inflate(R.layout.view_my_item, this);
    mIvTitle = (ImageView) itemView.findViewById(R.id.imageView_my_item_icon);
    mTvTitle = (TextView) itemView.findViewById(R.id.textView_my_item_title);
    mTvContent = (TextView) itemView.findViewById(R.id.textView_my_item_content);
    mIvArrows = (ImageView) itemView.findViewById(R.id.textView_my_item_arrows);
    mIvRedTip = (ImageView) itemView.findViewById(R.id.imageView_my_item_update_tip);
    mViewDivide = itemView.findViewById(R.id.view_my_item_divide);
    mTvTitle.setText(item_title);
    mIvArrows.setSelected(item_checked);
    if (item_icon == 0) {
      mIvTitle.setVisibility(View.GONE);
    } else {
      mIvTitle.setVisibility(View.VISIBLE);
      mIvTitle.setBackgroundResource(item_icon);
    }

    if (item_content_drawright !=0) {
      Drawable draw = ContextCompat.getDrawable(context, item_content_drawright);
      draw.setBounds(0, 0, draw.getIntrinsicWidth(), (int) (draw.getMinimumHeight()));
      mTvContent.setCompoundDrawables(null , null , draw, null);
    }

    mTvContent.setText(item_content);
    mIvArrows.setBackgroundResource(item_arrows);
    mIvArrows.setVisibility(item_arrows_show ? View.VISIBLE : View.INVISIBLE);

    if (contentColor != 0) {
      mTvContent.setTextColor(contentColor);
    }
    if (contentSize != 0) {
      mTvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, contentSize);
    }

    setDivide(item_divide);
    setItemEnable(item_enable);
  }

  /**
   * 设置是否显示右边图标
   */
  public void setIvArrowsVisible(int visible){
    mIvArrows.setVisibility(visible);
  }

  /**
   * 是否可点击 颜色变化
   */
  public void setItemEnable(boolean enable) {
    setEnabled(enable);
  }

  /**
   * 设置描述性数字
   */
  public void setTextContent(String descriptionContent) {
    mTvContent.setText(descriptionContent);
  }

  public void setTextContent(@StringRes int  strRes) {
    mTvContent.setText(strRes);
  }

  public String getTextContent() {
    return mTvContent.getText().toString();
  }

  public void setChecked(boolean isChecked) {
    mIvArrows.setSelected(isChecked);
  }

  public boolean isChecked() {
    return mIvArrows.isSelected();
  }

  /**
   * 设置红色小圆点提醒
   */
  public void setRedTipVisibility(int Visibility) {
    mIvRedTip.setVisibility(Visibility);
  }

  public void setDivide(boolean visible) {
    if (visible) {
      mViewDivide.setVisibility(VISIBLE);
    } else {
      mViewDivide.setVisibility(GONE);
    }
  }

  public void setArrowsVisible(int visible) {
    mIvArrows.setVisibility(visible);
  }

  public void setArrowsRes(@DrawableRes int drawRes) {
    mIvArrows.setBackgroundResource(drawRes);
  }

  public void setTitle(String title) {
    mTvTitle.setText(title);
  }

  public void setItemIcon(@DrawableRes int itemIcon) {
    mIvTitle.setBackgroundResource(itemIcon);
  }
}
