package com.vasyerp.selfcheckout.models.login;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginBody {
    @Expose
    private String companyId;
    @Expose
    private String branchId;
}
