package com.fku.exchange.service.impl;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.ActiveExchangeService;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.PassiveExchangeService;
import io.reactivex.Observable;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.math.BigDecimal;

@Log4j2
public class ExchangeServiceFacade implements ExchangeService {
    private final PassiveExchangeService passiveExchangeService;
    private final ActiveExchangeService activeExchangeService;

    public ExchangeServiceFacade(PassiveExchangeService passiveExchangeService, ActiveExchangeService activeExchangeService) {
        this.passiveExchangeService = passiveExchangeService;
        this.activeExchangeService = activeExchangeService;
    }

    // PASSIVE OPERATION
    @Override
    public String getExchangeName() {
        return passiveExchangeService.getExchangeName();
    }

    @Override
    public CurrencyPair getCurrencyPair() {
        return passiveExchangeService.getCurrencyPair();
    }

    @Override
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException {
        return passiveExchangeService.getCurrentPrices();
    }

    @Override
    public OpenOrders getOpenOrders() throws MerchantExchangeException {
        return passiveExchangeService.getOpenOrders();
    }

    @Override
    public TimeSeries getHistoricalTimeSeries(Granularity granularity) throws MerchantExchangeException {
        return passiveExchangeService.getHistoricalTimeSeries(granularity);
    }

//    @Override
//    public Bar getLastBar(Granularity granularity) throws MerchantExchangeException {
//        return passiveExchangeService.getLastBar(granularity);
//    }

    @Override
    public Observable<Bar> getBarObservable() throws MerchantExchangeException {
        return passiveExchangeService.getBarObservable();
    }

    @Override
    public void nextBar() throws MerchantExchangeException {
         passiveExchangeService.nextBar();
    }

    // ACTIVE OPERATION
    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException {
        return activeExchangeService.placeOrder(orderType, baseCurrencyAmount, limitPrice);
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException {
        return activeExchangeService.placeBuyOrder(currentBidPrice, counterCurrencyAmount);
    }

    @Override
    public ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyBuyOrderAmount) throws MerchantExchangeException {
        return activeExchangeService.placeBuyOrderAtCurrentPrice(counterCurrencyBuyOrderAmount);
    }
}
