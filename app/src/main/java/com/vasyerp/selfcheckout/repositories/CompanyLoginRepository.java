package com.vasyerp.selfcheckout.repositories;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.db.SelfCheckOutDB;
import com.vasyerp.selfcheckout.db.SelfCheckOutDao;
import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.CompanyCustomerBody;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyLoginRepository {
    private Api api;
    private String TAG = "CompanyLoginRepository";
    private SelfCheckOutDao selfCheckOutDao;
    private int tempId = -1;

    public CompanyLoginRepository(Api api, SelfCheckOutDao selfCheckOutDao) {
        this.api = api;
        this.selfCheckOutDao = selfCheckOutDao;
    }

    public static CompanyLoginRepository getInstance(Api api, SelfCheckOutDao selfCheckOutDao) {
        return new CompanyLoginRepository(api, selfCheckOutDao);
    }

    public void companyLoginFromRemote(DataSource<LogIn> dataSource, CompanyCustomerBody companyCustomerBody) {
        Call<ApiResponse<LogIn>> callCompanyLogin = api.companyLogin(companyCustomerBody);
        dataSource.loading(true);
        callCompanyLogin.enqueue(new Callback<ApiResponse<LogIn>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LogIn>> call, @NonNull Response<ApiResponse<LogIn>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse() != null) {
                            dataSource.data(response.body().getResponse());
                            dataSource.loading(false);
                            dataSource.error(null);
                            Log.e(TAG, "onResponse: call set db data");
                            addLoginData(response.body().getResponse());
                        } else {
                            Log.d(TAG, "onResponse: response is null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("Invalid store QR Code.");
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
            public void onFailure(@NonNull Call<ApiResponse<LogIn>> call, @NonNull Throwable t) {
                dataSource.loading(false);
                dataSource.data(null);
                dataSource.error("fail to connect.");
            }
        });
    }

    public void addLoginData(LogIn login) {
        Log.e("TAG", "companyLogin: call repo addLoginData method");
        SelfCheckOutDB.databaseWriteExecutor.execute(() -> tempId = selfCheckOutDao.checkDataIsExist(login.getBranchId(), login.getCompanyId()));
        Log.e("TAG", "companyLogin: " + tempId);
        new Handler().postDelayed(() -> {
            Log.e(TAG, "addLoginData: " + tempId);
            if (tempId != -1) {
                Log.e("TAG", "companyLogin: call repo addLoginData method if condition");
                login.setTempId(tempId);
                SelfCheckOutDB.databaseWriteExecutor.execute(() -> selfCheckOutDao.addSingleStore(login));
            } else {
                Log.e("TAG", "companyLogin: call repo addLoginData method else condition");
                SelfCheckOutDB.databaseWriteExecutor.execute(() -> selfCheckOutDao.addSingleStore(login));
            }
        }, 1500);
        /*GajanandDB.databaseWriteExecutor.execute(()->{
            gajanandDao.addStates(states);
        });*/
    }

}
