package com.fku.strategy.service.impl;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.service.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public abstract class ATradingStrategy implements TradingStrategy {
    protected final ExchangeService exchangeService;
    protected final ExchangeOrderRepository exchangeOrderRepository;

    @Autowired
    public ATradingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        this.exchangeService = exchangeService;
        this.exchangeOrderRepository = exchangeOrderRepository;
    }

    @Override
    public void execute() throws Exception {
        log.info("Execution of [{}]", this.getClass().getSimpleName());
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws Exception;

    protected void checkProfitability() throws MerchantStrategyException {
        // no check by default
    }
}
