package com.fku.exchange.service.impl;


import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.impl.dummy.Constants;
import com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.dto.marketdata.OrderBook;

import static com.fku.exchange.service.impl.dummy.Constants.BASE_CURRENCY_AMOUNT;
import static com.fku.exchange.service.impl.dummy.Constants.COUNTER_CURRENCY_AMOUNT;
import static com.fku.exchange.service.impl.dummy.Constants.INSTRUMENT_LAST_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_ASK_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_BID_PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ExchangeHelperTest {
    public ExchangeHelperTest() throws Exception {
    }

    private OrderBook orderBook;

    @Before
    public void setUpBeforeEachTest() throws Exception {
        orderBook = DummyExchangeDataFactory.getOrderBook(LIMIT_ASK_PRICE, LIMIT_BID_PRICE);
    }

    @Test
    public void getCurrentBidPriceTest() throws Exception {
        assertThat(ExchangeHelper.getCurrentBidPrice(orderBook))
                .isEqualTo(LIMIT_BID_PRICE);
    }

    @Test
    public void getCurrentAskPriceTest() throws  Exception {
        assertThat(ExchangeHelper.getCurrentAskPrice(orderBook))
                .isEqualTo(LIMIT_ASK_PRICE);
    }

    @Test
    public void getCurrentBidPrice_exceptionTest() throws Exception {
        orderBook.getBids().clear();
        assertThatExceptionOfType(MerchantExchangeException.class)
                .isThrownBy(() -> ExchangeHelper.getCurrentBidPrice(orderBook));
    }

    @Test
    public void calculateAmountOfBaseCurrencyTest() {
        assertThat(ExchangeHelper.calculateBaseCurrencyAmount(COUNTER_CURRENCY_AMOUNT, INSTRUMENT_LAST_PRICE))
                .isEqualTo(BASE_CURRENCY_AMOUNT);
    }
}
