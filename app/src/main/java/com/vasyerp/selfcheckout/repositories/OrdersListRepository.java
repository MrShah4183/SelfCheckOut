package com.vasyerp.selfcheckout.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.models.orderlist.OrdersListResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersListRepository {
    private Api api;
    private String TAG = "OrderListsRepository";

    public OrdersListRepository(Api api) {
        this.api = api;
    }

    public static OrdersListRepository getInstance(Api api) {
        return new OrdersListRepository(api);
    }

    public void getAllOrdersList(DataSource<OrdersListResponse> dataSource, int pageNo, int limit, String branchId, String companyId, int contactId) {
        //Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllOrderList(pageNo, limit, branchId, companyId,contactId);
        Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllOrderList(pageNo, limit, branchId, companyId, contactId);
        dataSource.loading(true);
        callAllOrdersList.enqueue(new Callback<ApiResponse<OrdersListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Response<ApiResponse<OrdersListResponse>> response) {
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
            public void onFailure(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void getAllPaidOrdersList(DataSource<OrdersListResponse> dataSource, int pageNo, int limit, String branchId, String companyId, int contactId) {
        //Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllOrderList(pageNo, limit, branchId, companyId,contactId);
        //Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllOrderList(pageNo, limit, branchId, companyId,contactId);
        Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllPaidOrderList(pageNo, limit, branchId, companyId, contactId, 1);
        dataSource.loading(true);
        callAllOrdersList.enqueue(new Callback<ApiResponse<OrdersListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Response<ApiResponse<OrdersListResponse>> response) {
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
            public void onFailure(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void getAllUnPaidOrdersList(DataSource<OrdersListResponse> dataSource, int pageNo, int limit, String branchId, String companyId, int contactId) {
        //Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllOrderList(pageNo, limit, branchId, companyId,contactId);
        Call<ApiResponse<OrdersListResponse>> callAllOrdersList = api.getAllUnPaidOrderList(pageNo, limit, branchId, companyId, contactId, 1);
        dataSource.loading(true);
        callAllOrdersList.enqueue(new Callback<ApiResponse<OrdersListResponse>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Response<ApiResponse<OrdersListResponse>> response) {
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
            public void onFailure(@NonNull Call<ApiResponse<OrdersListResponse>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }
}
