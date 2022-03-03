package com.vasyerp.selfcheckout.ui.test;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.databinding.ActivityTestBinding;
import com.vasyerp.selfcheckout.databinding.ActivityTestingBinding;

public class TestingActivity extends AppCompatActivity {
    private final String TAG = TestingActivity.this.getClass().getSimpleName();
    ActivityTestingBinding testingBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        testingBinding = ActivityTestingBinding.inflate(getLayoutInflater());
        setContentView(testingBinding.getRoot());

        /*BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = null;
        try {
            bitmap = barcodeEncoder.encodeBitmap(dynamicInvoice, BarcodeFormat.CODE_128, 500, 50);
            //bitmap = barcodeEncoder.encodeBitmap("POS1509", BarcodeFormat.QR_CODE, 200, 200);
        } catch (WriterException e) {
            e.printStackTrace();
        }*/

        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap("INV4183", BarcodeFormat.CODE_128, 500, 50);
            testingBinding.ivBarcode.setImageBitmap(bitmap);
            testingBinding.tvOrderNo.setText("INV4183");
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: some interrupt ");
        }

    }
}