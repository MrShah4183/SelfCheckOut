package com.vasyerp.selfcheckout.models.product;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySettingVo {
    @Expose
    private int companySettingId;
    @Expose
    private int value;
    @Expose
    private String addValue;
    @Expose
    private String type;
    @Expose
    private int companyId;
    @Expose
    private int branchId;
    @Expose
    private int userId;
}
