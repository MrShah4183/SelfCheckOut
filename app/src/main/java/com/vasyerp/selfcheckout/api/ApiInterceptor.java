package com.vasyerp.selfcheckout.api;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.BuildConfig;

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
        requestBuilder.addHeader("X-APIKEY", BuildConfig.X_API_KEY);
        requestBuilder.addHeader("AgentName", BuildConfig.AGENT_NAME);
        requestBuilder.addHeader("X-XSRF-TOKEN","");
        //requestBuilder.addHeader("XSRF-TOKEN", "88803e53-1c3d-4356-9c62-afccf4a2e107");
        return chain.proceed(requestBuilder.build());
    }
}
