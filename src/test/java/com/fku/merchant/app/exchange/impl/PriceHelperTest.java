package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.exchange.impl.dummy.DummyExchangeDataFactory;
import com.fku.merchant.app.exchange.PriceHelper;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.dto.marketdata.OrderBook;

import static com.fku.merchant.app.exchange.impl.dummy.DummyExchangeDataFactory.BASE_CURRENCY_AMOUNT;
import static com.fku.merchant.app.exchange.impl.dummy.DummyExchangeDataFactory.COUNTER_CURRENCY_AMOUNT;
import static com.fku.merchant.app.exchange.impl.dummy.DummyExchangeDataFactory.INSTRUMENT_LAST_PRICE;
import static org.assertj.core.api.Assertions.*;

public class PriceHelperTest {
    public PriceHelperTest() throws Exception {
    }

    private OrderBook orderBook;

    @Before
    public void setUpBeforeEachTest() throws Exception {
        orderBook = DummyExchangeDataFactory.createOrderBook();
    }

    @Test
    public void getCurrentBidPriceTest() throws Exception {
        assertThat(PriceHelper.getCurrentBidPrice(orderBook))
                .isEqualTo(DummyExchangeDataFactory.CURRENT_BID_PRICE);
    }

    @Test
    public void getCurrentAskPriceTest() throws  Exception {
        assertThat(PriceHelper.getCurrentAskPrice(orderBook))
                .isEqualTo(DummyExchangeDataFactory.CURRENT_ASK_PRICE);
    }

    @Test
    public void getCurrentBidPrice_exceptionTest() throws Exception {
        orderBook.getBids().clear();
        assertThatExceptionOfType(MerchantExchangeException.class)
                .isThrownBy(() -> PriceHelper.getCurrentBidPrice(orderBook));
    }

    @Test
    public void calculateAmountOfBaseCurrencyTest() {
        assertThat(PriceHelper.calculateBaseCurrencyAmount(COUNTER_CURRENCY_AMOUNT, INSTRUMENT_LAST_PRICE))
                .isEqualTo(BASE_CURRENCY_AMOUNT);
    }
}
