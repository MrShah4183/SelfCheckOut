package com.vasyerp.selfcheckout.models.savebill;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@Entity(foreignKeys = {@ForeignKey(entity = SaveBill.class,
        parentColumns = "responseSalesId",
        childColumns = "saveBillSalesId",
        onDelete = ForeignKey.CASCADE)})

public class SalesDTO {

    @PrimaryKey(autoGenerate = true)
    private int salesDtoId;

    private long saveBillSalesId;

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

    @Ignore
    public SalesDTO() {
    }

    public SalesDTO(int salesDtoId, long saveBillSalesId, long productVarientId, double qty, double mrp, long taxId, double taxRate, double taxAmount, double price, double netAmount, double landingCost, double sellingPrice, long batchId, String batchNo, double profit, int orderBy, String discountType, double discount, double mrpToDiscount, String mrpToDiscountTypeAdditional, String mrpToDiscountType, double mrpTodiscountAdditional, String discountTypeAdditional, double discountAdditional, String itemCode) {
        this.salesDtoId = salesDtoId;
        this.saveBillSalesId = saveBillSalesId;
        this.productVarientId = productVarientId;
        this.qty = qty;
        this.mrp = mrp;
        this.taxId = taxId;
        this.taxRate = taxRate;
        this.taxAmount = taxAmount;
        this.price = price;
        this.netAmount = netAmount;
        this.landingCost = landingCost;
        this.sellingPrice = sellingPrice;
        this.batchId = batchId;
        this.batchNo = batchNo;
        this.profit = profit;
        this.orderBy = orderBy;
        this.discountType = discountType;
        this.discount = discount;
        this.mrpToDiscount = mrpToDiscount;
        this.mrpToDiscountTypeAdditional = mrpToDiscountTypeAdditional;
        this.mrpToDiscountType = mrpToDiscountType;
        this.mrpTodiscountAdditional = mrpTodiscountAdditional;
        this.discountTypeAdditional = discountTypeAdditional;
        this.discountAdditional = discountAdditional;
        this.itemCode = itemCode;
    }
}
