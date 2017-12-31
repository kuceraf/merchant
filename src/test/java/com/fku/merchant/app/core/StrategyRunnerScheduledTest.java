package com.fku.merchant.app.core;

import com.fku.merchant.app.core.exception.MerchantStrategyException;
import com.fku.merchant.app.strategy.TradingStrategy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class StrategyRunnerScheduledTest {

    @Mock
    private TradingStrategy tradingStrategyMocked;
    @Mock
    private ShutdownManager shutdownManagerMocked;

    @Test
    public void scheduledExecution_strategyException() throws Exception {
        // Given
        StrategyRunnerScheduled strategyRunnerScheduledTested = new StrategyRunnerScheduled(tradingStrategyMocked, shutdownManagerMocked);
        Mockito.doThrow(new MerchantStrategyException("test")).when(tradingStrategyMocked).execute();
        // When
        strategyRunnerScheduledTested.scheduledExecution();
        //Then
        Mockito.verify(shutdownManagerMocked).initiateShutdown(0);
    }
}

