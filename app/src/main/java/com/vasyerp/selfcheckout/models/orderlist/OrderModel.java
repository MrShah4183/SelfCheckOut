package com.vasyerp.selfcheckout.models.orderlist;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    @SerializedName("salesId")
    @Expose
    private int salesId;
    @SerializedName("total")
    @Expose
    private float total;
    @SerializedName("contactId")
    @Expose
    private int contactId;
    @SerializedName("salesDate")
    @Expose
    private String salesDate;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("salesNo")
    @Expose
    private int salesNo;
    @SerializedName("paidAmount")
    @Expose
    private float paidAmount;
    @SerializedName("prefix")
    @Expose
    private String prefix;
}
