package com.vasyerp.selfcheckout.models.product;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

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
public class ProductVarientsDTO {
    @Expose
    @SerializedName("productVarientId")
    private int productVarientId;

    @Expose
    @SerializedName("purchasePrice")
    private double purchasePrice;

    @Expose
    @SerializedName("mrp")
    private double mrp;

    @Expose
    @SerializedName("active")
    private int active;

    @Expose
    @SerializedName("qty")
    private String qty;

    @Expose
    @SerializedName("stockMasterVos")
    private List<StockMasterVo> stockMasterVos;

    @Expose
    @SerializedName("productName")
    private String productName;

    @Expose
    @SerializedName("taxId")
    private int taxId;

    @Expose
    @SerializedName("taxRate")
    private float taxRate;

    @Expose
    @SerializedName("taxIncluded")
    private int taxIncluded;

    @Expose
    @SerializedName("purchaseTaxIncluded")
    private int purchaseTaxIncluded;

    @Expose
    @SerializedName("discountType")
    private String discountType;

    @Expose
    @SerializedName("discount")
    private double discount;

    @Expose
    @SerializedName("purchaseTaxrate")
    private double purchaseTaxrate;

    private ProductStatus productStatus;

    private ProductDto productDto;

    @NonNull
    @Override
    public String toString(){
        return productName+" "+productVarientId;
    }
}
