package com.fku.exchange.service;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.ExchangeNonFatalException;
import com.fku.exchange.service.impl.Granularity;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ExchangeService {
    String getExchangeName();
    CurrencyPair getCurrencyPair();

    InstrumentPrice getCurrentPrices() throws MerchantExchangeException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException;
    OpenOrders getOpenOrders() throws MerchantExchangeException;
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException;
    ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyBuyOrderAmount) throws MerchantExchangeException;

    /**
     * Get historical time series from exchange
     * Results in unauthenticated exchange API call.
     * @param startDateTime of series in ISO 8601 format
     * @param endDateTime of series in ISO 8601 format
     * @param granularity
     * @return time series created from historical date returned by exchange
     * @throws MerchantExchangeException
     * @throws ExchangeNonFatalException
     */
    @Deprecated
    TimeSeries getHistoricalTimeSeries(LocalDateTime startDateTime, LocalDateTime endDateTime, Granularity granularity)
            throws MerchantExchangeException;

    /**
     * Get maximal time series from now
     * @param granularity
     * @return
     * @throws MerchantExchangeException
     * @throws ExchangeNonFatalException
     */
    TimeSeries getHistoricalTimeSeries(Granularity granularity)
            throws MerchantExchangeException;

    Bar getLastBar(Granularity granularity)
            throws MerchantExchangeException;

//    @Deprecated
//    Bar getBar(Granularity granularity)
//            throws MerchantExchangeException, MerchantExchangeNonFatalException;;
}
