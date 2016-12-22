package com.timqi.animshare.animabc.evaluator;

/**
 * Created by qiqi on 2016/12/21.
 */

public class DecelerateEvaluator implements Evaluator {

  @Override
  public float eval(float process) {
    return -process * process + 2 * process;
  }
}
