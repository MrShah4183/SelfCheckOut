package com.vasyerp.selfcheckout.ui.test;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ActivityTestBinding;
import com.vasyerp.selfcheckout.ui.main.MainActivity;
import com.vasyerp.selfcheckout.ui.payment.PaymentActivity;

public class TestActivity extends AppCompatActivity {
    ActivityTestBinding testBinding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testBinding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(testBinding.getRoot());

        testBinding.rGrpPayType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.radioOnline:
                    testBinding.llPaymentOptions.setVisibility(View.VISIBLE);
                    Toast.makeText(TestActivity.this, "Make Payment Online,\n Now, select Payment Options", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radioCounter:
                    Toast.makeText(TestActivity.this, "Make Payment At Counter", Toast.LENGTH_SHORT).show();
                    testBinding.llPaymentOptions.setVisibility(View.INVISIBLE);
                    testBinding.radioDebitCard.setChecked(false);
                    testBinding.radioUPI.setChecked(false);
                    testBinding.radioNetBanking.setChecked(false);
                    break;
            }
        });

        testBinding.rGrpPaymentOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (testBinding.radioOnline.isChecked()) {
                    switch (checkedId) {
                        case R.id.radioDebitCard:
                            Toast.makeText(TestActivity.this, "Make Payment Online using Debit Or Credit Card", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.radioUPI:
                            Toast.makeText(TestActivity.this, "Make Payment Online using UPI", Toast.LENGTH_SHORT).show();
                            break;
                        case R.id.radioNetBanking:
                            Toast.makeText(TestActivity.this, "Make Payment Online using Net Banking", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        });

        testBinding.btnCheckOut.setOnClickListener(v -> {
            //todo place order unpaid
            Intent intent;
            if (testBinding.radioOnline.isChecked())
                intent = new Intent(TestActivity.this, PaymentActivity.class);
            else
                intent = new Intent(TestActivity.this, MainActivity.class);

            startActivity(intent);
        });
    }
}