package com.fku.merchant.config.facotry;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.SupportedStrategyType;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.impl.scalping.ScalpingStrategy;
import com.fku.strategy.impl.scalping_ta4j.ScalpingTa4jStrategy;
import org.springframework.beans.factory.config.AbstractFactoryBean;

public class TradingStrategyFactory extends AbstractFactoryBean<TradingStrategy> {
    private SupportedStrategyType strategyType;
    private ExchangeOrderRepository exchangeOrderRepository;
    private ExchangeService exchangeService;

    public TradingStrategyFactory(SupportedStrategyType strategyType,
                                  ExchangeOrderRepository exchangeOrderRepository,
                                  ExchangeService exchangeService) {
        this.strategyType = strategyType;
        this.exchangeOrderRepository = exchangeOrderRepository;
        this.exchangeService = exchangeService;
    }

    @Override
    public Class<?> getObjectType() {
        return TradingStrategy.class;
    }

    @Override
    protected TradingStrategy createInstance() throws Exception {
        switch (strategyType) {
            case SCALPING:
                return new ScalpingStrategy(exchangeService, exchangeOrderRepository);
            case SCALPING_TA4J:
                return new ScalpingTa4jStrategy(exchangeService, exchangeOrderRepository);
            default:
                throw new IllegalStateException("Unsupported strategy: " + strategyType.name());
        }
    }
}
