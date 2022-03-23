package com.vasyerp.selfcheckout.models.razorpaymodel.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllOrdersModel {
    @SerializedName("entity")
    @Expose
    public String entity;
    @SerializedName("count")
    @Expose
    public String count;
    @SerializedName("items")
    @Expose
    public List<SingleOrderModel> singleOrderModelList;
}
