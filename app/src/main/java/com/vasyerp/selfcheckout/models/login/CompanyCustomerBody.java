package com.vasyerp.selfcheckout.models.login;

import com.google.gson.annotations.Expose;
import com.vasyerp.selfcheckout.models.customer.CreateCustomerBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyCustomerBody {
    @Expose
    private String companyId;
    @Expose
    private String branchId;
    //@Expose
    //private CreateCustomerBody contactDetails;
}
