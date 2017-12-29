package com.fku.merchant.app.exchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.*;
import java.math.BigDecimal;

// Provide locally stored testing data
public class ExchangeTestDataFactory {
    public static final BigDecimal CURRENT_BID_PRICE = BigDecimal.valueOf(13728.24);
    public static final BigDecimal CURRENT_ASK_PRICE = BigDecimal.valueOf(13728.25);

    // Provide order book of BTC/EUR from 27/12/2018
    public static OrderBook createOrderBook() throws Exception {
        FileInputStream fi = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("exchange/orderBook.ser").toURI()));
        ObjectInputStream oi = new ObjectInputStream(fi);
        return (OrderBook) oi.readObject();
    }
}
