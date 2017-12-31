package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.exchange.ExchangeTestDataFactory;
import com.fku.merchant.app.core.exception.MerchantStrategyException;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class PriceHelperTest {
    public PriceHelperTest() throws Exception {
    }

    private OrderBook orderBook;

    @Before
    public void setUpBeforeEachTest() throws Exception {
        orderBook = ExchangeTestDataFactory.createOrderBook();
    }

    @Test
    public void getCurrentBidPriceTest() throws Exception {
        Assertions.assertThat(PriceHelper.getCurrentBidPrice(orderBook))
                .isEqualTo(ExchangeTestDataFactory.CURRENT_BID_PRICE);
    }

    @Test
    public void getCurrentAskPriceTest() throws  Exception {
        assertThat(PriceHelper.getCurrentAskPrice(orderBook))
                .isEqualTo(ExchangeTestDataFactory.CURRENT_ASK_PRICE);
    }

    @Test
    public void getCurrentBidPrice_exceptionTest() throws Exception {
        orderBook.getBids().clear();
        assertThatExceptionOfType(MerchantStrategyException.class)
                .isThrownBy(() -> PriceHelper.getCurrentBidPrice(orderBook));
    }

    @Test
    public void calculateAmountOfBaseCurrencyTest() {
        assertThat(PriceHelper.calculateAmountOfBaseCurrency(BigDecimal.valueOf(10), BigDecimal.valueOf(7599.99)))
                .isEqualTo(BigDecimal.valueOf(0.00131579));
    }
}
