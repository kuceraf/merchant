package com.fku.merchant.app.core.runner.impl;

import com.fku.exchange.error.ExchangeNonFatalException;
import com.fku.exchange.service.ExchangeService;
import com.fku.merchant.app.core.ShutdownManager;
import com.fku.merchant.app.core.runner.MerchantRunner;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.error.StrategyNonFatalException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StrategyRunnerScheduled implements MerchantRunner {

    // services
    private final ShutdownManager shutdownManager;
    private final ExchangeService exchangeService;

    @Autowired
    public StrategyRunnerScheduled(ShutdownManager shutdownManager, ExchangeService exchangeService) {
        this.shutdownManager = shutdownManager;
        this.exchangeService = exchangeService;
    }

    @Override
    public void run() {
        log.info("Strategy is run by scheduler");
    }

    // The delay in milliseconds. the period will be measured from the completion time of each preceding invocation
    // If you do not provide a pool-size attribute, the default thread pool will only have a single thread.
    // Scheduler starts automatically, when bean is created by spring
    @Scheduled(fixedDelay = 60000)
    public void scheduledExecution() {
        try {
//            exchangeDataProvider.submit();
//            Long strategyExecutionNo = tradingStrategy.getExecutionNo();
            log.info("----------------------------");
            log.info("BEGIN Strategy execution");
            long startTime = System.currentTimeMillis();
//            tradingStrategy.execute();
            exchangeService.nextBar();
            long stopTime = System.currentTimeMillis();
            long elapsedTime = stopTime - startTime;
            log.info("END Strategy execution (duration [{}] ms)", elapsedTime);
        }
        // Order of catch block is important (always catch specific exeptions first!)
        catch (ExchangeNonFatalException e) {
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
