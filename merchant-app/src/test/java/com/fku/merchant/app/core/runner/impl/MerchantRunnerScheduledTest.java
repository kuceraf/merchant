//package com.fku.merchant.app.core.runner.impl;
//
//import com.fku.exchange.error.ExchangeNonFatalException;
//import com.fku.merchant.app.core.ShutdownManager;
//import com.fku.strategy.error.MerchantStrategyException;
//import com.fku.strategy.TradingStrategy;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.verifyZeroInteractions;
//
//@RunWith(MockitoJUnitRunner.class)
//public class MerchantRunnerScheduledTest {
//
//    @Mock
//    private TradingStrategy tradingStrategyMocked;
//    @Mock
//    private ShutdownManager shutdownManagerMocked;
//
//    private StrategyRunnerScheduled strategyRunnerScheduledTested;
//
//    @Before
//    public void prepareFreshTestedObjectForEachTest() throws Exception {
//        strategyRunnerScheduledTested = new StrategyRunnerScheduled(tradingStrategyMocked, shutdownManagerMocked);
//    }
//
//    @Test
//    public void scheduledExecution_strategyFatalException() throws Exception {
//        // Given
//        doThrow(new MerchantStrategyException("test")).when(tradingStrategyMocked).execute();
//        // When (fatal exception is thrown)
//        strategyRunnerScheduledTested.scheduledExecution();
//        //Then (application must be shutdown)
//        verify(shutdownManagerMocked).initiateShutdown(0);
//    }
//
//    @Test
//    public void scheduledExecution_strategyNonFatalException() throws Exception {
//        // Given
//        doThrow(new ExchangeNonFatalException("test")).when(tradingStrategyMocked).execute();
//        // When (non-fatal exception is thrown)
//        strategyRunnerScheduledTested.scheduledExecution();
//        //Then (application must not be shutdown)
//        verifyZeroInteractions(shutdownManagerMocked);
//    }
//}
//
