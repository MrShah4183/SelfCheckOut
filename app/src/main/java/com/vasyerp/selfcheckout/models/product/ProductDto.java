package com.vasyerp.selfcheckout.models.product;

import androidx.annotation.Keep;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @Expose
    private String name;
    @Expose
    private double mrp;
    @Expose
    private int cess;
    @Expose
    private int tax_id;
    @Expose
    private int tax_included;
    @Expose
    private String tax_name;
    @Expose
    private double tax_rate;
    @Expose
    private long product_id;
    @Expose
    private String measurement_name;
    @Expose
    private int active_online;
    @Expose
    private int company_id;
    @Expose
    private int branch_id;
    @Expose
    private int is_deleted;
    @Expose
    private String display_name;
    @Expose
    private int purchase_tax_id;
    @Expose
    private double ptax_rate;
    @Expose
    private String ptax_name;
    @Expose
    private String measurement_code;
    @Expose
    private int purchase_tax_included;
    @Expose
    private double cess_tax;
    @Expose
    private double selling_price;
    @Expose
    private int active;
    @Expose
    private String description;
    @Expose
    @SerializedName("hsn_code")
    private String hsnCode;
}
