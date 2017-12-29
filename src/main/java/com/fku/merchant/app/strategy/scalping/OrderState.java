package com.fku.merchant.app.strategy.scalping;

import java.math.BigDecimal;
import lombok.ToString;

@ToString
public class OrderState {

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

