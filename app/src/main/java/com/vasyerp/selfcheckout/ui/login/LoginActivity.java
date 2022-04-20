package com.vasyerp.selfcheckout.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vasyerp.selfcheckout.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
    }
}