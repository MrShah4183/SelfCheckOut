package com.vasyerp.selfcheckout.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsModel {
    String proName;
    Double proQty;
    Double proDisc;
    Double proPrice;
}
