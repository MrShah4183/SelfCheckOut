package com.vasyerp.selfcheckout.models.orderlist;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersListResponse {
    @SerializedName("data")
    @Expose
    private List<OrderModel> data = null;
    @SerializedName("total")
    @Expose
    private int total;
    @SerializedName("pageNo")
    @Expose
    private int pageNo;
    @SerializedName("limit")
    @Expose
    private int limit;
}
