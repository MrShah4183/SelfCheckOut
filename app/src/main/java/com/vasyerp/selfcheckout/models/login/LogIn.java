package com.vasyerp.selfcheckout.models.login;

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
public class LogIn {
    @SerializedName("branchId")
    @Expose
    public String branchId;
    @SerializedName("GSTIN")
    @Expose
    public String gstin;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("monthInterval")
    @Expose
    public String monthInterval;
    @SerializedName("lastDateFinancialYear")
    @Expose
    public String lastDateFinancialYear;
    @SerializedName("cityCode")
    @Expose
    public String cityCode;
    @SerializedName("contactName")
    @Expose
    public String contactName;
    @SerializedName("companyUpi")
    @Expose
    public String companyUpi;
    @SerializedName("branchName")
    @Expose
    public String branchName;
    @SerializedName("firstDateFinancialYear")
    @Expose
    public String firstDateFinancialYear;
    @SerializedName("FSSAINO")
    @Expose
    public String fssaino;
    @SerializedName("countriesCode")
    @Expose
    public String countriesCode;
    @SerializedName("telephone")
    @Expose
    public String telephone;
    @SerializedName("userFrontId")
    @Expose
    public String userFrontId;
    @SerializedName("corporateId")
    @Expose
    public String corporateId;
    @SerializedName("financialYear")
    @Expose
    public String financialYear;
    @SerializedName("companyId")
    @Expose
    public String companyId;
    @SerializedName("logoPrefix")
    @Expose
    public String logoPrefix;
    @SerializedName("domainName")
    @Expose
    public String domainName;
    @SerializedName("logo")
    @Expose
    public String logo;
    @SerializedName("stateCode")
    @Expose
    public String stateCode;
    @SerializedName("userType")
    @Expose
    public String userType;
    @SerializedName("menuPermission")
    @Expose
    public String menuPermission;
    @SerializedName("cashierName")
    @Expose
    public String cashierName;
}
