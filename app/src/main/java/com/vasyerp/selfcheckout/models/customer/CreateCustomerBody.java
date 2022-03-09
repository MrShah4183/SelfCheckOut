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
public class CreateCustomerBody {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String mobileNo;
    @Expose
    private String dateOfBirth;
    @Expose
    private String anniversaryDate;
    @Expose
    private String addressLine1;
    @Expose
    private String stateCode;
    @Expose
    private String cityCode;
    @Expose
    private String gstType;
    @Expose
    private String gstin;
    @Expose
    private String countriesCode;
}
