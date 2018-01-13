package com.fku.exchange.service.impl.dummy;

import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public final class OrderBookBuilder {
    private static final String ORDER_BOOK_RESOURCE = "dummy/orderBook.ser";
    private List<LimitOrder> asks = new ArrayList<>();
    private List<LimitOrder> bids = new ArrayList<>();
    private Date timestamp;

    private OrderBookBuilder() {}

    static OrderBookBuilder anOrderBookBuilder() {
        return new OrderBookBuilder();
    }

    OrderBookBuilder withAsk(LimitOrder ask) {
        asks.add(ask);
        return this;
    }

    OrderBookBuilder withBid(LimitOrder bid) {
        bids.add(bid);
        return this;
    }

    OrderBookBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    OrderBook build() {
        return new OrderBook(timestamp, asks, bids);
    }

    // Provides order book of BTC/EUR from 27/12/2017
    private static OrderBook getOrderBookFromFile() {
        OrderBook orderBook = null;
        try {
            InputStream fi = new ClassPathResource(ORDER_BOOK_RESOURCE).getInputStream();
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

}
