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
public class PaymentTermVo {
    @Expose
    private long paymentTermId;
    @Expose
    private String paymentTermName;
    @Expose
    private int isDeleted;
    @Expose
    private int paymentTermDay;
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
