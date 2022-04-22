package com.vasyerp.selfcheckout.repositories;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.models.customer.City;
import com.vasyerp.selfcheckout.models.customer.Country;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;
import com.vasyerp.selfcheckout.models.customer.CustomerDetails;
import com.vasyerp.selfcheckout.models.customer.LoginCustomerDetails;
import com.vasyerp.selfcheckout.models.customer.State;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginRegistrationRepository {
    private final Api api;
    private final String TAG = "LoginRegistrationRepository";

    public LoginRegistrationRepository(Api api) {
        this.api = api;
    }

    public static LoginRegistrationRepository getInstance(Api api) {
        return new LoginRegistrationRepository(api);
    }

    public void getCountryList(DataSource<List<Country>> dataSource) {
        dataSource.loading(true);
        api.getCountryResponse().enqueue(new Callback<ApiResponse<List<Country>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Country>>> call, @NonNull Response<ApiResponse<List<Country>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(null);
                        } else {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(response.body().getMessage());
                        }
                    }
                } else {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Country>>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void getStateList(DataSource<List<State>> dataSource, String countryId) {
        dataSource.loading(true);
        api.getStateResponse(countryId).enqueue(new Callback<ApiResponse<List<State>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<State>>> call, @NonNull Response<ApiResponse<List<State>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(null);
                        } else {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(response.body().getMessage());
                        }
                    }
                } else {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<State>>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void getCityList(DataSource<List<City>> dataSource, String stateId) {
        dataSource.loading(true);
        api.getCityResponse(stateId).enqueue(new Callback<ApiResponse<List<City>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<City>>> call, @NonNull Response<ApiResponse<List<City>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(null);
                        } else {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(response.body().getMessage());
                        }
                    }
                } else {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<City>>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void customerRegistration(DataSource<CustomerDetails> dataSource, int branchId, int companyId, int userId, String type, CreateCustomerBody customerBody) {
        dataSource.loading(true);
        api.addNewCustomer(branchId, companyId, userId, type, customerBody).enqueue(new Callback<ApiResponse<CustomerDetails>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<CustomerDetails>> call, @NonNull Response<ApiResponse<CustomerDetails>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(null);
                        } else {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(response.body().getMessage());
                        }
                    }
                } else {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<CustomerDetails>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void userLoginInCompany(DataSource<LoginCustomerDetails> dataSource, int branchId, int companyId, String mobileNo) {
        dataSource.loading(true);
        api.userLoginInCompany(branchId, companyId, mobileNo).enqueue(new Callback<ApiResponse<LoginCustomerDetails>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LoginCustomerDetails>> call, @NonNull Response<ApiResponse<LoginCustomerDetails>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().isStatus()) {
                            if (response.body().getResponse().getActive() == 0) {
                                dataSource.data(response.body().getResponse());
                                dataSource.loading(false);
                                dataSource.error(null);
                            } else if (response.body().getResponse().getActive() != 0) {
                                dataSource.data(null);
                                dataSource.loading(false);
                                dataSource.error("User activation left, Please contact admin.");
                            }
                        } else {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(response.body().getMessage());
                        }
                    }
                } else {
                    dataSource.loading(false);
                    dataSource.data(null);
                    dataSource.error("Response fail.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LoginCustomerDetails>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }
}
