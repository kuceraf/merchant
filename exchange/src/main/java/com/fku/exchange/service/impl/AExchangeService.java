package com.fku.exchange.service.impl;


import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@Log4j2
public abstract class AExchangeService implements ExchangeService {
    protected final org.knowm.xchange.Exchange xchangeAdapter;
    protected final CurrencyPair currencyPair;

    protected AExchangeService(@Nonnull Exchange xchangeAdapter, @Nonnull CurrencyPair currencyPair) {
        this.xchangeAdapter = xchangeAdapter;
        this.currencyPair = currencyPair;
    }

    protected abstract OrderBook getOrderBook() throws MerchantExchangeException, MerchantExchangeNonFatalException;

    @Override
    public String getExchangeName() {
        return xchangeAdapter.getExchangeSpecification().getExchangeName();
    }

    @Override
    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        ExchangeOrder exchangeOrder = null;
        LimitOrder newBuyLimitOrder = new LimitOrder.Builder(orderType, this.currencyPair)
                .originalAmount(baseCurrencyAmount)
                .limitPrice(limitPrice)
                .build();

        log.info("Sending {} order to exchange (price:{}, amount:{})--->", orderType.toString(), limitPrice, baseCurrencyAmount);

        try {
            String orderId = xchangeAdapter.getTradeService().placeLimitOrder(newBuyLimitOrder);
            exchangeOrder = new ExchangeOrder(
                    orderId,
                    orderType,
                    limitPrice,
                    baseCurrencyAmount
            );
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }

        return exchangeOrder;
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount)
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        BigDecimal baseCurrencyAmount = null;
        try {
            BigDecimal lastInstrumentMarketPrice = xchangeAdapter.getMarketDataService().getTicker(this.currencyPair).getLast();
            baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, lastInstrumentMarketPrice);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return placeOrder(Order.OrderType.BID, baseCurrencyAmount, currentBidPrice);
    }

    @Override
    public OpenOrders getOpenOrders()
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        OpenOrders openOrders = null;
        try {
            OpenOrdersParams openOrdersParams = xchangeAdapter.getTradeService().createOpenOrdersParams();
            openOrders = xchangeAdapter.getTradeService().getOpenOrders(openOrdersParams);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return openOrders;
    }

    @Override
    public InstrumentPrice getCurrentPrices()
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        OrderBook orderBook = getOrderBook();
        return new InstrumentPrice(ExchangeHelper.getCurrentBidPrice(orderBook), ExchangeHelper.getCurrentAskPrice(orderBook));
    }
}
