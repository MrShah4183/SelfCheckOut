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
public class ContactAddressVo {
    @Expose
    private String countriesName;
    @Expose
    private String stateName;
    @Expose
    private String cityName;
    @Expose
    private String localityName;
    @Expose
    private Long contactAddressId;
    @Expose
    private String companyName;
    @Expose
    private String name;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String addressLine1;
    @Expose
    private String addressLine2;
    @Expose
    private String addressLine3;
    @Expose
    private String countriesCode;
    @Expose
    private String zoneCode;
    @Expose
    private String stateCode;
    @Expose
    private String cityCode;
    @Expose
    private String localityCode;
    @Expose
    private String place;
    @Expose
    private Long lat;
    @Expose
    private Long lng;
    @Expose
    private String pinCode;
    @Expose
    private String phoneNo;
    @Expose
    private int isDeleted;
    @Expose
    private int isDefault;
}
