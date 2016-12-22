package com.timqi.animshare.ball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.timqi.animshare.R;

public class MagicBallActivity extends AppCompatActivity {

  private Button btnStart;

  private MagicBall ball;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_magic_ball);
    ball = (MagicBall) findViewById(R.id.ball);
    this.btnStart = (Button) findViewById(R.id.btnStart);
    btnStart.setOnClickListener(v -> ball.startAnimation());
  }
}
