package com.vasyerp.selfcheckout.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.models.ordersummary.OrderSummary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSummaryRepository {
    private Api api;
    private String TAG = "OrderSummaryRepository";

    public OrderSummaryRepository(Api api) {
        this.api = api;
    }

    public static OrderSummaryRepository getInstance(Api api) {
        return new OrderSummaryRepository(api);
    }

    /*Call<ApiResponse<OrderSummary>> getOrderSummary(
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Query("salesId") int salesId);*/

    public void getOrderSummary(DataSource<OrderSummary> dataSource, int branchId, int companyId, long salesId) {
        Call<ApiResponse<OrderSummary>> callOrderSummary = api.getOrderSummary(branchId, companyId, salesId);
        dataSource.loading(true);
        callOrderSummary.enqueue(new Callback<ApiResponse<OrderSummary>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OrderSummary>> call, @NonNull Response<ApiResponse<OrderSummary>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse() != null) {
                            if (response.body().isStatus()) {
                                dataSource.data(response.body().getResponse());
                                dataSource.loading(false);
                                dataSource.error(null);
                            } else {
                                dataSource.data(response.body().getResponse());
                                dataSource.loading(false);
                                dataSource.error(response.message());
                            }
                        } else {
                            Log.d(TAG, "onResponse: response is null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("no list available");
                        }
                        dataSource.data(response.body().getResponse());
                    } else {
                        Log.d(TAG, "onResponse: response body null.");
                        dataSource.loading(false);
                        dataSource.data(null);
                        dataSource.error("Response body is null.");
                    }
                } else {
                    Log.d(TAG, "onResponse: response fail.");
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<OrderSummary>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }
}
