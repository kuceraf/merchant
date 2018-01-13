package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory;
import com.fku.strategy.service.impl.StrategyHelper;
import com.fku.strategy.service.impl.StrategyHelperTest;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;

import static com.fku.exchange.service.impl.dummy.Constants.BASE_CURRENCY_AMOUNT;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_ASK_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_BID_PRICE;
import static org.mockito.Mockito.*;


public class ScalpingStrategyTest {
    private ExchangeService exchangeServiceMocked = mock(ExchangeService.class);
    private ExchangeOrderRepository exchangeOrderRepository = mock(ExchangeOrderRepository.class);
    private ScalpingStrategy scalpingStrategyTested;

    @Before
    public void setUp() throws Exception {
        scalpingStrategyTested = new ScalpingStrategy(exchangeServiceMocked, exchangeOrderRepository);
    }

    @Test
    public void execute_firstTimeOrder() throws Exception {
        // Given
        when(exchangeServiceMocked.getCurrentPrices())
            .thenReturn(new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE));
        when(exchangeOrderRepository.findLast())
                .thenReturn(null);

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1)).getCurrentPrices();
        verify(exchangeServiceMocked, times(1)).placeBuyOrder(LIMIT_BID_PRICE, ScalpingStrategy.COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }

    @Test
    public void execute_trySellWithProfit_lastOrderFulfilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("someId", Order.OrderType.BID, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT));
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("someDifferentId"));
        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1))
                .placeOrder(Order.OrderType.ASK, BASE_CURRENCY_AMOUNT, BigDecimal.valueOf(14002.80480000).setScale(8));
    }

    @Test
    public void execute_trySellWithProfit_lastOrderNotFulfilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("sameId", Order.OrderType.BID, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT));
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("sameId"));
        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, never())
                .placeOrder(Order.OrderType.ASK, BASE_CURRENCY_AMOUNT, BigDecimal.valueOf(14002.80480000).setScale(8));
    }
}
