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
public class Product {
    @Expose
    @SerializedName("mposProductDTO")
    ProductDto mposProductDTO;

    @Expose
    @SerializedName("productVarientsDTO")
    ProductVarientsDTO productVarientsDTO;

    @Expose
    @SerializedName("companySettingVo")
    CompanySettingVo companySettingVo;
}
