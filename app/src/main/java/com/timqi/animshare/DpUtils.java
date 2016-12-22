package com.timqi.animshare;

import android.content.Context;

public class DpUtils {

  private DpUtils() {
  }

  public static int dp2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }
}
