package com.fku.merchant.app.repository.order.domain;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ExchangeOrder {

    /**
     * Id - default to null.
     */
    public String id = null;

    /**
     * Type: buy/sell. We default to null which means no order has been placed yet, i.e. we've just started!
     */
    public OrderType type = null;

    /**
     * Price to buy/sell at - default to zero.
     */
    public BigDecimal price = BigDecimal.ZERO;

    /**
     * Number of units to buy/sell - default to zero.
     */
    public BigDecimal amount = BigDecimal.ZERO;

}

