package com.zj.mqtt.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import com.zj.mqtt.AppApplication;
import com.zj.mqtt.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author zhuj
 * @date 2017/6/13 下午6:36.
 */
public class AppUtils {

  /**
   * 得到amr的时长
   *
   * @param file
   * @return 单位秒
   * @throws IOException
   */
  public static int getAmrDuration(File file) throws IOException {
    long duration = -1;
    int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0, 0,
            0, 0 };
    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "rw");
      long length = file.length();// 文件的长度
      int pos = 6;// 设置初始位置
      int frameCount = 0;// 初始帧数
      int packedPos = -1;

      byte[] datas = new byte[1];// 初始数据值
      while (pos <= length) {
        randomAccessFile.seek(pos);
        if (randomAccessFile.read(datas, 0, 1) != 1) {
          duration = length > 0 ? ((length - 6) / 650) : 0;
          break;
        }
        packedPos = (datas[0] >> 3) & 0x0F;
        pos += packedSize[packedPos] + 1;
        frameCount++;
      }

      duration += frameCount * 20;// 帧数*20
    } finally {
      if (randomAccessFile != null) {
        randomAccessFile.close();
      }
    }
    return (int)((duration/1000)+1);
  }

  /**
   * 判断是否是主进程， 这里在androidManifest里设置进程名都带:xxxx
   * application 获得进程是  packageName
   */
  public static boolean isMainProcess(Context mContext) {
    String packageName = mContext.getPackageName();
    // String className = mContext.getClass().getSimpleName();
    String corrProcess = getCurrentProcessName(mContext);
    if (corrProcess == null) {//iuni上会出现这个问题。
      return false;
    }
    if (corrProcess.equals(packageName)) {
      return true;
    }
    return false;
  }


  /**
   * 获得当前进程名称
   */
  public static String getCurrentProcessName(Context context) {
    int pid = android.os.Process.myPid();
    android.app.ActivityManager mActivityManager =
            (android.app.ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    for (android.app.ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
      if (appProcess.pid == pid) {
        return appProcess.processName;
      }
    }
    return null;
  }

  /**
   * 判断是手机号是否正确；上个方法在编译时会报错
   *
   * @param s String 类型
   */
  public static boolean isRightPhoneNumber(String s) {
    Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[012356789]|16[0-9]|17[01235678]|18[0-9]|19[0-9])[0-9]{8}$");
    Matcher m = p.matcher(s);
    boolean b = m.matches();
    return b;
  }

  /**
   * 输入框 光标移动到最后一个文字处
   */
  public static void initSelecton(EditText et) {
    if (et != null) {
      String str = et.getText().toString();
      if (str.length() > 0) {
        et.setSelection(str.length());
      }
    }
  }

  public static String formatTime(long milli) {
    String pattern = "mm:ss";
    int m = (int) (milli / (60*1000));
    int s = (int) ((milli / 1000) % 60);
    String mm = String.format(Locale.getDefault(), "%02d", m);
    String ss = String.format(Locale.getDefault(), "%02d", s);
    return pattern.replace("mm", mm).replace("ss", ss);
  }

  /**
   * 需要权限:android.permission.GET_TASKS
   * 判断本程序是否运行在前台
   *
   * @return true
   */
  public static boolean isApplicationInFornt(Context context) {
    if (isScreenLock(context)) {
      //如果是锁屏状态，直接返回当做不在前台
      return false;
    }

    android.app.ActivityManager am =
            (android.app.ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
    if (tasks != null && !tasks.isEmpty()) {
      ComponentName topActivity = tasks.get(0).topActivity;
      //LogUtils.v("tools", " the app is "+topActivity.getPackageName() );
      if (topActivity.getPackageName().equals(context.getPackageName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断页面是否正在显示
   */
  public static boolean isActivityTop(Context context, Class cls) {
    android.app.ActivityManager am =
            (android.app.ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
    if (list != null && list.size() > 0) {
      ComponentName cpn = list.get(0).topActivity;
      if (cls.getName().equals(cpn.getClassName())) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断某一个类是否存在任务栈里面
   * @return
   */
  public static boolean isExsitActivity(Context context, Class<?> cls){
    Intent intent = new Intent(context, cls);
    ComponentName cmpName = intent.resolveActivity(context.getPackageManager());
    boolean flag = false;
    if (cmpName != null) { // 说明系统中存在这个activity
      ActivityManager
              am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
      List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
      for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
        if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
          flag = true;
          break;  //跳出循环，优化效率
        }
      }
    }
    return flag;
  }

    //安装apk
  public static void installApk(Context context,File file) {
    Intent installApkIntent = new Intent();
    installApkIntent.setAction(Intent.ACTION_VIEW);
    installApkIntent.addCategory(Intent.CATEGORY_DEFAULT);
    installApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    //if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
      //installApkIntent.setDataAndType(FileProvider.getUriForFile(sApp, PermissionUtil.getFileProviderAuthority(), apkFile), "application/vnd.android.package-archive");
      //installApkIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    //} else {
      installApkIntent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
    //}
    context.startActivity(installApkIntent);
  }


  public static int getPackageVersionCode() {
    int versionCode = 0;
    Context context = AppApplication.getApp();
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      versionCode = info.versionCode;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionCode;
  }

  /**
   * 获得版本名
   * @return
   */
  public static String getPackageVersionName() {
    String versionName = "";
    Context context = AppApplication.getApp();
    try {
      PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
      versionName = info.versionName;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return versionName;
  }

  /**
   * 描述：判断网络是否有效.
   *
   * @return true, if is network available
   */
  public static boolean isNetworkAvailable(Context context) {
    try {
      ConnectivityManager
              connectivity = (ConnectivityManager) context.getApplicationContext().getSystemService(
              Context.CONNECTIVITY_SERVICE);
      if (connectivity != null) {
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
          if (info.getState() == NetworkInfo.State.CONNECTED) {
            return true;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  public static int getTargetVersion () {
    int targetSdkVersion = 0;
    try {
      Context context = AppApplication.getApp();
      final PackageInfo info = context.getPackageManager().getPackageInfo(
              context.getPackageName(), 0);
      targetSdkVersion = info.applicationInfo.targetSdkVersion;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return targetSdkVersion;
  }

  private static String mChannelValue;

  /**
   * 获得当前渠道，
   * inner 表示是内置膜
   */
  public static String getChannel(Context context) {
    if (mChannelValue == null) {
      synchronized (AppUtils.class) {
        if (mChannelValue == null) {
          ApplicationInfo appInfo = null;
          try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
          } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
          }
          if (appInfo.metaData.containsKey("UMENG_CHANNEL")) {
            mChannelValue = appInfo.metaData.getString("UMENG_CHANNEL");
          } else {
            mChannelValue = "buddy";
          }
        }
      }
    }
    return mChannelValue;
  }

  public static String getRingDuring(String mUri){
    String duration=null;
    android.media.MediaMetadataRetriever mmr = new android.media.MediaMetadataRetriever();

    try {
      if (mUri != null) {
        HashMap<String, String> headers=new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.4.2; zh-CN; MW-KW-001 Build/JRO03C) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 UCBrowser/1.0.0.001 U4/0.8.0 Mobile Safari/533.1");
        mmr.setDataSource(mUri, headers);
      }

      duration = mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      mmr.release();
    }
    return duration;
  }

  public static boolean isApkDebugModel() {
    return BuildConfig.DEBUG;
  }

  /**
   * 是否包含中文
   * @param str
   * @return
   */
  public static boolean isContainsChinese(String str)
  {
    Pattern pat = Pattern.compile("[\\u4e00-\\u9fa5]");
    Matcher matcher = pat.matcher(str);
    boolean flg = false;
    if (matcher.find())    {
      flg = true;
    }
    return flg;
  }

  /**
   * 获取播放数量
   * @param count
   * @return
   */
  public static String getReadCount(int count)
  {
    DecimalFormat df   = new DecimalFormat("######0.0");
    String countStr;
    if (count < 10000) {
      countStr = count + "";
    }else{
      double countDouble = count/10000.0;
      countStr = df.format(countDouble)+"万";
    }
    return countStr;
  }

  /**
   * 返回byte的数据大小对应的文本
   *
   * @param size
   * @return
   */
  public static String getDataSize(long size) {
    if (size < 0) {
      size = 0;
    }
    DecimalFormat formater = new DecimalFormat("####.00");
    if (size < 1024) {
      return size + "bytes";
    } else if (size < 1024 * 1024) {
      float kbsize = size / 1024f;
      return formater.format(kbsize) + "KB";
    } else if (size < 1024 * 1024 * 1024) {
      float mbsize = size / 1024f / 1024f;
      return formater.format(mbsize) + "MB";
    } else if (size < 1024 * 1024 * 1024 * 1024) {
      float gbsize = size / 1024f / 1024f / 1024f;
      return formater.format(gbsize) + "GB";
    } else {
      return "size: error";
    }

  }


  /**
   * 是否锁屏
   * @param context
   * @return  true 表示锁屏， false表示正常
   */
  public static boolean isScreenLock(Context context) {
    KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

    boolean
            flag = mKeyguardManager.inKeyguardRestrictedInputMode();
    return flag;
  }

  /**
   * 判断qq是否可用
   *
   * @param context
   * @return
   */
  public static boolean checkAppIsInstall(Context context,String packName) {
    final PackageManager packageManager = context.getPackageManager();
    List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
    if (pinfo != null) {
      for (int i = 0; i < pinfo.size(); i++) {
        String pn = pinfo.get(i).packageName;
        if (pn.equals(packName)) {
          return true;
        }
      }
    }
    return false;
  }

}
