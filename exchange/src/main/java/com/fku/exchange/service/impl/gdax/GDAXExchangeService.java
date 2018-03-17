package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.ExchangeNonFatalException;
import com.fku.exchange.service.ActiveExchangeService;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.PassiveExchangeService;
import com.fku.exchange.service.impl.BaseExchangeService;
import com.fku.exchange.service.impl.Granularity;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.RestProxyFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Log4j2
public class GDAXExchangeService implements ExchangeService {
    private final PassiveExchangeService passiveExchangeService;
    private final ActiveExchangeService activeExchangeService;

    public GDAXExchangeService(PassiveExchangeService passiveExchangeService, ActiveExchangeService activeExchangeService) {
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

    @Override
    public Bar getLastBar(Granularity granularity) throws MerchantExchangeException {
        return passiveExchangeService.getLastBar(granularity);
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
