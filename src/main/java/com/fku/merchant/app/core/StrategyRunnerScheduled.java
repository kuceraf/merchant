package com.fku.merchant.app.core;

import com.fku.merchant.app.strategy.StrategyException;
import com.fku.merchant.app.strategy.TradingStrategy;
import com.fku.merchant.app.util.ShutdownManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StrategyRunnerScheduled implements StrategyRunner {
    Long strategyExecutionNo = 1L;

    // services
    private final TradingStrategy tradingStrategy;
    private final ShutdownManager shutdownManager;

    @Autowired
    public StrategyRunnerScheduled(TradingStrategy tradingStrategy, ShutdownManager shutdownManager) {
        this.tradingStrategy = tradingStrategy;
        this.shutdownManager = shutdownManager;
    }

    @Override
    public void runStrategy() {
        log.info("Strategy is run by scheduler");
    }

    // The delay in milliseconds. the period will be measured from the completion time of each preceding invocation
    // If you do not provide a pool-size attribute, the default thread pool will only have a single thread.
    @Scheduled(fixedDelay = 30000)
    public void scheduledExecution() {
        try {
            log.info("Strategy execution number [{}] BEGIN", strategyExecutionNo);
            long startTime = System.currentTimeMillis();
            tradingStrategy.execute();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            log.info("Strategy execution number [{}] END", strategyExecutionNo);
            log.info("Strategy execution duration [{}] ms", elapsedTime);
        } catch (StrategyException e) {
            log.fatal("A FATAL error has occurred in Trading Strategy!", e);
            shutdownManager.initiateShutdown(0);
        }

    }
}
