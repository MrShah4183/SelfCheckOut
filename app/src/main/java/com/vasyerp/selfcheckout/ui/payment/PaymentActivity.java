package com.vasyerp.selfcheckout.ui.payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vasyerp.selfcheckout.databinding.ActivityPaymentBinding;
import com.vasyerp.selfcheckout.ui.main.MainActivity;

public class PaymentActivity extends AppCompatActivity {
    ActivityPaymentBinding paymentBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        paymentBinding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(paymentBinding.getRoot());

        paymentBinding.btnPayment.setOnClickListener(v -> {
            Toast.makeText(PaymentActivity.this, "Payment Done", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}