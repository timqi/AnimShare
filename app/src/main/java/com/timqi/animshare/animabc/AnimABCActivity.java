package com.timqi.animshare.animabc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.timqi.animshare.DpUtils;
import com.timqi.animshare.R;
import com.timqi.animshare.animabc.evaluator.AccelerateEvaluator;
import com.timqi.animshare.animabc.evaluator.BounceEvaluator;
import com.timqi.animshare.animabc.evaluator.DecelerateEvaluator;
import com.timqi.animshare.animabc.evaluator.Evaluator;
import com.timqi.animshare.animabc.evaluator.LinearEvaluator;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class AnimABCActivity extends AppCompatActivity {

  private static int FREQUENCY = 100;

  private static int INTERVAL = 1000 / FREQUENCY;

  private static int endMarginHeightDP = 300;

  private TextView mAnimText;

  private Button mPerformButton;

  private int mAnimPosStart, mAnimPosEnd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    init();

    mAnimPosStart = 0;
    mAnimPosEnd = DpUtils.dp2px(this, endMarginHeightDP);

    mPerformButton.setOnClickListener(view -> performTextAnim(1000));
  }

  private void performTextAnim(int durationMilliSecond) {

    Observable.interval(INTERVAL, TimeUnit.MILLISECONDS)

        /**
         * 每隔 INTERVAL 毫秒产生一个回调
         */
        .take(durationMilliSecond / INTERVAL + 0)

        /**
         * 对进度进行归一化处理，值域为 [0, 1]
         * 完成插值动作
         */
        .map(time -> (float) time * INTERVAL / durationMilliSecond)

        /**
         * 根据线性插值结果进行估值，控制动画的缓动效果
         */
        .map(interpolated -> handleEval(interpolated))

        /**
         * 在计算线程池运算，在主线程回调 UI
         */
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())

        /**
         * 动画结果的连续回调
         * 动画过程是进度的线性函数
         *
         * y = a*x + b;
         */
        .subscribe(animProgress -> applyAnim(animProgress),

            throwable -> errorHandle(throwable),

            () -> reset());

  }

  private float handleEval(float interpolated) {
    Evaluator evaluator = null;
    switch (eval) {
      case LINEAR:
        evaluator = new LinearEvaluator();
        break;
      case ACCELERATE:
        evaluator = new AccelerateEvaluator();
        break;
      case DECELERATE:
        evaluator = new DecelerateEvaluator();
        break;
      case BOUNCE:
        evaluator = new BounceEvaluator();
        break;
    }
    return evaluator.eval(interpolated);
  }

  private void reset() {
    Observable.timer(100, TimeUnit.MICROSECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aLong -> {
          LinearLayout.LayoutParams params =
              (LinearLayout.LayoutParams) mAnimText.getLayoutParams();
          params.topMargin = 0;
          mAnimText.requestLayout();
        });
  }

  private void applyAnim(Float animProgress) {
    LinearLayout.LayoutParams params =
        (LinearLayout.LayoutParams) mAnimText.getLayoutParams();
    params.topMargin = (int) (mAnimPosStart + (mAnimPosEnd - mAnimPosStart) * animProgress);
    mAnimText.requestLayout();
  }

  private void errorHandle(Throwable throwable) {
    throwable.printStackTrace();
  }


  /**
   * 辅助代码
   */

  private Spinner mFrequencySpinner, mEvaluatorSpinner;

  private Eval eval = Eval.LINEAR;

  private void init() {
    setContentView(R.layout.activity_anim_abc);
    mAnimText = (TextView) findViewById(R.id.text);
    mPerformButton = (Button) findViewById(R.id.perform);
    mFrequencySpinner = (Spinner) findViewById(R.id.spinnerFrequency);
    mEvaluatorSpinner = (Spinner) findViewById(R.id.spinnerEvaluator);
    mFrequencySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new Integer[]{100, 50, 10}));
    mEvaluatorSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Eval.values()));
    mFrequencySpinner.setOnItemSelectedListener(onSpinnerItemSelectedListener);
    mEvaluatorSpinner.setOnItemSelectedListener(onSpinnerItemSelectedListener);

    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) findViewById(R.id.endSection).getLayoutParams();
    layoutParams.topMargin = DpUtils.dp2px(this, endMarginHeightDP + 30);
    findViewById(R.id.endSection).requestLayout();
  }

  private AdapterView.OnItemSelectedListener onSpinnerItemSelectedListener
      = new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
      switch (adapterView.getId()) {
        case R.id.spinnerFrequency:
          FREQUENCY = (int) adapterView.getItemAtPosition(i);
          INTERVAL = 1000 / FREQUENCY;
          break;
        case R.id.spinnerEvaluator:
          eval = (Eval) adapterView.getItemAtPosition(i);
          break;
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
  };
}
