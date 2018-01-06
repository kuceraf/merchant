package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.domain.InstrumentPrice;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.*;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

// Provide locally stored testing data
@Log4j2
public class DummyExchangeDataFactory {
    // testing constants
    public static final BigDecimal CURRENT_BID_PRICE = BigDecimal.valueOf(13728.24); // maximum price that a buyer is willing to pay for a security
    public static final BigDecimal CURRENT_ASK_PRICE = BigDecimal.valueOf(13728.25); // minimum price that a seller is willing to receive for a security
    public static final BigDecimal INSTRUMENT_LAST_PRICE = CURRENT_ASK_PRICE; // actual exchange rate of currency pair (instrument)
    public static final BigDecimal COUNTER_CURRENCY_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal BASE_CURRENCY_AMOUNT = BigDecimal.valueOf(0.00728425);

    private static final String ORDER_BOOK_RESOURCE = "orderBook.ser";
    // Provide order book of BTC/EUR from 27/12/2018
    public static OrderBook createOrderBook() {
        OrderBook orderBook = null;
        try {
            Path path = Paths.get(Objects.requireNonNull(Thread.currentThread().getContextClassLoader()
                    .getResource(ORDER_BOOK_RESOURCE)).toURI());
            InputStream  fi = Files.newInputStream(path);
            ObjectInputStream oi = new ObjectInputStream(fi);
            orderBook = (OrderBook) oi.readObject();
        } catch (URISyntaxException | IOException | ClassNotFoundException e) {
            log.error(e);
        }
        if (orderBook == null) {
            throw new IllegalStateException("Can'nt load order book resource");
        } else {
            return orderBook;
        }
    }

    public static InstrumentPrice createInstrumentPrice(){
        return new InstrumentPrice(CURRENT_BID_PRICE, CURRENT_ASK_PRICE);
    }
}
