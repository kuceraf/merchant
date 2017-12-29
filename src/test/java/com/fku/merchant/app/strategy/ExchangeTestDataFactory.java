package com.fku.merchant.app.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.*;

public class ExchangeTestDataFactory {
    public static OrderBook createOrderBook() throws Exception {
        FileInputStream fi = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("exchange/orderBook.ser").toURI()));
        ObjectInputStream oi = new ObjectInputStream(fi);
        return (OrderBook) oi.readObject();
    }
}
