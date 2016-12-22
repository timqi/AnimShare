package com.timqi.animshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.timqi.animshare.animabc.AnimABCActivity;
import com.timqi.animshare.ball.MagicBallActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.animabc).setOnClickListener(v -> startActivity(new Intent(this, AnimABCActivity.class)));
    findViewById(R.id.magicball).setOnClickListener(v -> startActivity(new Intent(this, MagicBallActivity.class)));
  }
}
