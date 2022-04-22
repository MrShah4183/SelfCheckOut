package com.vasyerp.selfcheckout.models.customer;

import com.google.gson.annotations.Expose;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCustomerDetails {
    @Expose
    private String stateName;
    @Expose
    private int active;
    @Expose
    private long contactId;
    @Expose
    private String firstName;
    @Expose
    private String addressLine1;
    @Expose
    private String cityCode;
    @Expose
    private String countriesCode;
    @Expose
    private String lastName;
    @Expose
    private String stateCode;
    @Expose
    private String cityName;
    @Expose
    private String countriesName;
    @Expose
    private String mobileNo;
    @Expose
    private String dateOfBirth;
    @Expose
    private String anniversaryDate;
    @Expose
    private String gstType;
    @Expose
    private String gstin;
}
