package com.vasyerp.selfcheckout.models.savebill;

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
public class SalesDTO {
    @Expose
    @SerializedName("productVarientId")
    private long productVarientId;

    @Expose
    @SerializedName("qty")
    private double qty;

    @Expose
    @SerializedName("mrp")
    private double mrp;

    @Expose
    @SerializedName("taxId")
    private long taxId;

    @Expose
    @SerializedName("taxRate")
    private double taxRate;

    @Expose
    @SerializedName("taxAmount")
    private double taxAmount;

    @Expose
    @SerializedName("price")
    private double price;

    @Expose
    @SerializedName("netAmount")
    private double netAmount;

    @Expose
    @SerializedName("landingCost")
    private double landingCost;

    @Expose
    @SerializedName("sellingPrice")
    private double sellingPrice;

    @Expose
    @SerializedName("batchId")
    private long batchId;

    @Expose
    @SerializedName("batchNo")
    private String batchNo;

    @Expose
    @SerializedName("profit")
    private double profit;

    @Expose
    @SerializedName("orderBy")
    private int orderBy;

    @Expose
    @SerializedName("discountType")
    private String discountType;

    @Expose
    @SerializedName("discount")
    private double discount;

    @Expose
    @SerializedName("mrpToDiscount")
    private double mrpToDiscount;

    @Expose
    @SerializedName("mrpToDiscountTypeAdditional")
    private String mrpToDiscountTypeAdditional;

    @Expose
    @SerializedName("mrpToDiscountType")
    private String mrpToDiscountType;

    @Expose
    @SerializedName("mrpTodiscountAdditional")
    private double mrpTodiscountAdditional;

    @Expose
    @SerializedName("discountTypeAdditional")
    private String discountTypeAdditional;

    @Expose
    @SerializedName("discountAdditional")
    private double discountAdditional;

    @Expose
    @SerializedName("itemCode")
    private String itemCode;
}
