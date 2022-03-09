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
public class MemberShipMasterVo {
    @Expose
    private static final long serialVersionUID = 1L;
    @Expose
    private long membershipId;
    @Expose
    private String membershipName;
    @Expose
    private int membershipDay;
    @Expose
    private double amount = 0.0;
    @Expose
    private int isDeleted;
    @Expose
    private long companyId;
    @Expose
    private long branchId;
    @Expose
    private long alterBy;
    @Expose
    private long createdBy;
    @Expose
    private String createdOn;
    @Expose
    private String modifiedOn;
    @Expose
    private String createdbyname;
}
