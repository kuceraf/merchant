package com.fku.merchant.config;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.repository.impl.OrderRepositoryImpl;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.SupportedExchangeType;
import com.fku.merchant.config.facotry.ExchangeServiceFactory;
import com.fku.merchant.config.facotry.TradingStrategyFactory;
import com.fku.merchant.config.facotry.XchangeAdapterFactory;
import com.fku.strategy.SupportedStrategyType;
import com.fku.strategy.TradingStrategy;
import org.knowm.xchange.Exchange;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@Configuration
@EnableScheduling
public class MerchantApplicationConfig {
    @Value("${exchange.type}")
    private String exchangeTypeProperty;
    private SupportedExchangeType exchangeType;

    @Value("${strategy.type}")
    private String strategyTypeProperty;
    private SupportedStrategyType strategyType;

    @PostConstruct
    private void setUp() {
        exchangeType = SupportedExchangeType.valueOf(exchangeTypeProperty.toUpperCase());
        strategyType = SupportedStrategyType.valueOf(strategyTypeProperty.toUpperCase());
    }

    // *** EXCHANGE CONFIG ***
    /** org.knowm.xchange config (3th party library to access cryptocurrency exchanges)**/
    @Bean("xchangeAdapterFactory")
    public FactoryBean<Exchange> xchangeAdapterFactory() {
        return new XchangeAdapterFactory(exchangeType);
    }

    @Bean("exchangeServiceFactory")
    public FactoryBean<ExchangeService> exchangeServiceFactory() throws Exception {
        return new ExchangeServiceFactory(exchangeType, xchangeAdapterFactory().getObject());
    }

    // *** REPOSITORY ***
    @Bean
    public ExchangeOrderRepository exchangeOrderRepository() {
        return new OrderRepositoryImpl();
    }

    // *** STRATEGY CONFIG ***
    @Bean("tradingStrategyFactory")
    public FactoryBean<TradingStrategy> tradingStrategyFactory() throws Exception {
        return new TradingStrategyFactory(strategyType, exchangeOrderRepository(), exchangeServiceFactory().getObject());
    }

    @Bean("tradingStrategy")
    public TradingStrategy tradingStrategy() throws Exception {
        return tradingStrategyFactory().getObject();
    }
}
