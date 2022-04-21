package com.vasyerp.selfcheckout.api;

import com.vasyerp.selfcheckout.models.customer.City;
import com.vasyerp.selfcheckout.models.customer.Country;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;
import com.vasyerp.selfcheckout.models.customer.CustomerDetails;
import com.vasyerp.selfcheckout.models.customer.State;
import com.vasyerp.selfcheckout.models.login.LogIn;
import com.vasyerp.selfcheckout.models.login.CompanyCustomerBody;
import com.vasyerp.selfcheckout.models.orderlist.OrdersListResponse;
import com.vasyerp.selfcheckout.models.ordersummary.OrderSummary;
import com.vasyerp.selfcheckout.models.product.GetAllProducts;
import com.vasyerp.selfcheckout.models.product.Product;
import com.vasyerp.selfcheckout.models.savebill.SaveBill;
import com.vasyerp.selfcheckout.models.savebill.SaveBillStatusModel;
import com.vasyerp.selfcheckout.models.savebill.SaveBillResponse;
import com.vasyerp.selfcheckout.models.savebill.UpdateBillResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
    //"customerId":216174
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

    //http://192.168.175.38:8080/mpos/api/v2/allorders/?pageNo=0&limit=20&branchId=64&companyId=64&contactId=68856&ispaid=1
    //http://192.168.175.38:8080/mpos/api/v2/allorders/?pageNo=0&limit=20&branchId=64&companyId=64&contactId=68856&isunpaid=1
    @GET("mpos/api/v2/allorders/")
    Call<ApiResponse<OrdersListResponse>> getAllOrderList(
            @Query("pageNo") int pageNo,
            @Query("limit") int limit,
            @Query("branchId") String branchId,
            @Query("companyId") String companyId,
            @Query("contactId") int contactId);

    @GET("mpos/api/v2/allorders/")
    Call<ApiResponse<OrdersListResponse>> getAllPaidOrderList(
            @Query("pageNo") int pageNo,
            @Query("limit") int limit,
            @Query("branchId") String branchId,
            @Query("companyId") String companyId,
            @Query("contactId") int contactId,
            @Query("ispaid") int isPaid);

    @GET("mpos/api/v2/allorders/")
    Call<ApiResponse<OrdersListResponse>> getAllUnPaidOrderList(
            @Query("pageNo") int pageNo,
            @Query("limit") int limit,
            @Query("branchId") String branchId,
            @Query("companyId") String companyId,
            @Query("contactId") int contactId,
            @Query("isunpaid") int isUnPaid);

    /**
     * GetOrderSummary
     *
     * @param branchId
     * @param companyId
     * @param salesId
     * @return
     */
    @GET("/mpos/api/v2/orderdetails")
    Call<ApiResponse<OrderSummary>> getOrderSummary(
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Query("salesId") long salesId);

    @POST("/mpos/api/v2/savepayment")
    Call<UpdateBillResponse> updateSaveBillStatus(
            @Query("userId") int userId,
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Body SaveBillStatusModel saveBillStatusModel);

    /**
     * Save bill
     * http://192.168.2.20:8080/mpos/api/v2/savebill?userId=64&brachId=64&companyId=64
     *
     * @param userId
     * @param //brachId
     * @param companyId
     * @param saveBill
     * @return
     */
    /*@POST("mpos/api/v2/savebill")
    Call<ApiResponse<SaveBillResponse>> saveBill(
            @Query("userId") int userId,
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Body SaveBill saveBill);*/
    @POST("mpos/api/v2/savebillforselfcheckout")
    Call<ApiResponse<SaveBillResponse>> saveBill(
            @Query("userId") int userId,
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Body SaveBill saveBill);

    @GET("/mpos/api/v2/country")
    Call<ApiResponse<List<Country>>> getCountryResponse();

    @GET("/mpos/api/v2/state")
    Call<ApiResponse<List<State>>> getStateResponse(@Query("id") String id);

    @GET("/mpos/api/v2/city")
    Call<ApiResponse<List<City>>> getCityResponse(@Query("id") String id);

    @POST("/mpos/api/v2/createcontact")
    Call<ApiResponse<CustomerDetails>> addNewCustomer(
            @Query("branchId") int branchId,
            @Query("companyId") int companyId,
            @Query("userId") int userId,
            @Query("type") String type,
            @Body CreateCustomerBody customerBody);

    //http://192.168.175.38:8080/mpos/api/v2/allorders/?pageNo=0&limit=20&branchId=64&companyId=64&contactId=68856&ispaid=1
    //http://192.168.175.38:8080/mpos/api/v2/allorders/?pageNo=0&limit=20&branchId=64&companyId=64&contactId=68856&isunpaid=1

}