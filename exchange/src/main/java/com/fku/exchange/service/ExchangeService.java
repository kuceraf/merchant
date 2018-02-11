package com.fku.exchange.service;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.TimeSeries;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    CurrencyPair getCurrencyPair();

    InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantExchangeNonFatalException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantExchangeNonFatalException;
    OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantExchangeNonFatalException;
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws MerchantExchangeException, MerchantExchangeNonFatalException;
     TimeSeries getHistoricalTimeSeries(String startTime, String endTime, String granularityInSec)
            throws MerchantExchangeException, MerchantExchangeNonFatalException;
}
