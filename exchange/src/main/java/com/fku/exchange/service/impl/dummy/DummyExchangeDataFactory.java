package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.service.impl.Granularity;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.*;

import java.io.*;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

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

        List<LimitOrder> asks = new ArrayList<>();
        asks.add(ask);
        List<LimitOrder> bids = new ArrayList<>();
        bids.add(bid);
        return new OrderBook(new Date(), asks, bids);
    }

    // OPEN ORDERS
    public static OpenOrders getOpenOrdersWithAskOpenOrder(String openOrderId) {
        LimitOrder existingOpenOrder = new LimitOrder.Builder(Order.OrderType.ASK, CURRENCY_PAIR)
                .id(openOrderId)
                .build();

        List<LimitOrder> existingOpenOrders = new ArrayList<>();
        existingOpenOrders.add(existingOpenOrder);
        return new OpenOrders(existingOpenOrders);
    }

    public static TimeSeries getTimeSeries(int numberOfBars) {
        ZonedDateTime startDateTime = ZonedDateTime.now();
        Deque<Bar> bars = new LinkedList<>();
        for(int i = 0; i < numberOfBars ;i++) {
            Bar bar = createBar(startDateTime, i);
            bars.addFirst(bar);
        }
        return new BaseTimeSeries("dummy-time-series", new ArrayList<>(bars));
    }

    private static Bar createBar(ZonedDateTime startDateTime, int num) {
        long periodInSecond = Granularity.ONE_MINUTE.getSeconds();
        return new BaseBar(
                Duration.ofSeconds(periodInSecond),
                startDateTime.minusSeconds(periodInSecond * num),
                Decimal.valueOf(7174.07),
                Decimal.valueOf(7174.07),
                Decimal.valueOf(7174.06),
                Decimal.valueOf(7174.06), // close price
                Decimal.valueOf(0.5261563)

        );
    }

}
