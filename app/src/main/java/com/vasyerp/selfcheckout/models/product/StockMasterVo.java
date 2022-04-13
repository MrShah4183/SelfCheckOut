package com.vasyerp.selfcheckout.models.product;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(primaryKeys = {"productVarientId", "branchId", "companyId"})
@Keep
@Data
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

    @Ignore
    private ProductStatus productStatus;

    @Ignore
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

    private String itemCode;

    private int companyId;
    private int branchId;
    private int userFrontId;
    private String productName;

    @Ignore
    public StockMasterVo() {
    }

    public StockMasterVo(long productVarientId, long stockId, String quantity, String batchNo, double purchaseprice, double mrp, double sellingPrice, double landingcost, int isDisable, double mrpToDiscount, double displayMrp, double totalTaxPrice, double netMrp, double taxPrice, double profit, double price, double taxableAmount, String oldQuantity, int hasNegativeSelling, String discountType, double discount, String itemCode, int companyId, int branchId, int userFrontId) {
        this.productVarientId = productVarientId;
        this.stockId = stockId;
        this.quantity = quantity;
        this.batchNo = batchNo;
        this.purchaseprice = purchaseprice;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.landingcost = landingcost;
        this.isDisable = isDisable;
        this.mrpToDiscount = mrpToDiscount;
        this.displayMrp = displayMrp;
        this.totalTaxPrice = totalTaxPrice;
        this.netMrp = netMrp;
        this.taxPrice = taxPrice;
        this.profit = profit;
        this.price = price;
        this.taxableAmount = taxableAmount;
        this.oldQuantity = oldQuantity;
        this.hasNegativeSelling = hasNegativeSelling;
        this.discountType = discountType;
        this.discount = discount;
        this.itemCode = itemCode;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userFrontId = userFrontId;
    }
}
