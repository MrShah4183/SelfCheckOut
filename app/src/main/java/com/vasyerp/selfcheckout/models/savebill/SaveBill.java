package com.vasyerp.selfcheckout.models.savebill;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Entity(foreignKeys = {@ForeignKey(entity = SaveBillResponse.class,
        parentColumns = "salesId",
        childColumns = "responseSalesId",
        onDelete = ForeignKey.CASCADE)})
@Keep
@Data
public class SaveBill {

    @PrimaryKey
    private long responseSalesId;
    private int companyId;
    private int branchId;
    private int userFrontId;

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

    //private int userContactId;

    @Ignore
    public SaveBill() {

    }

    //public SaveBill(int salesId, SaveBillResponse saveBillResponse, long customerId, double roundOff, double netAmount, String paymentMode, long bankId, String date, String financialYear, String firstDateFinancialYear, String lastDateFinancialYear, String tendered, List<SalesDTO> mposItemSalesDTOs, int companyId, int brachId, int userId, int userContactId) {


    public SaveBill(long responseSalesId, int companyId, int branchId, int userFrontId, long customerId, double roundOff, double netAmount, String paymentMode, long bankId, String date, String financialYear, String firstDateFinancialYear, String lastDateFinancialYear, String tendered) {
        this.responseSalesId = responseSalesId;
        this.companyId = companyId;
        this.branchId = branchId;
        this.userFrontId = userFrontId;
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
    }
}
