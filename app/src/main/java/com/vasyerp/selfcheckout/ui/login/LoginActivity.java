package com.vasyerp.selfcheckout.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
//9726098261
import com.kaopiz.kprogresshud.KProgressHUD;
import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityLoginBinding;
import com.vasyerp.selfcheckout.repositories.LoginRegistrationRepository;
import com.vasyerp.selfcheckout.utils.CommonUtil;
import com.vasyerp.selfcheckout.utils.ConnectivityStatus;
import com.vasyerp.selfcheckout.utils.PreferenceManager;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModel;
import com.vasyerp.selfcheckout.viewmodels.login_registration.LoginRegistrationViewModelFactory;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;
    String TAG = LoginActivity.this.getClass().getSimpleName();
    KProgressHUD kProgressHUD;
    private boolean isInternetConnected;

    private int companyId;
    private int userId;
    private int branchId;
    String domainName;

    LoginRegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        companyId = Integer.parseInt(PreferenceManager.getCompanyId(this));
        branchId = Integer.parseInt(PreferenceManager.getBranchId(this));
        userId = Integer.parseInt(PreferenceManager.getUserId(this));

        domainName = PreferenceManager.getDomain(this);

        kProgressHUD = CommonUtil.getKProgressHud(LoginActivity.this);
        kProgressHUD.setCancellable(false);
        kProgressHUD.setDimAmount(0.25f);

        initViewModelAndRepository();

        ConnectivityStatus connectivityStatus = new ConnectivityStatus(LoginActivity.this);
        connectivityStatus.observe(this, aBoolean -> isInternetConnected = aBoolean);

        viewObserverCollection();

        loginBinding.btnLogin.setOnClickListener(v -> {
            if (isInternetConnected) {
                kProgressHUD.show();
                if (checkLoginValidation()) {
                    viewModel.userLoginCompanyApiCall(Objects.requireNonNull(loginBinding.etMobile.getText()).toString());
                }
                kProgressHUD.dismiss();
            } else {
                CommonUtil.showSnackBar(loginBinding.llLoginTerms, loginBinding.llLoginTerms, "Check internet Connection.");
            }
        });

        loginBinding.tvLoginToRegistration.setOnClickListener(v -> {
            Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intentRegistration);
            LoginActivity.this.finish();
        });

        loginBinding.etMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    loginBinding.tilMobile.setErrorEnabled(false);
                    loginBinding.tilMobile.setError(null);
                } else {
                    loginBinding.tilMobile.setErrorEnabled(true);
                    loginBinding.tilMobile.setError("Please, enter mobile no");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean checkLoginValidation() {
        if (Objects.requireNonNull(loginBinding.etMobile.getText()).toString().trim().isEmpty()) {
            loginBinding.tilMobile.setErrorEnabled(true);
            loginBinding.tilMobile.setError("Please, enter mobile no");
            loginBinding.etMobile.requestFocus();
            return false;
        } else if (!loginBinding.etMobile.getText().toString().trim().isEmpty() && loginBinding.etMobile.getText().toString().trim().length() != 10) {
            loginBinding.tilMobile.setErrorEnabled(true);
            loginBinding.tilMobile.setError("Please, enter 10 digit mobile no");
            loginBinding.etMobile.requestFocus();
            return false;
        } else {
            //if (!loginBinding.etMobile.getText().toString().isEmpty() && loginBinding.etMobile.getText().toString().length() == 10) {
            loginBinding.tilMobile.setErrorEnabled(false);
            loginBinding.tilMobile.setError(null);
            return true;
        }
    }

    private void initViewModelAndRepository() {
        Api apiInterface = ApiGenerator.getApi(CommonUtil.tempBaseUrl).create(Api.class);
        viewModel = new ViewModelProvider(this, new LoginRegistrationViewModelFactory(LoginRegistrationRepository.getInstance(apiInterface), companyId, branchId, userId)).get(LoginRegistrationViewModel.class);
    }

    private void viewObserverCollection() {
        viewModel.loginGetCustomerResponse.observe(this, loginCustomerDetails -> {
            if (loginCustomerDetails != null) {
                //Log.e(TAG, "onChanged: login response" + loginCustomerDetails.toString());
                PreferenceManager.savePref(this, loginCustomerDetails.getFirstName(), CommonUtil.USER_F_NAME);
                if (loginCustomerDetails.getLastName() != null) {
                    PreferenceManager.savePref(this, loginCustomerDetails.getLastName(), CommonUtil.USER_L_NAME);
                }
                PreferenceManager.savePref(this, loginCustomerDetails.getMobileNo(), CommonUtil.USER_MOBILE);
                if (loginCustomerDetails.getAddressLine1().trim().length() > 2) {
                    PreferenceManager.savePref(this, loginCustomerDetails.getAddressLine1(), CommonUtil.USER_ADDRESS);
                }
                PreferenceManager.savePref(this, String.valueOf(loginCustomerDetails.getContactId()), CommonUtil.USER_CONTACT_ID);

            } else {
                CommonUtil.showSnackBar(loginBinding.llLoginTerms, loginBinding.llLoginTerms, "User not found Create New Account.");
            }
        });

        viewModel.error.observe(this, s -> {
            if (s != null) {
                //user not found create new account
                String tempMsg = "No_Customer_Found";
                if (s.trim().equalsIgnoreCase(tempMsg.trim())) {
                    CommonUtil.showSnackBar(loginBinding.llLoginTerms, loginBinding.llLoginTerms, "User not found Create New Account.");
                } else {
                    CommonUtil.showSnackBar(loginBinding.llLoginTerms, loginBinding.llLoginTerms, "" + s);
                }

            }
        });

        viewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                kProgressHUD.show();
            } else {
                kProgressHUD.dismiss();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}