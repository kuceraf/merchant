package com.fku.merchant.app.strategy.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@AllArgsConstructor
public class PricePair {
    @Getter
    private BigDecimal bidPrice;
    @Getter
    private BigDecimal askPrice;
}
