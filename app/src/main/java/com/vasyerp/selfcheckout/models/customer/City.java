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
public class City {
    @Expose
    private int cityId;
    @Expose
    private String cityCode;
    @Expose
    private String cityName;
    @Expose
    private String stateCode;
    @Expose
    private String countriesCode;
    @Expose
    private String zoneCode;
}
