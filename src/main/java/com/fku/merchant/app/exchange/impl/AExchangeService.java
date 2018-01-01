package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.exchange.ExchangeExceptionHandler;
import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.exchange.PriceHelper;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import com.fku.merchant.app.repository.order.domain.OrderType;
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

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount)
            throws MerchantExchangeException, MerchantNonFatalException {
        ExchangeOrder exchangeOrder = null;
        try {

            BigDecimal lastInstrumentMarketPrice = xchangeAdapter.getMarketDataService().getTicker(this.currencyPair).getLast();
            BigDecimal amountOfBaseCurrency = PriceHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, lastInstrumentMarketPrice);
            LimitOrder newBuyLimitOrder = new LimitOrder(
                    Order.OrderType.BID,
                    amountOfBaseCurrency, // The amount of base currency to trade
                    this.currencyPair,
                    null,
                    null,
                    currentBidPrice // In a BID this is the highest acceptable price
            );

            log.info("Sending BUY order to exchange --->");
            log.info(newBuyLimitOrder);
            String orderId = xchangeAdapter.getTradeService().placeLimitOrder(newBuyLimitOrder);

            exchangeOrder = new ExchangeOrder(orderId, OrderType.BUY, currentBidPrice, amountOfBaseCurrency);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return exchangeOrder;
    }
}
