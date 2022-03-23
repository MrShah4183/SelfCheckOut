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
import com.vasyerp.selfcheckout.utils.PreferenceManager;

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
        //apiInterface = RazorpayApiGenerator.getSingle().create(Api.class);
        //kProgressHUD = CommonUtil.getProgressView(UserLoginActivity.this);
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

        userLoginBinding.btnLoginUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.savePref(UserLoginActivity.this, Objects.requireNonNull(userLoginBinding.etUserFirstName.getText()).toString(), CommonUtil.USER_F_NAME);
                PreferenceManager.savePref(UserLoginActivity.this, Objects.requireNonNull(userLoginBinding.etUserLastName.getText()).toString(), CommonUtil.USER_L_NAME);
                PreferenceManager.savePref(UserLoginActivity.this, Objects.requireNonNull(userLoginBinding.etUserMobile.getText()).toString(), CommonUtil.USER_MOBILE);
                PreferenceManager.savePref(UserLoginActivity.this, Objects.requireNonNull(userLoginBinding.etUserAddress.getText()).toString(), CommonUtil.USER_ADDRESS);
                Intent intent = new Intent(UserLoginActivity.this, CompanyLoginActivity.class);
                startActivity(intent);
                UserLoginActivity.this.finish();
            }
        });

        /*userLoginBinding.btnLoginUser.setOnClickListener(v -> {
            if (isInternetAvailable) {
                //if (checkLoginValidation()) {
                //todo check login value
                //checkLoginValue(userLoginBinding.etUserFirstName.getText().toString(), userLoginBinding.etUserMobile.getText().toString());
                Intent intent = new Intent(UserLoginActivity.this, CompanyLoginActivity.class);
                startActivity(intent);
                UserLoginActivity.this.finish();
                //}
            } else {
                CommonUtil.showSnackBar(userLoginBinding.getRoot(), userLoginBinding.getRoot(), "No, Internet available!!!");
            }
        });*/
    }

    private boolean checkLoginValidation() {
        boolean check;
        //todo set error remove listener
        if (Objects.requireNonNull(Objects.requireNonNull(userLoginBinding.etUserFirstName).getText()).toString().isEmpty()) {
            userLoginBinding.tilUserFirstName.setErrorEnabled(true);
            userLoginBinding.tilUserFirstName.setError("The Username is Required.");
            userLoginBinding.tilUserFirstName.setErrorIconDrawable(null);
            userLoginBinding.tilUserFirstName.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
            check = false;
        } else if (Objects.requireNonNull(userLoginBinding.etUserFirstName.getText()).toString().trim().length() < 3) {
            userLoginBinding.tilUserFirstName.setErrorEnabled(true);
            userLoginBinding.tilUserFirstName.setError("User name must be min 3 characters long.");
            userLoginBinding.tilUserFirstName.setErrorIconDrawable(null);
            userLoginBinding.tilUserFirstName.setBoxStrokeErrorColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red)));
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
            userLoginBinding.tilUserFirstName.setErrorEnabled(false);
            userLoginBinding.tilUserFirstName.setError(null);
            check = true;
        }
        return check;
    }
}