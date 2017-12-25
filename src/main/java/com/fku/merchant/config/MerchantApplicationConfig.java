package com.fku.merchant.config;

import com.fku.merchant.app.exchange.ExchangeFactory;
import org.knowm.xchange.Exchange;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
public class MerchantApplicationConfig {
    /** EXCHANGE config **/
    @Bean("exchangeFactory")
    public FactoryBean<Exchange> exchangeFactory() {
        return new ExchangeFactory();
    }

    @Bean("exchange")
    public Exchange exchange() throws Exception {
        return exchangeFactory().getObject();
    }
}
