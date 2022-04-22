package com.vasyerp.selfcheckout.models.login;

import androidx.annotation.Keep;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vasyerp.selfcheckout.models.customer.CustomerDetailsResponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Keep
@Data

public class LogIn {
    @PrimaryKey(autoGenerate = true)
    public int tempId;
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
    /*@Ignore
    @SerializedName("contactVo")
    @Expose
    public CustomerDetailsResponse customerDetailsResponse;*/

    @Ignore
    public LogIn() {

    }

    public LogIn(int tempId, String branchId, String gstin, String address, String monthInterval, String lastDateFinancialYear, String cityCode, String contactName, String companyUpi, String branchName, String firstDateFinancialYear, String fssaino, String countriesCode, String telephone, String userFrontId, String corporateId, String financialYear, String companyId, String logoPrefix, String domainName, String logo, String stateCode, String userType, String menuPermission, String cashierName) {
        this.tempId = tempId;
        this.branchId = branchId;
        this.gstin = gstin;
        this.address = address;
        this.monthInterval = monthInterval;
        this.lastDateFinancialYear = lastDateFinancialYear;
        this.cityCode = cityCode;
        this.contactName = contactName;
        this.companyUpi = companyUpi;
        this.branchName = branchName;
        this.firstDateFinancialYear = firstDateFinancialYear;
        this.fssaino = fssaino;
        this.countriesCode = countriesCode;
        this.telephone = telephone;
        this.userFrontId = userFrontId;
        this.corporateId = corporateId;
        this.financialYear = financialYear;
        this.companyId = companyId;
        this.logoPrefix = logoPrefix;
        this.domainName = domainName;
        this.logo = logo;
        this.stateCode = stateCode;
        this.userType = userType;
        this.menuPermission = menuPermission;
        this.cashierName = cashierName;
    }
}
