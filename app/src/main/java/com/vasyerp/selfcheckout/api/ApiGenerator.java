package com.vasyerp.selfcheckout.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {
    private static Retrofit retrofit = null;
    public static final String BASE_URLs = "http://192.168.2.225:8283/";
    public static final String BASE_URL = "http://192.168.2.98:8080/";
    //public static final String BASE_URL = "http://testing2.vasyerp.com/";

    //mpos/api/v2/

    //http://192.168.2.20:8080/mpos/api/v2/domain/checkdomain?url=croods

    //192.168.2.20:8080/mpos/api/v2/domain/checkdomain?url=croodstest
    //192.168.2.20:8080/mpos/api/v2/check/login?username=vyaparerp&password=123456789&domain=croods.vasyerp.com
    //192.168.2.20:8080/mpos/api/v2/getproductbybarcode?branchId=64&barcode=12345678
    //192.168.2.20:8080/mpos/api/v2/getproductdata?companyId=64
    //192.168.2.20:8080/mpos/api/v2/getcustomer?companyId=64&type=customers&isDeleted=0&onSearch=7418529635


    public static Retrofit getSingle(String baseURL) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        //public static final String BASE_URL = "http://192.168.2.225:8282/";
        //http://192.168.2.225:8282/products?itemcode=3252010100004
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new ApiInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }

    public static Retrofit getSingle() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String baseURL = "http://192.168.2.225:8282/products?itemcode=3252010100004";
        //public static final String BASE_URL = "http://192.168.2.225:8282/";
        //http://192.168.2.225:8282/products?itemcode=3252010100004
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(new ApiInterceptor())
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}
