package com.fku.exchange.service;

import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.ExchangeNonFatalException;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.impl.Granularity;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.time.LocalDateTime;
import java.util.List;

public interface PassiveExchangeService {
    String getExchangeName();
    CurrencyPair getCurrencyPair();
    InstrumentPrice getCurrentPrices() throws MerchantExchangeException;
    OpenOrders getOpenOrders() throws MerchantExchangeException;

    /**
     * Get maximal time series from now
     * @param granularity
     * @return
     * @throws MerchantExchangeException
     * @throws ExchangeNonFatalException
     */
    TimeSeries getHistoricalTimeSeries(Granularity granularity) throws MerchantExchangeException;
//    Bar getLastBar(Granularity granularity) throws MerchantExchangeException;

    Observable<Bar> getBarObservable() throws MerchantExchangeException;
    void nextBar() throws MerchantExchangeException;
}
