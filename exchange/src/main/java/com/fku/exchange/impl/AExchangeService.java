package com.fku.exchange.impl;


import com.fku.exchange.exception.ExchangeExceptionHandler;
import com.fku.exchange.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.OrderType;
import com.fku.exchange.exception.ExchangeException;
import com.fku.exchange.exception.ExchangeNonFatalException;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.math.BigDecimal;

@Log4j2
public abstract class AExchangeService implements ExchangeService {
    protected final org.knowm.xchange.Exchange xchangeAdapter;
    protected final CurrencyPair currencyPair;

    protected AExchangeService(Exchange xchangeAdapter, CurrencyPair currencyPair) {
        this.xchangeAdapter = xchangeAdapter;
        this.currencyPair = currencyPair;
    }

    @Override
    public String getExchangeName() {
        return xchangeAdapter.getExchangeSpecification().getExchangeName();
    }

    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws ExchangeException, ExchangeNonFatalException {
        ExchangeOrder exchangeOrder = null;
        LimitOrder newBuyLimitOrder = new LimitOrder(
                Order.OrderType.BID,
                baseCurrencyAmount,
                this.currencyPair,
                null,
                null,
                limitPrice
        );

        log.info("Sending {} order to exchange (price:{}, amount:{})--->", orderType.toString(), limitPrice, baseCurrencyAmount);

        try {
            String orderId = xchangeAdapter.getTradeService().placeLimitOrder(newBuyLimitOrder);
            exchangeOrder = new ExchangeOrder(orderId, OrderType.BUY, limitPrice, baseCurrencyAmount);

        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }

        return exchangeOrder;
    }

    @Override // Fasada pro get lastInstrumentMarketPrice a placeOrder
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount)
            throws ExchangeException, ExchangeNonFatalException {
        BigDecimal baseCurrencyAmount = null;
        try {
            BigDecimal lastInstrumentMarketPrice = xchangeAdapter.getMarketDataService().getTicker(this.currencyPair).getLast();
            baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, lastInstrumentMarketPrice);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return placeOrder(Order.OrderType.BID, baseCurrencyAmount, currentBidPrice);
    }
}
