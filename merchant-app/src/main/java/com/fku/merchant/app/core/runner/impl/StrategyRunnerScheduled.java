package com.fku.merchant.app.core.runner.impl;

import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.merchant.app.core.ShutdownManager;
import com.fku.merchant.app.core.runner.StrategyRunner;
import com.fku.strategy.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StrategyRunnerScheduled implements StrategyRunner {

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
    // Scheduler starts automatically, when bean is created by spring
    @Scheduled(fixedDelay = 60000)
    public void scheduledExecution() {
        try {
            Long strategyExecutionNo = tradingStrategy.getExecutionNo();
            log.info("----------------------------");
            log.info("BEGIN Strategy execution (number [{}])", strategyExecutionNo);
            long startTime = System.currentTimeMillis();

            tradingStrategy.execute();

            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            log.info("END Strategy execution (number [{}], duration [{}] ms)", strategyExecutionNo, elapsedTime);
        }
        // Order of catch block is important (always catch specific exeptions first!)
        catch (MerchantExchangeNonFatalException e) {
            log.warn("A NON-FATAL error has occurred in Trading Strategy, keeping strategy alive", e);
            // TODO send warning mail and log to DB
        }
        catch (Exception e) {
            log.fatal("A FATAL error has occurred in Trading Strategy!", e);
            shutdownManager.initiateShutdown(0);
            // TODO send error mail and log to DB
        }
    }
}
