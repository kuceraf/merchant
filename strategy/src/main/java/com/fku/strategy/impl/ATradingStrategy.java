package com.fku.strategy.impl;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.TradingStrategy;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;

@Log4j2
public abstract class ATradingStrategy {
    protected final ExchangeService exchangeService;
    protected final ExchangeOrderRepository exchangeOrderRepository;
    private Long executionNo = 0L;

    @Setter
    @Value("${strategy.counterCurrencyBuyOrderAmount}")
    protected BigDecimal counterCurrencyBuyOrderAmount;

    @Setter
    @Value("${strategy.minimumPercentageProfit}")
    protected BigDecimal minimumPercentageProfit; // The minimum % gain was to achieve before placing a SELL order.

    @Autowired
    public ATradingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        this.exchangeService = exchangeService;
        this.exchangeOrderRepository = exchangeOrderRepository;
    }

    public Long getExecutionNo() {
        return executionNo;
    }

    public void execute() throws Exception {
        log.debug("Execution of [{}]", this.getClass().getSimpleName());
        executionNo++;
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws Exception;

    protected void checkProfitability() throws MerchantStrategyException {
        // no check by default
    }
}
