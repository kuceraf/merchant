package com.fku.merchant.config.facotry;

import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.SupportedExchangeType;
import com.fku.exchange.service.impl.dummy.DummyExchangeService;
import com.fku.exchange.service.impl.gdax.GDAXExchangeService;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;


public class ExchangeServiceFactory extends AbstractFactoryBean<ExchangeService> {
    @Value("${exchange.currencyPair}")
    private String currencyPairProperty;

    private SupportedExchangeType exchangeType;
    private Exchange xchangeAdapter;

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
                return new GDAXExchangeService(xchangeAdapter, new CurrencyPair(this.currencyPairProperty));
            case DUMMY:
                return new DummyExchangeService();
            default:
                throw new IllegalStateException("Unsupported exchange: " + exchangeType.name());
        }
    }
}
