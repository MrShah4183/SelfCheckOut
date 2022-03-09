package com.vasyerp.selfcheckout.models.customer;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCustomVo {
    @Expose
    private int accountCustomId;
    @Expose
    private Group group;
    @Expose
    private int companyId;
    @Expose
    private int branchId;
    @Expose
    private int alterBy;
    @Expose
    private int createdBy;
    @Expose
    private String accountName;
    @Expose
    private int isDeleted;
    @Expose
    private int isUpdatable;
    @Expose
    private String accounType;
    @Expose
    private String createdOn;
    @Expose
    private String modifiedOn;
}
