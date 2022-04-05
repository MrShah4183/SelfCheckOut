package com.vasyerp.selfcheckout.models.savebill;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Keep
@Data
public class SaveBillResponse {
    @PrimaryKey
    @SerializedName("salesId")
    @Expose
    public long salesId;
    @SerializedName("prefix")
    @Expose
    public String prefix;
    @SerializedName("token")
    @Expose
    public int token;
    @SerializedName("salesNo")
    @Expose
    public int salesNo;
    @SerializedName("upiData")
    @Expose
    public String upiData;

    public String paymentId = "0";
    public String paymentMode = "0";
    public String paymentStatus = "pending";
    private boolean isDeleted = false;
    private String paymentFailReason = "";

    @Ignore
    @SerializedName("customerAddress")
    @Expose
    public CustomerAddress customerAddress;

    @Ignore
    public SaveBillResponse() {

    }

    public SaveBillResponse(long salesId, String prefix, int token, int salesNo, String upiData, String paymentId, String paymentMode, String paymentStatus) {
        this.salesId = salesId;
        this.prefix = prefix;
        this.token = token;
        this.salesNo = salesNo;
        this.upiData = upiData;
        this.paymentId = paymentId;
        this.paymentMode = paymentMode;
        this.paymentStatus = paymentStatus;
    }
}
