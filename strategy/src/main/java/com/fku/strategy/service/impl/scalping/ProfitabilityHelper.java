package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.domain.ExchangeOrder;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.List;

@Log4j2
public class ProfitabilityHelper {

    private ProfitabilityHelper() {
        throw new AssertionError("This class isn't designed for instantiation");
    }

    public static boolean isProfitable(@Nonnull List<ExchangeOrder> buyOrders,
                                       @Nonnull List<ExchangeOrder> sellOrders,
                                       @Nonnull CurrencyPair currencyPair) {
        BigDecimal buyOrdersTotalCost = buyOrders.stream()
                .map(buyOrder -> buyOrder.getPrice().multiply(buyOrder.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal sellOrdersTotalRevenue = sellOrders.stream()
                .map(sellOrder -> sellOrder.getPrice().multiply(sellOrder.getAmount()))
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
        // strategy is not profitable if totalRevenue is negative (totalRevenue >= 0 is profitable)
        return (BigDecimal.ZERO.compareTo(totalRevenue) <= 0);  // if totalRevenue < 0 than false

    }
}
