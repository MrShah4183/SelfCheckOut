package com.vasyerp.selfcheckout.models.product;

import androidx.annotation.Keep;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Keep
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatus {
    boolean isProductAddable;
    String reason;
}
