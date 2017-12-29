package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.exchange.ExchangeTestDataFactory;
import com.fku.merchant.app.exchange.MerchantExchangeException;
import com.fku.merchant.app.exchange.impl.PriceHelper;
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
        assertThatExceptionOfType(MerchantExchangeException.class)
                .isThrownBy(() -> PriceHelper.getCurrentBidPrice(orderBook));
    }
}
