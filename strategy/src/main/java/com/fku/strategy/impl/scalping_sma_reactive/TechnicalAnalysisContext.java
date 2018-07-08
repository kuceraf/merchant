package com.fku.strategy.impl.scalping_sma_reactive;

import lombok.extern.log4j.Log4j2;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.time.format.DateTimeFormatter;

@Log4j2
public class TechnicalAnalysisContext {
    public TimeSeries timeSeries;
    public Strategy strategy;
    private TradingRecord tradingRecord;

    public TechnicalAnalysisContext(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        init(timeSeries);
    }

    private void init(TimeSeries timeSeries) {
        timeSeries.setMaximumBarCount(20); // It ensures that your memory consumption won't increase infinitely (the series will turn it into a moving time series.)
//        log.info("Initializing SmaAndClosePriceTA (size: {}, start: {}, end: {})",
//                timeSeries.getBarCount(),
//                timeSeries.getFirstBar().getBeginTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
//                timeSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // init strategy
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, 20);

        // Buy when SMA (of last x close price) goes over close price
        // Sell when close price goes over SMA
        strategy = new BaseStrategy(
                // entry if sma(20) > closePrice
                new OverIndicatorRule(sma, closePriceIndicator),
                // exit if sma(20) < closePrice
                new UnderIndicatorRule(sma, closePriceIndicator));

        // init the trading history
        tradingRecord = new BaseTradingRecord();
    }

    public boolean shouldEnter() {
        return strategy.shouldEnter(timeSeries.getEndIndex());
    }

    public boolean shouldExit() {
        return strategy.shouldExit(timeSeries.getEndIndex());
    }
}
