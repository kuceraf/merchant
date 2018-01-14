package com.fku.strategy.service.impl;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.service.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public void execute() throws Exception {
        log.info("Execution of [{}]", this.getClass().getSimpleName());
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws Exception;

    private void checkProfitability() throws MerchantStrategyException { // TODO test it in refactor it
        ExchangeOrder lastOrder = exchangeOrderRepository.findLast();
        if(lastOrder != null && lastOrder.isAsk()) {
            List<ExchangeOrder> buyOrders = exchangeOrderRepository.findBids();
            List<ExchangeOrder> sellOrders = exchangeOrderRepository.findAsks();
            CurrencyPair currencyPair = exchangeService.getCurrencyPair();

            BigDecimal buyOrdersTotalCost = buyOrders.stream()
                    .map(buyOrder -> buyOrder.getPrice().multiply(buyOrder.getAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal sellOrdersTotalRevenue = sellOrders.stream()
                    .map(buyOrder -> buyOrder.getPrice().multiply(buyOrder.getAmount()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            log.info("Buy orders [{}] total cost [{}] {}",
                    buyOrders.size(),
                    buyOrdersTotalCost,
                    currencyPair.counter);
            log.info("Sell orders [{}] total revenue [{}] {}",
                    sellOrders.size(),
                    sellOrdersTotalRevenue,
                    currencyPair.counter);

            BigDecimal totalRevenue = sellOrdersTotalRevenue.subtract(buyOrdersTotalCost);
            log.info("When pending sell order filled, total revenue will be [{}] {}",
                    totalRevenue,
                    currencyPair.counter
                    );
            if(BigDecimal.ZERO.compareTo(totalRevenue) > 0) {
                throw new MerchantStrategyException("Strategy is lossy - stopping execution");
            }
        }
    }
}
