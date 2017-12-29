package com.fku.merchant.config;

import com.fku.merchant.app.exchange.impl.GdaxExchange;
import com.fku.merchant.app.exchange.impl.XchangeFactory;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MerchantApplicationConfig {
    // EXCHANGE
    @Value("${exchange.currencyPair}")
    private String currencyPairProp;
    /** org.knowm.xchange config (3th party library to access cryptocurrency exchanges)**/
    @Bean("xchangeFactory")
    public FactoryBean<Exchange> xchangeFactory() {
        return new XchangeFactory();
    }

    @Bean("xchange")
    public Exchange xchange() throws Exception {
        return xchangeFactory().getObject();
    }

    /** local wrapping interface for org.knowm.xchange**/
    @Bean("exchange")
    public com.fku.merchant.app.exchange.Exchange exchange() throws Exception {
        // TODO the exchange impl could be configurable based on active spring profile
        CurrencyPair currencyPair = new CurrencyPair(this.currencyPairProp);
        return new GdaxExchange(xchange(), currencyPair);
    }
}
