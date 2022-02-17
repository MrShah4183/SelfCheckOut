package com.vasyerp.selfcheckout.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vasyerp.selfcheckout.databinding.ActivitySplashBinding;
import com.vasyerp.selfcheckout.ui.company.CompanyLoginActivity;
import com.vasyerp.selfcheckout.ui.login.UserLoginActivity;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, CompanyLoginActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }, 2000);
    }
}