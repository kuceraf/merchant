package com.fku.merchant.app.exchange.impl.dummy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.*;
import java.math.BigDecimal;

// Provide locally stored testing data
public class DummyExchangeDataFactory {
    // testing constants
    public static final BigDecimal CURRENT_BID_PRICE = BigDecimal.valueOf(13728.24); // maximum price that a buyer is willing to pay for a security
    public static final BigDecimal CURRENT_ASK_PRICE = BigDecimal.valueOf(13728.25); // minimum price that a seller is willing to receive for a security

    public static final BigDecimal INSTRUMENT_LAST_PRICE = CURRENT_ASK_PRICE; // actual exchange rate of currency pair (instrument)
    public static final BigDecimal COUNTER_CURRENCY_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal BASE_CURRENCY_AMOUNT = BigDecimal.valueOf(0.00728425);

    // Provide order book of BTC/EUR from 27/12/2018
    public static OrderBook createOrderBook() throws Exception {
        FileInputStream fi = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("exchange/orderBook.ser").toURI()));
        ObjectInputStream oi = new ObjectInputStream(fi);
        return (OrderBook) oi.readObject();
    }

    public static InstrumentPrice createInstrumentPrice(){
        return new InstrumentPrice(CURRENT_BID_PRICE, CURRENT_ASK_PRICE);
    }
}
