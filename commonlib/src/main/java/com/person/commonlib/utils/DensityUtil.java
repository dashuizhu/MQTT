package com.person.commonlib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2016/3/22.
 */
public class DensityUtil {
  private static final String TAG = DensityUtil.class.getSimpleName();

  // 当前屏幕的densityDpi
  private static float dmDensityDpi = 0.0f;

  /**
   * 将px值转换为dip或dp值，保证尺寸大小不变
   *
   * @param context
   * @param pxValue
   *            （DisplayMetrics类中属性density）
   * @return
   */
  public static int px2dip(Context context, float pxValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (pxValue / scale + 0.5f);
  }

  /**
   * 将dip或dp值转换为px值，保证尺寸大小不变
   *
   * @param dipValue
   * @param context
   *            （DisplayMetrics类中属性density）
   * @return
   */
  public static int dip2px(Context context, float dipValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dipValue * scale + 0.5f);
  }

  /**
   * 将px值转换为sp值，保证文字大小不变
   *
   * @param pxValue
   * @param context
   *            （DisplayMetrics类中属性scaledDensity）
   * @return
   */
  public static int px2sp(Context context, float pxValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (pxValue / fontScale + 0.5f);
  }

  /**
   * 将sp值转换为px值，保证文字大小不变
   *
   * @param spValue
   * @param context
   *            （DisplayMetrics类中属性scaledDensity）
   * @return
   */
  public static int sp2px(Context context, float spValue) {
    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
    return (int) (spValue * fontScale + 0.5f);
  }

  public static float getScreenDensity(Context context) {
    return context.getResources().getDisplayMetrics().scaledDensity;
  }


  public static float dp2px(Resources resources, float dp) {
    final float scale = resources.getDisplayMetrics().density;
    return  dp * scale + 0.5f;
  }

  public static float sp2px(Resources resources, float sp){
    final float scale = resources.getDisplayMetrics().scaledDensity;
    return sp * scale;
  }

  /**
   * 获取手机屏幕宽度
   * @param context
   * @return
   */
  public static int getPhoneScreenWidth(Context context) {
    //获取屏幕信息
    return context.getResources().getDisplayMetrics().widthPixels;
  }

  public static DisplayMetrics getPhoneScreen(Context context) {
    //获取屏幕信息
    return context.getResources().getDisplayMetrics();
  }

  @Override
  public String toString() {
    return " dmDensityDpi:" + dmDensityDpi;
  }

  public static int getPhoneScreenHeight(Context context) {
    return context.getResources().getDisplayMetrics().heightPixels;
  }
}
