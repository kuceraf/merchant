package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.exchange.Exchange;
import com.fku.merchant.app.exchange.ExchangeTestDataFactory;
import com.fku.merchant.app.strategy.dto.PricePair;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;


public class ScalpingStrategyTest {
    private Exchange exchangeMocked = Mockito.mock(Exchange.class);

    @Test
    public void execute_firstTime() throws Exception {
        // Given
        ScalpingStrategy scalpingStrategyTested = new ScalpingStrategy(exchangeMocked);
        when(exchangeMocked.getCurrentPrices())
            .thenReturn(new PricePair(ExchangeTestDataFactory.CURRENT_BID_PRICE, ExchangeTestDataFactory.CURRENT_ASK_PRICE));

        // When
        scalpingStrategyTested.execute();

        // Then
    }
}
