package com.vasyerp.selfcheckout.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityUserLoginBinding;
import com.vasyerp.selfcheckout.ui.company.CompanyLoginActivity;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;

import java.util.Objects;
import java.util.regex.Pattern;

public class UserLoginActivity extends AppCompatActivity {
    ActivityUserLoginBinding userLoginBinding;
    Api apiInterface;
    KProgressHUD kProgressHUD;
    boolean isInternetAvailable;
    String numberPatternStr = "^\\d+$";
    String numberLengthRegex = "^.{10}$";
    Pattern mobileNumberPattern, numberLengthReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userLoginBinding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        setContentView(userLoginBinding.getRoot());
        //apiInterface = ApiGenerator.getSingle().create(Api.class);
        kProgressHUD = CommonUtil.getProgressView(UserLoginActivity.this);
        numberLengthReg = Pattern.compile(numberLengthRegex, Pattern.DOTALL);
        mobileNumberPattern = Pattern.compile(numberPatternStr, Pattern.DOTALL);

        ConnectivityStatus status = new ConnectivityStatus(this);
        status.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isInternetAvailable = aBoolean;
                if (!aBoolean) {
                    userLoginBinding.setInternetMessage("No, Internet connection!!!");
                    userLoginBinding.setIsInternetAvailable(false);
                    userLoginBinding.noInternetConnection.setBackgroundColor(ContextCompat.getColor(UserLoginActivity.this, R.color.red));
                    userLoginBinding.noInternetConnection.setTextColor(ContextCompat.getColor(UserLoginActivity.this, R.color.dark_red));
                } else {
                    userLoginBinding.setInternetMessage("Internet is connected.");
                    userLoginBinding.noInternetConnection.setBackgroundColor(ContextCompat.getColor(UserLoginActivity.this, R.color.green));
                    userLoginBinding.noInternetConnection.setTextColor(ContextCompat.getColor(UserLoginActivity.this, R.color.dark_green));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            userLoginBinding.setIsInternetAvailable(true);
                        }
                    }, 1000);
                }
            }
        });

        userLoginBinding.btnLoginUser.setOnClickListener(v -> {
            if (isInternetAvailable) {
                if (checkLoginValidation()) {
                    //todo post data into api
                    //checkLoginValue(userLoginBinding.etUserName.getText().toString(), userLoginBinding.etUserMobile.getText().toString());
                    Intent intent = new Intent(UserLoginActivity.this, CompanyLoginActivity.class);
                    startActivity(intent);
                    UserLoginActivity.this.finish();
                }
            } else {
                CommonUtil.showSnackBar(userLoginBinding.getRoot(), userLoginBinding.getRoot(), "No, Internet available!!!");
            }
        });
    }

    private boolean checkLoginValidation() {
        boolean check;
        //todo set error remove listener
        if (Objects.requireNonNull(Objects.requireNonNull(userLoginBinding.etUserName).getText()).toString().isEmpty()) {
            userLoginBinding.tilUserName.setErrorEnabled(true);
            userLoginBinding.tilUserName.setError("The Username is Required.");
            userLoginBinding.tilUserName.setErrorIconDrawable(null);
            userLoginBinding.tilUserName.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        } else if (Objects.requireNonNull(userLoginBinding.etUserName.getText()).toString().trim().length() < 3) {
            userLoginBinding.tilUserName.setErrorEnabled(true);
            userLoginBinding.tilUserName.setError("User name must be min 3 characters long.");
            userLoginBinding.tilUserName.setErrorIconDrawable(null);
            userLoginBinding.tilUserName.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        } else if (Objects.requireNonNull(userLoginBinding.etUserMobile.getText()).toString().isEmpty()) {
            userLoginBinding.tilUserMobile.setErrorEnabled(true);
            userLoginBinding.tilUserMobile.setError("Mobile No. is Required.");
            userLoginBinding.tilUserMobile.setErrorIconDrawable(null);
            userLoginBinding.tilUserMobile.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        } else if (!numberLengthReg.matcher(userLoginBinding.etUserMobile.getText().toString()).matches()) {
            userLoginBinding.tilUserMobile.setErrorEnabled(true);
            userLoginBinding.tilUserMobile.setError("Mobile No. must be 10 digit.");
            userLoginBinding.tilUserMobile.setErrorIconDrawable(null);
            userLoginBinding.tilUserMobile.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        } else if (!mobileNumberPattern.matcher(userLoginBinding.etUserMobile.getText().toString()).matches()) {
            userLoginBinding.tilUserMobile.setErrorEnabled(true);
            userLoginBinding.tilUserMobile.setError("Mobile No. must not contains the special characters.");
            userLoginBinding.tilUserMobile.setErrorIconDrawable(null);
            userLoginBinding.tilUserMobile.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        }

            /*if (!(userLoginBinding.etUserMobile.getText().toString().trim().length() == 10)) {
            userLoginBinding.tilUserMobile.setErrorEnabled(true);
            userLoginBinding.tilUserMobile.setError("Mobile No. must be 10 digit.");
            userLoginBinding.tilUserMobile.setErrorIconDrawable(null);
            userLoginBinding.tilUserMobile.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        }*/
        else {
            userLoginBinding.tilUserMobile.setError(null);
            userLoginBinding.tilUserMobile.setErrorEnabled(false);
            userLoginBinding.tilUserName.setErrorEnabled(false);
            userLoginBinding.tilUserName.setError(null);
            check = true;
        }
        return check;
    }
}