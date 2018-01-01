package com.fku.merchant.config;

import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.exchange.impl.gdax.GdaxExchangeService;
import com.fku.merchant.app.exchange.SupportedExchangeType;
import com.fku.merchant.config.facotry.ExchangeServiceFactory;
import com.fku.merchant.config.facotry.XchangeAdapterFactory;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Configuration
@EnableScheduling
public class MerchantApplicationConfig {
    // *** EXCHANGE CONFIG ***
    @Value("${exchange.type}")
    private String exchangeTypeProperty;

    private SupportedExchangeType exchangeType;

    @PostConstruct
    private void setUp() {
        exchangeType = SupportedExchangeType.valueOf(exchangeTypeProperty.toUpperCase());
    }

    /** org.knowm.xchange config (3th party library to access cryptocurrency exchanges)**/
    @Bean("xchangeAdapterFactory")
    public FactoryBean<Exchange> xchangeAdapterFactory() {
        return new XchangeAdapterFactory(exchangeType);
    }

    @Bean("exchangeServiceFactory")
    public FactoryBean<ExchangeService> exchangeServiceFactory() throws Exception {
        return new ExchangeServiceFactory(exchangeType, xchangeAdapterFactory().getObject());
    }

    /** local wrapping interface for org.knowm.xchange**/
    @Bean("exchangeService")
    public ExchangeService exchangeService() throws Exception {
        return exchangeServiceFactory().getObject();
    }
}
