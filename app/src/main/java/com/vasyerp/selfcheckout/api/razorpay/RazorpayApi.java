package com.vasyerp.selfcheckout.api.razorpay;

import com.vasyerp.selfcheckout.models.razorpaymodel.order.PostOrderData;
import com.vasyerp.selfcheckout.models.razorpaymodel.order.SingleOrderModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RazorpayApi {

    @POST("orders")
    Call<SingleOrderModel> createOrder(
            @Body PostOrderData postOrderData,
            @Header("Authorization") String authKey
    );


}
