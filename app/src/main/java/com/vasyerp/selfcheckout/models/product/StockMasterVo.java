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
public class StockMasterVo {

    @Expose
    @SerializedName("productVarientId")
    private long productVarientId;

    @Expose
    @SerializedName("stockId")
    private long stockId;

    @Expose
    @SerializedName("quantity")
    private String quantity;

    @Expose
    @SerializedName("batchNo")
    private String batchNo;

    @Expose
    @SerializedName("purchaseprice")
    private double purchaseprice;

    @Expose
    @SerializedName("mrp")
    private double mrp;

    @Expose
    @SerializedName("sellingPrice")
    private double sellingPrice;

    @Expose
    @SerializedName("landingcost")
    private double landingcost;

    @Expose
    @SerializedName("isDisable")
    private int isDisable;

    private double mrpToDiscount;

    private ProductStatus productStatus;

    private ProductDto productDto;

    private double displayMrp;

    private double totalTaxPrice;

    private double netMrp;

    private double taxPrice;

    private double profit;

    private double price;

    private double taxableAmount;

    private String oldQuantity;

    private int hasNegativeSelling;

    private String discountType;

    private double discount;
}
