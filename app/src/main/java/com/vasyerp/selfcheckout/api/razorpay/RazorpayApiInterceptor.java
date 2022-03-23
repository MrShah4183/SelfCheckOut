package com.vasyerp.selfcheckout.api.razorpay;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RazorpayApiInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.addHeader("Content-Type", "application/json");
        //requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
        requestBuilder.addHeader("Transfer-Encoding", "chunked");
        /*requestBuilder.addHeader("X-APIKEY", BuildConfig.X_API_KEY);
        requestBuilder.addHeader("AgentName", BuildConfig.AGENT_NAME);*/
        requestBuilder.addHeader("User-Agent","PostmanRuntime/7.29.0");
        requestBuilder.addHeader("Accept","*/*");
        requestBuilder.addHeader("Accept-Encoding","gzip, deflate, br");
        requestBuilder.addHeader("Connection","keep-alive");
        requestBuilder.addHeader("User-Agent","PostmanRuntime/7.29.0");


        //requestBuilder.addHeader("Authorization","rzp_test_goYqawczzlm2Yp");
        return chain.proceed(requestBuilder.build());
    }
}
