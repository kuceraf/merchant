package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.domain.OrderType;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.math.BigDecimal;

// Provide locally stored testing data
@Log4j2
public class DummyExchangeDataFactory {
    // testing constants
    public static final BigDecimal CURRENT_BID_PRICE = BigDecimal.valueOf(13728.24); // maximum price that a buyer is willing to pay for a security
    public static final BigDecimal CURRENT_ASK_PRICE = BigDecimal.valueOf(13728.25); // minimum price that a seller is willing to receive for a security
    public static final BigDecimal INSTRUMENT_LAST_PRICE = CURRENT_ASK_PRICE; // actual exchange rate of currency pair (instrument)
    public static final BigDecimal COUNTER_CURRENCY_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal BASE_CURRENCY_AMOUNT = BigDecimal.valueOf(0.00728425);
    public static final String EXISTING_OPEN_ORDER_ID = "ee0c87b1-fd65-4961-b317-7c0852b7f37e";

    private static final String ORDER_BOOK_RESOURCE = "dummy/orderBook.ser";
    private static final String OPEN_ORDERS_RESOURCE = "dummy/openOrders.ser";
    // Provide order book of BTC/EUR from 27/12/2017
    public static OrderBook createOrderBook() { // FIXME refactor to parametric method
        OrderBook orderBook = null;
        try {
            InputStream  fi = new ClassPathResource(ORDER_BOOK_RESOURCE).getInputStream();
            ObjectInputStream oi = new ObjectInputStream(fi);
            orderBook = (OrderBook) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e);
        }
        if (orderBook == null) {
            throw new IllegalStateException("Can'nt load order book resource");
        } else {
            return orderBook;
        }
    }

    // Provide open orders at my account from 6/01/2018
    public static OpenOrders createOpenOrders() {
        OpenOrders openOrders = null;
        try {
            InputStream  fi = new ClassPathResource(OPEN_ORDERS_RESOURCE).getInputStream();
            ObjectInputStream oi = new ObjectInputStream(fi);
            openOrders = (OpenOrders) oi.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error(e);
        }
        if (openOrders == null) {
            throw new IllegalStateException("Can'nt load open orders resource");
        } else {
            return openOrders;
        }
    }

    public static InstrumentPrice createInstrumentPrice(){
        return new InstrumentPrice(CURRENT_BID_PRICE, CURRENT_ASK_PRICE);
    }

    public static ExchangeOrder createExchangeOrder() {
        return new ExchangeOrder(
                EXISTING_OPEN_ORDER_ID,
                Order.OrderType.ASK,
                CURRENT_ASK_PRICE,
                BASE_CURRENCY_AMOUNT
                );
    }
}
