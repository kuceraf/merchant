package com.fku.strategy.impl;

import com.fku.exchange.domain.ExchangeOrder;
import org.junit.Test;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;

import static com.fku.exchange.service.impl.dummy.Constants.BASE_CURRENCY_AMOUNT;
import static com.fku.exchange.service.impl.dummy.Constants.EXISTING_OPEN_ORDER_ID;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_ASK_PRICE;
import static com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder;
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
        assertThat(StrategyHelper.isOrderFilled(getOpenOrdersWithAskOpenOrder(EXISTING_OPEN_ORDER_ID), getExchangeOrder()))
        .isFalse();
    }

    @Test
    public void isOrderFilled_true() {
        ExchangeOrder filledExchangeOrder = getExchangeOrder();
        filledExchangeOrder.setId("notFromOpenOrdes");
        assertThat(StrategyHelper.isOrderFilled(getOpenOrdersWithAskOpenOrder(EXISTING_OPEN_ORDER_ID), filledExchangeOrder))
                .isTrue();
    }

    private static ExchangeOrder getExchangeOrder() {
        return new ExchangeOrder(
                EXISTING_OPEN_ORDER_ID,
                Order.OrderType.ASK,
                LIMIT_ASK_PRICE,
                BASE_CURRENCY_AMOUNT
        );
    }
}
