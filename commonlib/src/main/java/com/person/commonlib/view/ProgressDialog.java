package com.person.commonlib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.person.commonlib.R;

/**
 * 加载进度框
 *
 * @author zhuj
 *         2017/6/19 上午10:40.
 */
public class ProgressDialog extends Dialog {

  public ProgressDialog(@NonNull Context context) {
    super(context, R.style.MyDialog);
    //super(context);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.com_dialog_progress);
  }
}
