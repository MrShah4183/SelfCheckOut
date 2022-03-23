package com.vasyerp.selfcheckout.ui.payment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.vasyerp.selfcheckout.R;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApi;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApiAuthentication;
import com.vasyerp.selfcheckout.api.razorpay.RazorpayApiGenerator;
import com.vasyerp.selfcheckout.databinding.ActivityRazorpayPaymentBinding;
import com.vasyerp.selfcheckout.models.razorpaymodel.order.PostOrderData;
import com.vasyerp.selfcheckout.models.razorpaymodel.order.SingleOrderModel;

import org.json.JSONObject;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RazorpayPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    private final String TAG = RazorpayPaymentActivity.this.getClass().getSimpleName();
    ActivityRazorpayPaymentBinding razorpayPaymentBinding;
    PostOrderData postOrderData;
    RazorpayApi apiInterface;
    static String baseUrl = "https://api.razorpay.com/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        razorpayPaymentBinding = ActivityRazorpayPaymentBinding.inflate(getLayoutInflater());
        setContentView(razorpayPaymentBinding.getRoot());

        apiInterface = RazorpayApiGenerator.getApi(baseUrl).create(RazorpayApi.class);

        razorpayPaymentBinding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrderData = new PostOrderData();
                Random random = new Random();
                int totalAmount = random.nextInt(999999 - 10000) + 10000;
                //int intAmount = Math.round(Float.parseFloat(strAmount) * 100);
                //totalAmount = Math.round(Float.parseFloat(String.valueOf(totalAmount)) * 100);
                postOrderData.setAmount((double) totalAmount);
                postOrderData.setCurrency("INR");
                int no = random.nextInt(999 - 100) + 100;
                postOrderData.setReceipt("OrderNo" + no);

                Call<SingleOrderModel> callCreateOrder = apiInterface.createOrder(
                        postOrderData,
                        RazorpayApiAuthentication.getAuthToken()
                );

                callCreateOrder.enqueue(new Callback<SingleOrderModel>() {
                    @Override
                    public void onResponse(@NonNull Call<SingleOrderModel> call, @NonNull Response<SingleOrderModel> response) {
                        if (response.isSuccessful()) {
                            Log.e(TAG, "onResponse: order created");
                            assert response.body() != null;
                            Log.e(TAG, "onResponse: order data" + response.body().getId());
                            Log.e(TAG, "onResponse: order data" + response.body().toString());
                            //orderId = response.body().getId();

                            Toast.makeText(RazorpayPaymentActivity.this, "Your Payment Process Is Started", Toast.LENGTH_SHORT).show();

                            new Handler().postDelayed(() -> {
                                Log.e(TAG, "run: payment start");
                                makePayments(response.body());
                            }, 2000);

                        } else {
                            Log.e(TAG, "onResponse: order create fail");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SingleOrderModel> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void makePayments(SingleOrderModel singleOrderModel) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID("rzp_test_goYqawczzlm2Yp");


        try {
            JSONObject options = new JSONObject();
            options.put("name", "My Shah");
            options.put("description", "Checking Order API ");
            options.put("image", "https://i.postimg.cc/sxc8sSTj/IMG-20200929-WA0000.jpg");
            options.put("currency", singleOrderModel.getCurrency());
            options.put("amount", singleOrderModel.getAmount());
            options.put("order_id", singleOrderModel.getId());
            //notifyModel.setEmail(true);
            //        notifyModel.setSms(true);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "shahmeeto1o82o@gmail.com");
            preFill.put("contact", "7575061808");
            options.put("prefill", preFill);

            JSONObject notify = new JSONObject();
            notify.put("sms", true);
            notify.put("email", true);
            options.put("notify", notify);

            options.put("reminder_enable", true);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        razorpayPaymentBinding.tv.setText("Successful payment ID :" + s);

        /*cnpwX3Rlc3RfZ29ZcWF3Y3p6bG0yWXA6U09oc3pENkdZdGVVOFpObk5GVHdwcE51
                cnpwX3Rlc3RfZ29ZcWF3Y3p6bG0yWXA6U09oc3pENkdZdGVVOFpObk5GVHdwcE51*/
    }

    @Override
    public void onPaymentError(int i, String s) {
        razorpayPaymentBinding.tv.setText("Failed and cause is :" + s);
        Toast.makeText(RazorpayPaymentActivity.this, "Fail some reason.", Toast.LENGTH_SHORT).show();
    }
}