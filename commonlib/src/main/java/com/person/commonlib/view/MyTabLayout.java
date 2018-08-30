package com.person.commonlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import com.person.commonlib.R;
import java.lang.reflect.Field;

/**
 * 自定义， 固定显示最多个数
 */
public class MyTabLayout extends TabLayout {
  private int showCount = 4;
  private static final String SCROLLABLE_TAB_MIN_WIDTH = "mScrollableTabMinWidth";

  public MyTabLayout(Context context) {
    super(context);
    initTabMinWidth();
  }

  public MyTabLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    initAttrs(context, attrs);
    initTabMinWidth();
  }

  public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initAttrs(context, attrs);
    initTabMinWidth();
  }

  private void initTabMinWidth() {
    int screenWidth = getResources().getDisplayMetrics().widthPixels;
    int tabMinWidth = screenWidth / showCount;

    Field field;
    try {
      field = TabLayout.class.getDeclaredField(SCROLLABLE_TAB_MIN_WIDTH);
      field.setAccessible(true);
      field.set(this, tabMinWidth);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  private void initAttrs(Context context, AttributeSet attrs) {
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTabLayout);
    showCount = ta.getInt(R.styleable.MyTabLayout_showCount, 4);
    ta.recycle();
  }
}  