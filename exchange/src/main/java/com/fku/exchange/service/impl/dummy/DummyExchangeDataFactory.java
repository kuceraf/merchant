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
    // testing constants
    public static final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;
    public static final BigDecimal LIMIT_BID_PRICE = BigDecimal.valueOf(13728.24); // maximum price that a buyer is willing to pay for a security
    public static final BigDecimal LIMIT_ASK_PRICE = BigDecimal.valueOf(13728.25); // minimum price that a seller is willing to receive for a security
    public static final BigDecimal INSTRUMENT_LAST_PRICE = LIMIT_ASK_PRICE; // actual exchange rate of currency pair (instrument)
    public static final BigDecimal COUNTER_CURRENCY_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal BASE_CURRENCY_AMOUNT = BigDecimal.valueOf(0.00728425);
    public static final String EXISTING_OPEN_ORDER_ID = "ee0c87b1-fd65-4961-b317-7c0852b7f37e";

    // ORDER BOOK
    public static OrderBook getOrderBook() {
        LimitOrder ask = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.ASK)
                .withCurrencyPair(CURRENCY_PAIR)
                .withLimitPrice(LIMIT_ASK_PRICE)
                .build();

        LimitOrder bid = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.BID)
                .withCurrencyPair(CURRENCY_PAIR)
                .withLimitPrice(LIMIT_BID_PRICE)
                .build();

        return OrderBookBuilder.anOrderBookBuilder()
                .withAsk(ask)
                .withBid(bid)
                .build();
    }

    // OPEN ORDERS
    public static OpenOrders getOpenOrdersWithAskOpenOrder() {
        LimitOrder existingOpenOrder = LimitOrderBuilder.anLimitOrderBuilder(Order.OrderType.ASK)
                .withId(EXISTING_OPEN_ORDER_ID)
                .build();

        return OpenOrdersBuilder.anOpenOrdersBuilder()
                .withOpenOrder(existingOpenOrder)
                .build();
    }

    // OTHERS
    public static InstrumentPrice createInstrumentPrice(){
        return new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE);
    }

    public static ExchangeOrder getExchangeOrder() {
        return new ExchangeOrder(
                EXISTING_OPEN_ORDER_ID,
                Order.OrderType.ASK,
                LIMIT_ASK_PRICE,
                BASE_CURRENCY_AMOUNT
                );
    }
}
