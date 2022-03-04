package com.vasyerp.selfcheckout.api;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.LoginBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    //comapany loging using qr code scanning
    @POST("mpos/api/v2/get/companydetails")
    Call<ApiResponse<LogIn>> companyLogin(@Body LoginBody loginBody);

    /**
     * check domain name
     * http://192.168.2.20:8080/mpos/api/v2/domain/checkdomain?url=croods
     * @param url
     * @return
     */
    /*@GET("mpos/api/v2/domain/checkdomain")
    Call<ApiResponse<Object>> checkDomain(@Query("url") String url);*/

    /**
     * check user login
     * http://192.168.2.20:8080/mpos/api/v2/check/login?username=vyaparerp&password=123456789&domain=croods.vasyerp.com
     * @param loginBody
     * @param domaina
     * @return
     */
    /*@POST("mpos/api/v2/check/login")
    Call<ApiResponse<LogIn>> checkLogins(
            @Body LoginBody loginBody,
            @Query("domain") String domaina);*/

    /*@POST("mpos/api/v2/check/login")
    Call<ApiResponse<LogIn>> oldCheckLogin(
            @Query("username") String username,
            @Query("password") String password,
            @Query("domain") String domaina);*/
    /**
     * Get product by id.
     * http://192.168.2.20:8080/mpos/api/v2/getproductbyproductId?branchId=64&productVarientId=425841&companyId=64&yearinterval='2021-2022'
     * @param branchId
     * @param productVarientId
     * @param companyId
     * @param yearinterval
     * @return
     */
    /*@GET("mpos/api/v2/getproductbyproductId")
    Call<ApiResponse<Product>> getProductByProductId(
            @Query("branchId") String branchId,
            @Query("productVarientId") String productVarientId,
            @Query("companyId") String companyId,
            @Query("yearinterval") String yearinterval);*/

}