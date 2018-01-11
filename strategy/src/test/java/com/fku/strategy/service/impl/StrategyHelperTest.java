package com.fku.strategy.service.impl;

import com.fku.exchange.domain.ExchangeOrder;
import org.junit.Test;

import java.math.BigDecimal;

import static com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory.createExchangeOrder;
import static com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory.createOpenOrders;
import static org.assertj.core.api.Assertions.assertThat;


public class StrategyHelperTest {
    private static final BigDecimal ORDER_BUY_PRICE = BigDecimal.valueOf(13728.24);
    private static final BigDecimal REQUIRED_PROFIT = BigDecimal.valueOf(0.02);
    @Test
    public void calculateSellPriceWithRequiredProfit_test() {
        final BigDecimal EXPECTED_RESULT = BigDecimal.valueOf(14002.80480000).setScale(8);
        assertThat(StrategyHelper.calculateSellPriceWithRequiredProfit(ORDER_BUY_PRICE, REQUIRED_PROFIT))
        .isEqualTo(EXPECTED_RESULT);
    }

    @Test
    public void isOrderFilled_false() {
        assertThat(StrategyHelper.isOrderFilled(createOpenOrders(), createExchangeOrder()))
        .isFalse();
    }

    @Test
    public void isOrderFilled_true() {
        ExchangeOrder filledExchangeOrder = createExchangeOrder();
        filledExchangeOrder.setId("notFromOrderBook");
        assertThat(StrategyHelper.isOrderFilled(createOpenOrders(), filledExchangeOrder))
                .isTrue();
    }
}
