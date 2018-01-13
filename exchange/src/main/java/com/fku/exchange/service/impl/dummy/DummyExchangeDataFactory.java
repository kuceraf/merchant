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

// Provide locally stored testing data
@Log4j2
public class DummyExchangeDataFactory {

    private static final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;

    // ORDER BOOK
    public static OrderBook getOrderBook(BigDecimal limitAskPrice, BigDecimal limitBidPrice) {
        LimitOrder ask = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.ASK)
                .withCurrencyPair(CURRENCY_PAIR)
                .withLimitPrice(limitAskPrice)
                .build();

        LimitOrder bid = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.BID)
                .withCurrencyPair(CURRENCY_PAIR)
                .withLimitPrice(limitBidPrice)
                .build();

        return OrderBookBuilder.anOrderBookBuilder()
                .withAsk(ask)
                .withBid(bid)
                .build();
    }

    // OPEN ORDERS
    public static OpenOrders getOpenOrdersWithAskOpenOrder(String openOrderId) {
        LimitOrder existingOpenOrder = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.ASK)
                .withId(openOrderId)
                .build();

        return OpenOrdersBuilder.anOpenOrdersBuilder()
                .withOpenOrder(existingOpenOrder)
                .build();
    }
}
