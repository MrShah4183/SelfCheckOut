package com.vasyerp.selfcheckout.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vasyerp.selfcheckout.databinding.ActivitySplashBinding;
import com.vasyerp.selfcheckout.ui.company.CompanyLoginActivity;
import com.vasyerp.selfcheckout.ui.login.UserLoginActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding splashBinding;
    String userFirstName, userLastName, userMobile, userAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashBinding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(splashBinding.getRoot());

        userFirstName = PreferenceManager.userFirstName(SplashActivity.this);
        userLastName = PreferenceManager.userLastName(SplashActivity.this);
        userLastName = PreferenceManager.userMobile(SplashActivity.this);
        userAddress = PreferenceManager.userAddress(SplashActivity.this);


        new Handler().postDelayed(() -> {
            //todo check user login or not according pass intent to activity
            if (userFirstName.trim().isEmpty() && userLastName.trim().isEmpty() && userLastName.trim().isEmpty() && userAddress.trim().isEmpty()) {
                Intent intent = new Intent(SplashActivity.this, UserLoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            } else {
                Intent intent = new Intent(SplashActivity.this, CompanyLoginActivity.class);
                PreferenceManager.savePref(SplashActivity.this, "", CommonUtil.USER_CONTACT_ID);
                startActivity(intent);
                SplashActivity.this.finish();
            }

        }, 2000);
    }
}