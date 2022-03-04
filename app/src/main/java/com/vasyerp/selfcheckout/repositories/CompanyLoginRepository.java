package com.vasyerp.selfcheckout.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.vasyerp.selfcheckout.api.Api;
import com.vasyerp.selfcheckout.api.ApiResponse;
import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.LoginBody;
import com.vasyerp.selfcheckout.ui.company.CompanyLoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyLoginRepository {
    private Api api;
    private String TAG = "CompanyLoginRepository";
    /*private GajanandDao gajanandDao;
    public HomeRepository(Api api,GajanandDao gajanandDao) {
        this.api = api;
        this.gajanandDao = gajanandDao;
    }*/
    /*public static CompanyLoginActivity getInstance(Api api, GajanandDao gajanandDao) {
        return new CompanyLoginActivity(api,gajanandDao);
    }*/

    public void companyLoginFromRemote(DataSource<LogIn> dataSource, LoginBody loginBody) {
        Call<ApiResponse<LogIn>> callCompanyLogin = api.companyLogin(loginBody);
        dataSource.loading(true);
        callCompanyLogin.enqueue(new Callback<ApiResponse<LogIn>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<LogIn>> call, @NonNull Response<ApiResponse<LogIn>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResponse() != null) {
                            dataSource.data(response.body().getResponse());
                            //insertPositionProductVo(new PositionProductVo(positionProductVo.getProductVarientsVo().getProductVarientId(),positionProductVo.getProductVarientsVo(),positionProductVo.getPosition(),positionProductVo.getType(),userFrontId));
                            //todo set up later insertInDB();
                        } else {
                            Log.d(TAG, "onResponse: response is null.");
                            dataSource.loading(false);
                            dataSource.data(null);
                            dataSource.error("Response is null.");
                        }
                        dataSource.data(response.body().getResponse());
                    } else {
                        Log.d(TAG, "onResponse: response body null.");
                        dataSource.loading(false);
                        dataSource.data(null);
                        dataSource.error("Response body is null.");
                    }
                } else {
                    //handle some error
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<LogIn>> call, @NonNull Throwable t) {

            }
        });
    }
}
