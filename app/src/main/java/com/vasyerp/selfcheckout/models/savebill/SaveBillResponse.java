package com.vasyerp.selfcheckout.models.savebill;

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
public class SaveBillResponse {
    @SerializedName("salesId")
    @Expose
    public int salesId;
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
    @SerializedName("customerAddress")
    @Expose
    public CustomerAddress customerAddress;

}
