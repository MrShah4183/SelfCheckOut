package com.vasyerp.selfcheckout.api;

import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.CompanyCustomerBody;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {

    //comapany loging using qr code scanning
    @POST("mpos/api/v2/get/companydetails")
    Call<ApiResponse<LogIn>> companyLogin(@Body CompanyCustomerBody companyCustomerBody);

    //get all products for edittext autocomplete
    //http://192.168.2.20:8080/mpos/api/v2/getproductdata?companyId=64
    @GET("mpos/api/v2/getproductdata")
    Call<ApiResponse<List<GetAllProducts>>> getAllProductList(@Query("companyId") String companyId);

    /**
     * Get product by barcode.
     * http://192.168.2.156:8080/mpos/api/v2/getproductbybarcode?branchId=64&barcode=12345678&companyId=64&yearinterval='2021-2022'
     *
     * @param branchId
     * @param barcode
     * @param companyId
     * @param yearinterval
     * @return
     */
    @GET("mpos/api/v2/getproductbybarcode")
    Call<ApiResponse<Product>> getProductByBarCode(
            @Query("branchId") String branchId,
            @Query("barcode") String barcode,
            @Query("companyId") String companyId,
            @Query("yearinterval") String yearinterval);

    /**
     * Get product by id.
     * http://192.168.2.20:8080/mpos/api/v2/getproductbyproductId?branchId=64&productVarientId=425841&companyId=64&yearinterval='2021-2022'
     *
     * @param branchId
     * @param productVarientId
     * @param companyId
     * @param yearinterval
     * @return
     */
    @GET("mpos/api/v2/getproductbyproductId")
    Call<ApiResponse<Product>> getProductByProductId(
            @Query("branchId") String branchId,
            @Query("productVarientId") String productVarientId,
            @Query("companyId") String companyId,
            @Query("yearinterval") String yearinterval);

}