package com.fku.merchant.app.strategy.helper;

import com.fku.merchant.app.strategy.ExchangeTestDataFactory;
import com.fku.merchant.app.strategy.StrategyException;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

public class PriceHelperTest {
    private static final BigDecimal CURRENT_BID_PRICE = BigDecimal.valueOf(13728.24);
    private static final BigDecimal CURRENT_ASK_PRICE = BigDecimal.valueOf(13728.25);
    public PriceHelperTest() throws Exception {
    }

    private OrderBook orderBook;

    @Before
    public void setUpBeforeEachTest() throws Exception {
        orderBook = ExchangeTestDataFactory.createOrderBook();
    }

    @Test
    public void getCurrentBidPriceTest() throws Exception {
        assertThat(PriceHelper.getCurrentBidPrice(orderBook))
                .isEqualTo(CURRENT_BID_PRICE);
    }

    @Test
    public void getCurrentAskPriceTest() throws  Exception {
        assertThat(PriceHelper.getCurrentAskPrice(orderBook))
                .isEqualTo(CURRENT_ASK_PRICE);
    }

    @Test
    public void getCurrentBidPrice_exceptionTest() throws Exception {
        orderBook.getBids().clear();
        assertThatExceptionOfType(StrategyException.class)
                .isThrownBy(() -> PriceHelper.getCurrentBidPrice(orderBook));
    }
}
