package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;

// Provide locally stored testing data
@Log4j2
public class DummyExchangeDataFactory {

    private static final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;

    // ORDER BOOK
    public static OrderBook getOrderBook(BigDecimal limitAskPrice, BigDecimal limitBidPrice) {
        LimitOrder ask = new LimitOrder.Builder(Order.OrderType.ASK, CURRENCY_PAIR)
                .limitPrice(limitAskPrice)
                .build();

        LimitOrder bid = new LimitOrder.Builder(Order.OrderType.BID, CURRENCY_PAIR)
                .limitPrice(limitBidPrice)
                .build();

        return new OrderBook(new Date(), Collections.singletonList(ask), Collections.singletonList(bid));
    }

    // OPEN ORDERS
    public static OpenOrders getOpenOrdersWithAskOpenOrder(String openOrderId) {
        LimitOrder existingOpenOrder = new LimitOrder.Builder(Order.OrderType.ASK, CURRENCY_PAIR)
                .id(openOrderId)
                .build();

        return new OpenOrders(Collections.singletonList(existingOpenOrder));
    }
}
