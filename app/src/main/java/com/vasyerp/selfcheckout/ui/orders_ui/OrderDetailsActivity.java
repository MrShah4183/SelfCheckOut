package com.vasyerp.selfcheckout.ui.orders_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.adapters.OrderDetailsAdapter;
import com.vasyerp.selfcheckout.databinding.ActivityOrderDetailsBinding;
import com.vasyerp.selfcheckout.models.OrderDetailsModel;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {

    private final String TAG = OrderDetailsActivity.this.getClass().getSimpleName();
    ActivityOrderDetailsBinding orderDetailsBinding;
    ArrayList<OrderDetailsModel> orderDetailsModelArrayList;
    OrderDetailsModel orderDetailsModel;
    OrderDetailsAdapter orderDetailsAdapter;
    int viewQrCode;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderDetailsBinding = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        setContentView(orderDetailsBinding.getRoot());

        Intent intent = getIntent();
        viewQrCode = intent.getIntExtra("checkStatus", 0);
        if (viewQrCode == 1) {
            orderDetailsBinding.rlBarcodeView.setVisibility(View.VISIBLE);
            setQrCode();
        } else {
            orderDetailsBinding.rlBarcodeView.setVisibility(View.GONE);
            orderDetailsBinding.tvStatus.setText("Paid");
            Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            DrawableCompat.setTint(buttonDrawable, Color.rgb(3, 60, 6));
            orderDetailsBinding.tvStatus.setBackground(buttonDrawable);
        }


        orderDetailsModelArrayList = new ArrayList<>();
        setOrderDetailsData();
        orderDetailsAdapter = new OrderDetailsAdapter(this, orderDetailsModelArrayList);
        orderDetailsBinding.rvOrderDetails.setAdapter(orderDetailsAdapter);
        //orderDetailsAdapter.notifyDataSetChanged();

    }

    @SuppressLint("ResourceAsColor")
    private void setQrCode() {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        try {
            Bitmap bitmap = barcodeEncoder.encodeBitmap("INV4183", BarcodeFormat.CODE_128, 700, 150);
            orderDetailsBinding.tvStatus.setText("Unpaid");
            Drawable buttonDrawable = orderDetailsBinding.tvStatus.getBackground();
            buttonDrawable = DrawableCompat.wrap(buttonDrawable);
            //the color is a direct color int and not a color resource
            //DrawableCompat.setTint(buttonDrawable, R.color.red);
            DrawableCompat.setTint(buttonDrawable, Color.rgb(255, 0, 0));
            orderDetailsBinding.tvStatus.setBackground(buttonDrawable);

            /*orderDetailsBinding.tvStatus.setBackgroundColor(ContextCompat.getColor(OrderDetailsActivity.this, R.color.red));
            orderDetailsBinding.tvStatus.setBackgroundResource(R.drawable.et_bg);*/
            //orderDetailsBinding.tvStatus.setBackgroundTintList(ColorStateList.valueOf(R.color.red));
            orderDetailsBinding.ivBarcode.setImageBitmap(bitmap);
            orderDetailsBinding.tvOrderNo.setText("INV4183");
        } catch (WriterException e) {
            e.printStackTrace();
            //orderDetailsBinding.tvStatus.setBackground(ContextCompat.getColor(OrderDetailsActivity.this, R.color.offwhite));
            //orderDetailsBinding.tvStatus.setTin
            Log.e(TAG, "onCreate: some interrupt ");
        }
    }

    private void setOrderDetailsData() {
        orderDetailsModel = new OrderDetailsModel(
                "product 1",
                1.0,
                0.0,
                10.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 2",
                2.0,
                0.0,
                20.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 3",
                3.0,
                0.0,
                30.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);

        orderDetailsModel = new OrderDetailsModel(
                "product 4",
                4.0,
                0.0,
                40.0
        );
        orderDetailsModelArrayList.add(orderDetailsModel);
    }
}