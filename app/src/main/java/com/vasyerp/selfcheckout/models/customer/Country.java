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
public class Country {
    @Expose
    private int countriesId;
    @Expose
    private String countriesCode;
    @Expose
    private String countriesName;
    @Expose
    private String currencyCode;
    @Expose
    private String currencyName;
    @Expose
    private String currencySymbol;
}
