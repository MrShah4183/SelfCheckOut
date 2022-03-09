package com.vasyerp.selfcheckout.models.product;

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
public class PlaceOrder {
    @SerializedName("productVarientId")
    @Expose
    private int productVarientId;
    @SerializedName("qty")
    @Expose
    private int qty;
    @SerializedName("mrp")
    @Expose
    private float mrp;
    @SerializedName("taxId")
    @Expose
    private int taxId;
    @SerializedName("taxRate")
    @Expose
    private float taxRate;
    @SerializedName("taxAmount")
    @Expose
    private float taxAmount;
    @SerializedName("price")
    @Expose
    private float price;
    @SerializedName("netAmount")
    @Expose
    private float netAmount;
}
