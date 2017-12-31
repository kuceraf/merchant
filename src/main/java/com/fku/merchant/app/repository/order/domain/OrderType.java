package com.fku.merchant.app.repository.order.domain;

public enum OrderType {

    /**
     * Buy order.
     */
    BUY("Buy"),

    /**
     * Sell order.
     */
    SELL("Sell");

    private final String orderType;

    OrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStringValue() {
        return orderType;
    }
}
