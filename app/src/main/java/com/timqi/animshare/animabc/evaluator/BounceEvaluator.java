package com.timqi.animshare.animabc.evaluator;

/**
 * Created by qiqi on 2016/12/21.
 */

public class BounceEvaluator implements Evaluator {

  private static float bounce(float t) {
    return t * t * 8.0f;
  }

  @Override
  public float eval(float process) {
    process *= 1.1226f;
    if (process < 0.3535f) return bounce(process);
    else if (process < 0.7408f) return bounce(process - 0.54719f) + 0.7f;
    else if (process < 0.9644f) return bounce(process - 0.8526f) + 0.9f;
    else return bounce(process - 1.0435f) + 0.95f;
  }
}
