package com.vasyerp.selfcheckout.models.ordersummary;

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
public class SalesItems {
    @Expose
    private long salesId;
    @Expose
    private int productVarientId;
    @Expose
    private double qty;
    @Expose
    private String itemCode;
    @Expose
    private double mrp;
    @Expose
    private double sellingPrice;
    @Expose
    private String discountType;
    @Expose
    private double taxRate;
    @Expose
    private double discount;
    @Expose
    private double netAmount;
    @Expose
    private float taxAmount;
    @Expose
    private long taxId;
    @Expose
    private double price;
    @Expose
    private String productName;
    @Expose
    private long productId;
    @Expose
    private long salesItemId;
    @Expose
    private String salesItemType;
    @Expose
    private String productDescription;
    @Expose
    private String discountTypeAdditional;
    @Expose
    private double discountAdditional;
    @Expose
    private double mrpTodiscountAdditional;
    @Expose
    private String mrpToDiscountTypeAdditional;
    @Expose
    private String mrpToDiscountType;
    @Expose
    private double landingCost;
    @Expose
    private double discount2;
    @Expose
    private String discountType2;
    @Expose
    @SerializedName("salesmanId")
    private int salesmanId;
    @Expose
    private double freeQty;
    @Expose
    private double cessAmount;
    @Expose
    private double cessRate;
    @Expose
    private double mrpToDiscount;
    @Expose
    private int isReturn;
    @Expose
    private double profit;
    @Expose
    private int orderBy;
    @Expose
    private String salesType;
    @Expose
    private int barcodeId;
    @Expose
    private String designNo;
    @Expose
    private String productVarientName;

}
