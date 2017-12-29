package com.fku.merchant.app.strategy;

import com.fku.merchant.app.strategy.scalping.OrderState;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public abstract class ATradingStrategy implements TradingStrategy {
    protected final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;
    protected final Exchange exchange;
    //key = orderId
    protected Map<String, OrderState> buyOrderStates = new HashMap<>();
    protected Map<String, OrderState> sellOrderStates = new HashMap<>();

    @Override
    public void execute() throws StrategyException {
        log.info("Execution of [{}]", this.getClass().getSimpleName());
        checkProfitability();
        executeStrategySpecific();
    }

    protected abstract void executeStrategySpecific() throws StrategyException;

    protected void checkProfitability() {
        BigDecimal buyOrdersTotalCost = buyOrderStates.values().stream()
                .map(buyOrder -> buyOrder.price.multiply(buyOrder.amount))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sellOrdersTotalCost = sellOrderStates.values().stream()
                .map(buyOrder -> buyOrder.price.multiply(buyOrder.amount))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Buy orders ({}) total cost {} EUR, sell orders ({}) total cost {} EUR",
                buyOrderStates.size(),
                buyOrdersTotalCost,
                sellOrderStates.size(),
                sellOrdersTotalCost);
    }

    public ATradingStrategy(@Qualifier("exchange") Exchange exchange) {
        this.exchange = exchange;
    }
}
