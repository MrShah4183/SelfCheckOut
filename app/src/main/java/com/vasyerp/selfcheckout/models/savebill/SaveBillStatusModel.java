package com.vasyerp.selfcheckout.models.savebill;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveBillStatusModel {
    @SerializedName("salesId")
    @Expose
    public long salesId;
    @SerializedName("paymentId")
    @Expose
    public String paymentId;
    @SerializedName("paymentMode")
    @Expose
    public String paymentMode;
    @SerializedName("paymentStatus")
    @Expose
    public String paymentStatus;
}
