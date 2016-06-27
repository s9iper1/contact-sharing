package com.byteshaft.contactsharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

public class SplashScreen extends Activity implements View.OnClickListener {

    private Button mLogin;
    private Button mShare;
    private ImageView mLogo;
    AlphaAnimation blinkanimation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen_layout);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mLogin = (Button) findViewById(R.id.button_login);
        mShare = (Button) findViewById(R.id.button_share);
        mLogo = (ImageView) findViewById(R.id.logo_img);
        mLogin.setOnClickListener(this);

        blinkanimation = new AlphaAnimation(1, (float) 0.4);
        blinkanimation.setDuration(1000); // duration - half a second
        blinkanimation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        blinkanimation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        blinkanimation.setRepeatMode(Animation.REVERSE);
        mLogo.startAnimation(blinkanimation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                break;
            case R.id.button_share:
                // TODO: 27/06/2016 Share
                break;
        }
    }
}
