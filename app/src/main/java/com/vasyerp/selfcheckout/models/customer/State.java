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
public class State {
    @Expose
    private int stateId;
    @Expose
    private String stateCode;
    @Expose
    private String wooCommerceStateCode;
    @Expose
    private String stateName;
    @Expose
    private String countriesCode;
    @Expose
    private String zoneCode;
}
