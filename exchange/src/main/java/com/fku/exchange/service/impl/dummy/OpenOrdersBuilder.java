package com.fku.exchange.service.impl.dummy;

import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public final class OpenOrdersBuilder {
    private static final String OPEN_ORDERS_RESOURCE = "dummy/openOrders.ser";

    private List<LimitOrder> openOrders = new ArrayList<>();

    private OpenOrdersBuilder() {
    }

    static OpenOrdersBuilder anOpenOrdersBuilder() {
        return new  OpenOrdersBuilder();
    }

    OpenOrdersBuilder withOpenOrder(LimitOrder openOrder) {
        openOrders.add(openOrder);
        return this;
    }

    OpenOrders build() {
        return new OpenOrders(openOrders);
    }

    // Provides open orders at my account from 6/01/2018
    private static OpenOrders getOpenOrdersFromFile() {
        OpenOrders openOrders = null;
        try {
            InputStream fi = new ClassPathResource(OPEN_ORDERS_RESOURCE).getInputStream();
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
}
