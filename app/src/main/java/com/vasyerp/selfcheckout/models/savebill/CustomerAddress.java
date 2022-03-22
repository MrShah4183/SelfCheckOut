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
public class CustomerAddress {
    @SerializedName("addressLine1")
    @Expose
    public String addressLine1;
    @SerializedName("addressLine2")
    @Expose
    public String addressLine2;
    @SerializedName("addressLine3")
    @Expose
    public String addressLine3;
    @SerializedName("cityName")
    @Expose
    public String cityName;
    @SerializedName("stateName")
    @Expose
    public String stateName;
    @SerializedName("countriesName")
    @Expose
    public String countriesName;
    @SerializedName("pinCode")
    @Expose
    public String pinCode;
}
