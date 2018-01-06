package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.repository.OrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory;
import org.junit.Test;

import static org.mockito.Mockito.*;


public class ScalpingStrategyTest {
    private ExchangeService exchangeServiceMocked = mock(ExchangeService.class);
    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Test
    public void execute_firstTimeOrder() throws Exception {
        // Given
        ScalpingStrategy scalpingStrategyTested = new ScalpingStrategy(exchangeServiceMocked, orderRepository);
        when(exchangeServiceMocked.getCurrentPrices())
            .thenReturn(DummyExchangeDataFactory.createInstrumentPrice());
        when(orderRepository.findLastOrder())
                .thenReturn(null);

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1)).getCurrentPrices();
        verify(exchangeServiceMocked, times(1)).placeBuyOrder(DummyExchangeDataFactory.CURRENT_BID_PRICE, ScalpingStrategy.COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }
}