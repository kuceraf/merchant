package com.fku.merchant.app.repository.order.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;


@AllArgsConstructor
public class InstrumentPrice {
    @Getter
    private BigDecimal bidPrice;
    @Getter
    private BigDecimal askPrice;
}
