package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.domain.ExchangeOrder;
import org.junit.Test;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ProfitabilityHelperTest {
    @Test
    public void isProfitable_true() {
        List<ExchangeOrder> buyOrders = new ArrayList<>();
        buyOrders.add(new ExchangeOrder("1", Order.OrderType.BID, BigDecimal.ONE, BigDecimal.TEN)); // buy for 1
        List<ExchangeOrder> sellOrders = new ArrayList<>();
        sellOrders.add(new ExchangeOrder("2", Order.OrderType.ASK, BigDecimal.TEN, BigDecimal.TEN)); // sell for 10

        assertThat(ProfitabilityHelper.isProfitable(buyOrders, sellOrders, CurrencyPair.BTC_EUR))
            .isTrue();
    }

    @Test
    public void isProfitable_false() {
        List<ExchangeOrder> buyOrders = new ArrayList<>();
        buyOrders.add(new ExchangeOrder("1", Order.OrderType.BID, BigDecimal.TEN, BigDecimal.TEN)); // buy for 10
        List<ExchangeOrder> sellOrders = new ArrayList<>();
        sellOrders.add(new ExchangeOrder("2", Order.OrderType.ASK, BigDecimal.ONE, BigDecimal.TEN)); // sell for 1

        assertThat(ProfitabilityHelper.isProfitable(buyOrders, sellOrders, CurrencyPair.BTC_EUR))
                .isFalse();
    }
}
