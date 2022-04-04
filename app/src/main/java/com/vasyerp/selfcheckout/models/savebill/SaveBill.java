package com.vasyerp.selfcheckout.models.savebill;

import androidx.annotation.Keep;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Keep
@Data
public class SaveBill {

    @PrimaryKey
    private int salesId;

    @Embedded(prefix = "saveBillResponse")
    private SaveBillResponse saveBillResponse;

    @Expose
    @SerializedName("customerId")
    private long customerId;

    @Expose
    @SerializedName("roundOff")
    private double roundOff;

    @Expose
    @SerializedName("netAmount")
    private double netAmount;

    @Expose
    @SerializedName("paymentMode")
    private String paymentMode;

    @Expose
    @SerializedName("bankId")
    private long bankId;

    @Expose
    @SerializedName("date")
    private String date;

    @Expose
    @SerializedName("financialYear")
    private String financialYear;

    @Expose
    @SerializedName("firstDateFinancialYear")
    private String firstDateFinancialYear;

    @Expose
    @SerializedName("lastDateFinancialYear")
    private String lastDateFinancialYear;

    @Expose
    @SerializedName("tendered")
    private String tendered;

    @Ignore
    @Expose
    @SerializedName("mposItemSalesDTOs")
    private List<SalesDTO> mposItemSalesDTOs;

    private int companyId;
    private int brachId;
    private int userId;
    private int userContactId;

    @Ignore
    public SaveBill() {

    }

    public SaveBill(int salesId, SaveBillResponse saveBillResponse, long customerId, double roundOff, double netAmount, String paymentMode, long bankId, String date, String financialYear, String firstDateFinancialYear, String lastDateFinancialYear, String tendered, List<SalesDTO> mposItemSalesDTOs, int companyId, int brachId, int userId, int userContactId) {
        this.salesId = salesId;
        this.saveBillResponse = saveBillResponse;
        this.customerId = customerId;
        this.roundOff = roundOff;
        this.netAmount = netAmount;
        this.paymentMode = paymentMode;
        this.bankId = bankId;
        this.date = date;
        this.financialYear = financialYear;
        this.firstDateFinancialYear = firstDateFinancialYear;
        this.lastDateFinancialYear = lastDateFinancialYear;
        this.tendered = tendered;
        this.mposItemSalesDTOs = mposItemSalesDTOs;
        this.companyId = companyId;
        this.brachId = brachId;
        this.userId = userId;
        this.userContactId = userContactId;
    }


}
