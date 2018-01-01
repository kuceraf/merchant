package com.fku.merchant.config.facotry;

import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.exchange.SupportedExchangeType;
import com.fku.merchant.app.exchange.impl.dummy.DummyExchangeService;
import com.fku.merchant.app.exchange.impl.gdax.GdaxExchangeService;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gdax.GDAXExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;

public class ExchangeServiceFactory extends AbstractFactoryBean<ExchangeService> {
    @Value("${exchange.currencyPair}")
    private String currencyPairProperty;

    private SupportedExchangeType exchangeType;
    private Exchange xchangeAdapter;
    private CurrencyPair currencyPair;

    @PostConstruct
    public void setUp() {
        currencyPair = new CurrencyPair(this.currencyPairProperty);
    }

    public ExchangeServiceFactory(SupportedExchangeType exchangeType, Exchange xchangeAdapter) {
        this.exchangeType = exchangeType;
        this.xchangeAdapter = xchangeAdapter;
    }

    @Override
    public Class<?> getObjectType() {
        return ExchangeService.class;
    }

    @Override
    protected ExchangeService createInstance() throws Exception {
        switch (exchangeType) {
            case GDAX:
                return new GdaxExchangeService(xchangeAdapter, currencyPair);
            case DUMMY:
                return new DummyExchangeService(xchangeAdapter, currencyPair);
            default:
                throw new IllegalStateException();
        }
    }
}
