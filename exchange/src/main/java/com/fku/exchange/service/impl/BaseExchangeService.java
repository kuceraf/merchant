package com.fku.exchange.service.impl;


import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.math.BigDecimal;

@Log4j2
public abstract class BaseExchangeService {
    protected final org.knowm.xchange.Exchange xchangeAdapter;
    protected final CurrencyPair currencyPair;

    protected BaseExchangeService(@Nonnull Exchange xchangeAdapter, @Nonnull CurrencyPair currencyPair) {
        this.xchangeAdapter = xchangeAdapter;
        this.currencyPair = currencyPair;
    }
//    TODO WebSocket streaming https://github.com/bitrich-info/xchange-stream
}
