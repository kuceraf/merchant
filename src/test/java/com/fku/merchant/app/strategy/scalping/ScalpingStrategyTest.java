package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.exchange.ExchangeTestDataFactory;
import com.fku.merchant.app.repository.order.OrderRepository;
import com.fku.merchant.app.repository.order.domain.CurrencyPricePair;
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
            .thenReturn(new CurrencyPricePair(ExchangeTestDataFactory.CURRENT_BID_PRICE, ExchangeTestDataFactory.CURRENT_ASK_PRICE));
        when(orderRepository.findLastOrder())
                .thenReturn(null);

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1)).getCurrentPrices();
        verify(exchangeServiceMocked, times(1)).placeBuyOrder(ExchangeTestDataFactory.CURRENT_BID_PRICE, ScalpingStrategy.COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }
}
