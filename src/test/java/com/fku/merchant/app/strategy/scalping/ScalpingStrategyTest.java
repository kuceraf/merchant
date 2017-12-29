package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.strategy.ExchangeTestDataFactory;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class ScalpingStrategyTest {

    private Exchange exchangeMocked = Mockito.mock(Exchange.class, Mockito.RETURNS_DEEP_STUBS);

    @Test
    public void execute_firstTime() throws Exception {
        // Given
        ScalpingStrategy scalpingStrategyTested = new ScalpingStrategy(exchangeMocked);
        when(exchangeMocked.getMarketDataService().getOrderBook(CurrencyPair.BTC_EUR,2))
            .thenReturn(ExchangeTestDataFactory.createOrderBook());

        // When
        scalpingStrategyTested.execute();

        // Then
    }
}
