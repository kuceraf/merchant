package com.fku.exchange.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.knowm.xchange.dto.Order;

@ToString
@Getter @Setter
public class ExchangeOrder {
    private String id;
    private Timestamp timestamp;
    private Order.OrderType type;
    private BigDecimal price;
    private BigDecimal amount; // base currency amount

    public ExchangeOrder(String id, Order.OrderType type, BigDecimal price, BigDecimal amount) {
        this.id = id;
        this.timestamp = new Timestamp(System.currentTimeMillis());
        this.type = type;
        this.price = price;
        this.amount = amount;
    }
}

