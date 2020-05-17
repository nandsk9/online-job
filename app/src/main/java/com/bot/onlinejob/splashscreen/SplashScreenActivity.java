package com.bot.onlinejob.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bot.onlinejob.R;
import com.bot.onlinejob.appintro.IntroActivity;
import com.google.firebase.FirebaseApp;


public class SplashScreenActivity extends AppCompatActivity {
    private ImageView splashLogo;
    private static int splashTimeOut=5000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //initialize the firebase app
        FirebaseApp.initializeApp(this);
        // make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        splashLogo=(ImageView)findViewById(R.id.splash_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(i);
                //finish activity
                //remove from backstack
                finish();
            }
        },splashTimeOut);

        Animation splashAnimation = AnimationUtils.loadAnimation(this,R.anim.splashanimation);
        //apply animation on logo
        splashLogo.startAnimation(splashAnimation);
    }
}
