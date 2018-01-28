package com.fku.strategy.impl;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

@Log4j2
public abstract class ATradingStrategy implements TradingStrategy {
    protected final ExchangeService exchangeService;
    protected final ExchangeOrderRepository exchangeOrderRepository;
    private Long executionNo = 1L;

    @Autowired
    public ATradingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        this.exchangeService = exchangeService;
        this.exchangeOrderRepository = exchangeOrderRepository;
    }

    @Override
    public Long getExecutionNo() {
        return executionNo;
    }

    @Override
    public void execute() throws Exception {
        log.info("Execution of [{}]", this.getClass().getSimpleName());
        executionNo++;
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws Exception;

    protected void checkProfitability() throws MerchantStrategyException {
        // no check by default
    }
}
