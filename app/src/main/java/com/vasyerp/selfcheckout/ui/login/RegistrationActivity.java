package com.vasyerp.selfcheckout.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.vasyerp.selfcheckout.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding registrationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(registrationBinding.getRoot());
    }
}