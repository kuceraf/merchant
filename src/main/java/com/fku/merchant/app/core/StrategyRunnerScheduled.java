package com.fku.merchant.app.core;

import com.fku.merchant.app.strategy.StrategyException;
import com.fku.merchant.app.strategy.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class StrategyRunnerScheduled implements StrategyRunner {

    private final TradingStrategy tradingStrategy;

    /*
     * Control flag decides if the Trading Engine lives or dies.
     */
//    private volatile boolean keepAlive = true;


    @Autowired
    public StrategyRunnerScheduled(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }

    @Override
    // The delay in milliseconds. the period will be measured from the completion time of each preceding invocation
    // If you do not provide a pool-size attribute, the default thread pool will only have a single thread.
    @Scheduled(fixedDelay = 30000)
    public void run() {
        try {
            tradingStrategy.execute();
        } catch (StrategyException e) {
            // TODO error handling
            log.fatal("A FATAL error has occurred in Trading Strategy!", e);
//            keepAlive = false;
        }
    }
}
