package com.vasyerp.selfcheckout.api;

import androidx.annotation.NonNull;

//import com.scan.myscanner.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiInterceptor implements Interceptor {
    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder requestBuilder = chain.request().newBuilder();
        requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8");
        requestBuilder.addHeader("Transfer-Encoding", "chunked");
        /*requestBuilder.addHeader("X-APIKEY", BuildConfig.X_API_KEY);
        requestBuilder.addHeader("AgentName", BuildConfig.AGENT_NAME);*/
        return chain.proceed(requestBuilder.build());
    }
}
