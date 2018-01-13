package com.fku.exchange.service.impl.dummy;

import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.math.BigDecimal;
import java.util.Date;

public final class LimitOrderBuilder {
    private Order.OrderType type;
    private BigDecimal originalAmount;
    private CurrencyPair currencyPair;
    private String id;
    private Date timestamp;
    private BigDecimal limitPrice;

    private LimitOrderBuilder(Order.OrderType type) {
        this.type = type;
    }

    static LimitOrderBuilder anLimitOrderBuilder(Order.OrderType type) {
        return new LimitOrderBuilder(type);
    }

    LimitOrderBuilder withOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
        return this;
    }

    LimitOrderBuilder withCurrencyPair(CurrencyPair currencyPair) {
        this.currencyPair = currencyPair;
        return this;
    }

    LimitOrderBuilder withId(String id) {
        this.id = id;
        return this;
    }

    LimitOrderBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    LimitOrderBuilder withLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
        return this;
    }

    LimitOrder build() {
        return new LimitOrder(
                type,
                originalAmount,
                currencyPair,
                id,
                timestamp,
                limitPrice);
    }
}
