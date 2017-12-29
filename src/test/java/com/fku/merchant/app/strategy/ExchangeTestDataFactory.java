package com.fku.merchant.app.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.*;

// Provide locally stored testing data
public class ExchangeTestDataFactory {
    // Provide order book of BTC/EUR from 27/12/2018
    public static OrderBook createOrderBook() throws Exception {
        FileInputStream fi = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("exchange/orderBook.ser").toURI()));
        ObjectInputStream oi = new ObjectInputStream(fi);
        return (OrderBook) oi.readObject();
    }
}
