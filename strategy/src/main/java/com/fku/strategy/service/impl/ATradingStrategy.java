package com.fku.strategy.service.impl;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.strategy.service.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class ATradingStrategy implements TradingStrategy {
    protected final ExchangeService exchangeService;
    protected final ExchangeOrderRepository exchangeOrderRepository;

    @Autowired
    public ATradingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        this.exchangeService = exchangeService;
        this.exchangeOrderRepository = exchangeOrderRepository;
    }
    //key = orderId
    protected Map<String, ExchangeOrder> buyOrderStates = new HashMap<>();
    protected Map<String, ExchangeOrder> sellOrderStates = new HashMap<>();

    @Override
    public void execute() throws Exception {
        log.info("Execution of [{}]", this.getClass().getSimpleName());
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws Exception;

    protected void checkProfitability() {
        BigDecimal buyOrdersTotalCost = buyOrderStates.values().stream()
                .map(buyOrder -> buyOrder.getPrice().multiply(buyOrder.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sellOrdersTotalCost = sellOrderStates.values().stream()
                .map(buyOrder -> buyOrder.getPrice().multiply(buyOrder.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Buy orders ({}) total cost {} EUR, sell orders ({}) total cost {} EUR",
                buyOrderStates.size(),
                buyOrdersTotalCost,
                sellOrderStates.size(),
                sellOrdersTotalCost);
    }
}
