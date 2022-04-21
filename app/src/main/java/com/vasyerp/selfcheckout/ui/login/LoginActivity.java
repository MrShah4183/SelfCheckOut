package com.vasyerp.selfcheckout.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vasyerp.selfcheckout.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding loginBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        loginBinding.btnLogin.setOnClickListener(v -> {
            /*Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intentRegistration);
            LoginActivity.this.finish();*/
            Toast.makeText(LoginActivity.this, "Login Done", Toast.LENGTH_SHORT).show();
        });

        loginBinding.tvLoginToRegistration.setOnClickListener(v -> {
            Intent intentRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intentRegistration);
            LoginActivity.this.finish();
        });


    }
}