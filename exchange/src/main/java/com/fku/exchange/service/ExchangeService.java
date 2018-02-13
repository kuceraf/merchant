package com.fku.exchange.service;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.service.impl.Granularity;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.TimeSeries;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExchangeService {
    String getExchangeName();
    CurrencyPair getCurrencyPair();

    InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantExchangeNonFatalException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantExchangeNonFatalException;
    OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantExchangeNonFatalException;
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws MerchantExchangeException, MerchantExchangeNonFatalException;

    /**
     * Get historical time series from exchange
     * Results in unauthenticated exchange API call.
     * @param startDateTime of series in ISO 8601 format
     * @param endDateTime of series in ISO 8601 format
     * @param granularity
     * @return time series created from historical date returned by exchange
     * @throws MerchantExchangeException
     * @throws MerchantExchangeNonFatalException
     */
    TimeSeries getHistoricalTimeSeries(LocalDateTime startDateTime, LocalDateTime endDateTime, Granularity granularity)
            throws MerchantExchangeException, MerchantExchangeNonFatalException;
}
