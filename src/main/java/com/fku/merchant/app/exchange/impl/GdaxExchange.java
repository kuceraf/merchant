package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.exchange.Exchange;
import com.fku.merchant.app.exchange.MerchantExchangeException;
import com.fku.merchant.app.strategy.dto.PricePair;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.io.IOException;


@Log4j2
public class GdaxExchange implements Exchange {
    private final CurrencyPair currencyPair;
    private final org.knowm.xchange.Exchange xchange;

    public GdaxExchange(org.knowm.xchange.Exchange xchange, CurrencyPair currencyPair) {
        this.xchange = xchange;
        this.currencyPair = currencyPair;
    }

    @Override
    public String getExchangeName() {
        return xchange.getExchangeSpecification().getExchangeName();
    }

    @Override
    public PricePair getCurrentPrices() throws MerchantExchangeException {
        OrderBook orderBook = getOrderBook();
        return new PricePair(PriceHelper.getCurrentBidPrice(orderBook), PriceHelper.getCurrentAskPrice(orderBook));
    }

    private OrderBook getOrderBook() throws MerchantExchangeException {
        OrderBook orderBook = null;
        try {
            orderBook = xchange.getMarketDataService().getOrderBook(currencyPair, 2);
        } catch (IOException e) {
            log.error("Failed to get order book from xchange");
            throw new MerchantExchangeException("Failed to get order book from xchange");
        }
        return orderBook;
    }
}
