package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;

import static com.fku.exchange.service.impl.dummy.Constants.BASE_CURRENCY_AMOUNT;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_ASK_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_BID_PRICE;
import static org.mockito.Mockito.*;


public class ScalpingStrategyTest {
    private static final BigDecimal COUNTER_CURRENCY_BUY_ORDER_AMOUNT = BigDecimal.valueOf(700);
    private static final BigDecimal MINIMUM_PERCENTAGE_PROFIT = BigDecimal.valueOf(0.02);

    private ExchangeService exchangeServiceMocked = mock(ExchangeService.class);
    private ExchangeOrderRepository exchangeOrderRepository = mock(ExchangeOrderRepository.class);
    private ScalpingStrategy scalpingStrategyTested;

    @Before
    public void setUp() throws Exception {
        scalpingStrategyTested = new ScalpingStrategy(exchangeServiceMocked, exchangeOrderRepository);
        scalpingStrategyTested.setCounterCurrencyBuyOrderAmount(COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
        scalpingStrategyTested.setMinimumPercentageProfit(MINIMUM_PERCENTAGE_PROFIT);
    }

    @Test
    public void execute_firstTimeOrder() throws Exception {
        // Given
        when(exchangeServiceMocked.getCurrentPrices())
            .thenReturn(new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE));
        when(exchangeOrderRepository.findLast())
                .thenReturn(null); // no previous orders

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1)).getCurrentPrices();
        verify(exchangeServiceMocked, times(1)).placeBuyOrder(LIMIT_BID_PRICE, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }

    @Test
    public void execute_sellWithProfitIfLastBuyOrderIsFilled_lastOrderFilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("someId", Order.OrderType.BID, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT)); // last buy order
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("someDifferentId")); // last buy order's id is not in open orders
        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1))
                .placeOrder(Order.OrderType.ASK, BASE_CURRENCY_AMOUNT, BigDecimal.valueOf(14002.80480000).setScale(8));
    }

    @Test
    public void execute_sellWithProfitIfLastBuyOrderIsFilled_lastOrderNotFilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("theSameId", Order.OrderType.BID, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT)); // last buy order
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("theSameId")); // last buy order's id is in open orders
        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, never())
                .placeOrder(Order.OrderType.ASK, BASE_CURRENCY_AMOUNT, BigDecimal.valueOf(14002.80480000).setScale(8));
    }

    @Test
    public void execute_buyIfLastSellOrderIsFilled_lastOrderFilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("someId", Order.OrderType.ASK, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT)); // last sell order
        when(exchangeServiceMocked.getCurrentPrices())
                .thenReturn(new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE.add(BigDecimal.ONE))); // last sell order has been filled because the price rise
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("sameDifferentId")); // last sell order's id is not in open orders

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, times(1))
                .placeBuyOrder(LIMIT_BID_PRICE, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }

    @Test
    public void execute_buyIfLastSellOrderIsFilled__lastOrderNotFilled() throws Exception {
        // Given
        when(exchangeOrderRepository.findLast())
                .thenReturn(new ExchangeOrder("theSameId", Order.OrderType.ASK, LIMIT_BID_PRICE, BASE_CURRENCY_AMOUNT)); // last sell order
        when(exchangeServiceMocked.getCurrentPrices())
                .thenReturn(new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE.subtract(BigDecimal.ONE))); // last sell order has not been filled because the price fall
        when(exchangeServiceMocked.getOpenOrders())
                .thenReturn(DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder("theSameId")); // last sell order's id still exists in open orders

        // When
        scalpingStrategyTested.execute();

        // Then
        verify(exchangeServiceMocked, never())
                .placeBuyOrder(LIMIT_BID_PRICE, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
    }
}
