package com.vasyerp.selfcheckout.models.savebill;

import androidx.annotation.Keep;

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
public class SaveBill {
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

     @Expose
     @SerializedName("mposItemSalesDTOs")
     private List<SalesDTO> mposItemSalesDTOs;
}
