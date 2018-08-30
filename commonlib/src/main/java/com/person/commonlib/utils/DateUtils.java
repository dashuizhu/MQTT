package com.person.commonlib.utils;

import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhuj
 * @date 2017/6/9 下午6:31.
 */
public class DateUtils {

  public static SimpleDateFormat sdf_none = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  public static SimpleDateFormat sdf_none_zh = new SimpleDateFormat("yyyy年MM月dd HH:mm");
  public static SimpleDateFormat sdf_year = new SimpleDateFormat("MM-dd HH:mm");
  public static SimpleDateFormat sdf_year_zh = new SimpleDateFormat("MM月dd日 HH:mm");
  public static SimpleDateFormat sdf_month = new SimpleDateFormat("dd HH:mm");
  public static SimpleDateFormat sdf_date_none = new SimpleDateFormat("yyyy-MM-dd");
  public static SimpleDateFormat sdf_date_none_zh = new SimpleDateFormat("yyyy年MM月dd日");
  public static SimpleDateFormat sdf_date_year = new SimpleDateFormat("MM-dd");
  public static SimpleDateFormat sdf_date_year2 = new SimpleDateFormat("MM/dd");
  public static SimpleDateFormat sdf_date_year_zh = new SimpleDateFormat("MM月dd日");
  public static SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");
  public static SimpleDateFormat sdf_year_month = new SimpleDateFormat("yyyy-MM");

  /**
   * 获得指定时期的  单月最大 日
   */
  public static int getMaxDayByMonth(int year, int month) {
    int maxDay = 31;
    switch (month) {
      case 2:
        if (year % 100 != 0 && year % 4 == 0) {
          maxDay = 29;
        } else {
          maxDay = 28;
        }
        break;
      case 4:
      case 6:
      case 9:
      case 11:
        maxDay = 30;
        break;
      default:
    }
    return maxDay;
  }

  /**
   * 根据时间戳 ，获得HH：mm
   */
  public static String getTime(long time) {
    Date date = new Date(time);
    return String.format("%02d:%02d", date.getHours(), date.getMinutes());
  }

  /**
   * 获得 小时 *60 + 分钟
   */
  public static int getMinuteOfDay(long time) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    return hour * 60 + minute;
  }

  /**
   * @param time HH:mm
   */
  public static int getMinuteOfDay(String time) {
    int[] array = getHourAndMinute(time);
    return array[0] * 60 + array[1];
  }

  /**
   * 将 hh:mm的时间 转化成 int[]{hh:mm}
   *
   * @return 默认返回当前小时：分钟
   */
  public static int[] getHourAndMinute(String time) {
    int hour, minute;
    try {
      Date date = sdf_time.parse(time);
      hour = date.getHours();
      minute = date.getMinutes();
    } catch (Exception e) {
      e.printStackTrace();
      Calendar c = Calendar.getInstance();
      hour = c.get(Calendar.HOUR_OF_DAY);
      minute = c.get(Calendar.MINUTE);
    }
    int[] timeArray = new int[2];
    timeArray[0] = hour;
    timeArray[1] = minute;
    return timeArray;
  }

  ///**
  // * yyyy.MM.dd HH:mm:ss
  // * @param time hh:mm
  // * @return
  // */
  //public static String getHourAndMinuteFormat(String time) {
  //  if (isDateString(time)) {
  //
  //  }
  //  return "";
  //}
  //
  //public static boolean isDateString(String date) {
  //  Pattern p = Pattern.compile("^(?<year>\\d{2,4})[.](?<month>\\d{1,2})[.](?<day>\\d{1,2})$");
  //  Matcher m = p.matcher(date);
  //  boolean b = m.matches();
  //  return b;
  //}

  /**
   * 将星期数值转换成文字，
   * 每天、工作日、 一、三、五 类似这样
   */
  //public static String getWeekValueString(String weekValue) {
  //  String str = "";
  //  if (TextUtils.isEmpty(weekValue)) {
  //    return "";
  //  }
  //  if (weekValue.length() != 7) {
  //    return "";
  //  }
  //  if (weekValue.equals("1111111")) {
  //    str = AppApplication.getContext().getString(R.string.label_week_all);
  //  } else if (weekValue.equals("0111110")) {
  //    str = AppApplication.getContext().getString(R.string.label_week_workday);
  //  } else {
  //    StringBuffer stringBuffer = new StringBuffer();
  //    stringBuffer.append("周");
  //    if (weekValue.charAt(1) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_1));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(2) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_2));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(3) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_3));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(4) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_4));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(5) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_5));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(6) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_6));
  //      stringBuffer.append(",");
  //    }
  //    if (weekValue.charAt(0) == '1') {
  //      stringBuffer.append(AppApplication.getContext().getString(R.string.label_week_short_7));
  //    } else {
  //      if (stringBuffer.length() > 0) {
  //        //去除最后一个'、'
  //        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
  //      }
  //    }
  //    //也就是说有星期值
  //    //if (stringBuffer.length()>0) {
  //    //   str = "星期"+stringBuffer.toString();
  //    //}
  //    str = stringBuffer.toString();
  //  }
  //  return str;
  //}

  /**
   * MM-dd hh:mm
   */
  public static String getDateFormat(long msgTime) {
    Date d = new Date(msgTime);
    return sdf_year.format(d);
  }

  /**
   * MM年dd日 hh:mm
   */
  public static String getDateFormatZH(long msgTime) {
    Date d = new Date(msgTime);
    return sdf_year_zh.format(d);
  }

  /**
   * YYYY年MM年dd日 hh:mm
   */
  public static String getDateFormatAllZH(long msgTime) {
    Date d = new Date(msgTime);
    return sdf_none_zh.format(d);
  }

  /**
   * 两个日期是否同一天
   * @param time1
   * @param time2
   * @return
   */
  public static boolean isOneDay(long time1, long time2) {
    Calendar cTime1 = Calendar.getInstance();
    Calendar cTime2 = Calendar.getInstance();
    cTime1.setTimeInMillis(time1);
    cTime2.setTimeInMillis(time2);

    boolean isDay = cTime1.get(Calendar.DAY_OF_YEAR) == cTime2.get(Calendar.DAY_OF_YEAR);
    if (isDay) {
      return cTime1.get(Calendar.YEAR) == cTime2.get(Calendar.YEAR);
    }
    return true;
  }

  public static boolean isToday(long time) {
    time = castMills(time);
    //与今天0点0份判断，是否是当天
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    long todayTime = calendar.getTimeInMillis();
    if (time >= todayTime) { //大于今天，是否小于明天
      long yesterday = todayTime + 24 * 3600 * 1000;
      return time < yesterday;
    }
    return false;
  }

  public static boolean isYesterday(long time) {
    time = castMills(time);
    //与今天0点0份判断，是否是当天
    Calendar calendarNow = Calendar.getInstance();
    Calendar cTime = Calendar.getInstance();
    cTime.setTimeInMillis(time);
    int cDay = cTime.get(Calendar.DAY_OF_YEAR);
    int nowDay = calendarNow.get(Calendar.DAY_OF_YEAR);
    return (nowDay == cDay + 1);
  }

  public static String getDateByNone(Date time) {

    return sdf_date_none.format(time);
  }

  public static String getDateByNoneZH(long time) {
    return sdf_date_none_zh.format(new Date(time));
  }

  /**
   * 返回 MM-dd
   */
  public static String getDateByYear(Date date) {
    return sdf_date_year.format(date);
  }

  public static String getDateByYear2(long time) {
    time = castMills(time);
    return sdf_date_year2.format(new Date(time));
  }

  /**
   * 返回 MM月dd日
   */
  public static String getDateByYearZH(long time) {
    time = castMills(time);
    return sdf_date_year_zh.format(new Date(time));
  }

  /**
   * 返回 MM月
   */
  public static String getMouthByYearZH(long time) {
    time = castMills(time);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    int month = calendar.get(Calendar.MONTH) + 1;
    return month + "月";
  }

  /**
   * 返回 dd日
   */
  public static String getDay(long time) {
    time = castMills(time);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(time);
    return String.format("%02d日", calendar.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * 显示 mm:ss ，如果超过1小时就 hh:mm:ss
   */
  public static String showTimeAll(int maxTime) {
    if (maxTime < 3600) {
      return String.format("%02d:%02d", maxTime / 60, maxTime % 60);
    } else {
      return String.format("%02d:%02d:%02d", maxTime / 3600, maxTime % 3600 / 60, maxTime % 60);
    }
  }

  public static String getToday(String formatStr) {
    Calendar now = Calendar.getInstance();
    int year = now.get(Calendar.YEAR);
    int month = now.get(Calendar.MONTH) + 1;
    int day = now.get(Calendar.DAY_OF_MONTH);
    return String.format(formatStr, year, month, day);
  }

  /**
   * 一小时内  xx分钟前
   * 24小时内  xx小时前
   * 昨天
   * xx月xx日
   *
   * @param time 时间
   * @return 显示内容
   */
  public static String getTimeShowString(long time) {
    long now = System.currentTimeMillis();
    long delayTime = now - time;
    if (delayTime < 3600 * 1000) { //一小时内
      int a = (int) (delayTime / 60000);
      if (a < 1) a = 1;
      return a + "分钟前";
    } else if (delayTime < 24 * 3600 * 1000) { //24小时内
      int hour = (int) (delayTime / 3600000);
      return hour + "小时前";
    } else if (isYesterday(time)) {
      return "昨天";
    } else {
      return DateUtils.getDateFormat(time);
    }
  }

  /**
   * 1分钟以内 刚刚
   * 一小时内  xx分钟前
   * 24小时内  xx小时前
   * 昨天
   * xx月xx日
   *
   * @param time 时间
   * @return 显示内容
   */
  public static String showGroupMessageDate(long time) {
    long now = System.currentTimeMillis();
    long delayTime = now - time * 1000;
    if (delayTime < 3600 * 1000) { //一小时内
      int a = (int) (delayTime / 60000);
      if (a < 1) {
        return "刚刚";
      } else {
        return a + "分钟前";
      }
    } else if (delayTime < 24 * 3600 * 1000) { //24小时内
      int hour = (int) (delayTime / 3600000);
      return hour + "小时前";
    } else if (isYesterday(time)) {
      return "昨天";
    } else {
      return DateUtils.getDateFormat(time);
    }
  }

  /**
   * @param time 毫秒
   */
  public static String showGroupHistroyDate(long time) {
    long now = System.currentTimeMillis();
    long delayTime = now - 1000;
    if (delayTime < 3600 * 1000) { //一小时内
      int a = (int) (delayTime / 60000);
      if (a < 1) {
        return "刚刚";
      } else {
        return a + "分钟前";
      }
    } else if (delayTime < 24 * 3600 * 1000) { //24小时内
      int hour = (int) (delayTime / 3600000);
      return hour + "小时前";
    } else if (isYesterday(time)) {
      return "昨天";
    } else {
      return sdf_none.format(new Date(time));
    }
  }

  public static String getTimeByMillis(long time) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return "(" + sdf.format(new Date(time)) + ")";
  }

  public static String getTimeToDate(long time) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    return sdf.format(new Date(time));
  }

  /**
   * 通过时间秒毫秒数判断两个时间的间隔
   */
  public static String getDayForMillis(long time) {
    long now = System.currentTimeMillis();
    long delayTime = now - time * 1000;
    if (delayTime < 3600 * 1000) { //一小时内
      int a = (int) (delayTime / 60000);
      if (a < 1) a = 1;
      return a + "分钟前";
    } else if (delayTime < 24 * 3600 * 1000) { //24小时内
      int hour = (int) (delayTime / 3600000);
      return hour + "小时前";
    } else if (delayTime >= 24 * 3600 * 1000) {
      int day = (int) (delayTime / (3600000 * 24));
      return day + "天前" + getTimeByMillis(time * 1000);
    } else {
      return "";
    }
  }

  public static String getDayForMillisToSchool(long time) {
    long now = System.currentTimeMillis();
    long delayTime = now - time * 1000;
    if (delayTime < 60 * 1000) {
      return "更新于刚刚";
    } else if (delayTime < 3600 * 1000) { //一小时内
      int a = (int) (delayTime / 60000);
      if (a < 1) a = 1;
      return "更新于" + a + "分钟前";
    } else if (delayTime < 24 * 3600 * 1000) { //24小时内
      int hour = (int) (delayTime / 3600000);
      return "更新于" + hour + "小时前";
    } else if (delayTime <= 24 * 3600 * 1000 * 2) {
      return "更新于昨天";
    } else if (delayTime >= 24 * 3600 * 1000 * 2) {
      //在今年内
      if (isYearInner(time * 1000)) {
        return "更新于 " + getDateByYear(new Date(time * 1000));
      } else {
        return "更新于 " + getTimeToDate(time * 1000);
      }

    } else {
      return "";
    }
  }

  /**
   * 判断当前日期是星期几
   */

  //public static String getWeek(CalendarDay calendarDay) {
  //
  //  String week = "星期";
  //  Calendar c = Calendar.getInstance();
  //  c.setTime(calendarDay.getDate());
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
  //    week += "天";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
  //    week += "一";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
  //    week += "二";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
  //    week += "三";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
  //    week += "四";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
  //    week += "五";
  //  }
  //  if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
  //    week += "六";
  //  }
  //  return week;
  //}

  /**
   * 是否在7天内，
   */
  public static boolean inLast7Day(long time) {
    long betime = System.currentTimeMillis() - time;
    if (betime > 0) {
      int day = (int) (betime / (3600 * 24 * 1000));
      return day < 7;
    }
    return false;
  }

  public static boolean isYearInner(long time) {
    time = castMills(time);
    Calendar calendar = Calendar.getInstance();
    int nowYear = calendar.get(Calendar.YEAR);

    calendar.setTimeInMillis(time);
    int timeYear = calendar.get(Calendar.YEAR);
    return nowYear == timeYear;
  }

  /**
   * MessageType界面显示时间
   */
  public static String getMessageTypeTimeShowString(long ts) {
    String showTime = "";
    ts = castMills(ts);
    if (DateUtils.isToday(ts)) { //表示是当天 只显示时和分
      showTime = DateUtils.getTime(ts);
    } else { //显示完整 yyyy-MM-dd
      showTime = DateUtils.getDateFormatZH(ts);
    }
    return showTime;
  }

  /**
   * 时间戳，如果是秒，就转成毫秒
   */
  public static long castMills(long ts) {
    //16 * 10亿 ， 如果是秒为单位， 可能值为 十几亿， 小于的话说明是秒单位
    if (ts < (1L << 34)) {
      ts = ts * 1000;
    }
    return ts;
  }


}
