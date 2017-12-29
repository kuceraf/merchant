package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.exchange.ExchangeTestDataFactory;
import com.fku.merchant.app.strategy.dto.PricePair;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;


public class ScalpingStrategyTest {
    private ExchangeService exchangeServiceMocked = Mockito.mock(ExchangeService.class);

    @Test
    public void execute_firstTime() throws Exception {
        // Given
        ScalpingStrategy scalpingStrategyTested = new ScalpingStrategy(exchangeServiceMocked);
        when(exchangeServiceMocked.getCurrentPrices())
            .thenReturn(new PricePair(ExchangeTestDataFactory.CURRENT_BID_PRICE, ExchangeTestDataFactory.CURRENT_ASK_PRICE));

        // When
        scalpingStrategyTested.execute();

        // Then
    }
}
