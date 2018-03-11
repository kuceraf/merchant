package com.fku.ta.impl;

import com.fku.ta.TechnicalAnalysis;
import lombok.extern.log4j.Log4j2;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.time.format.DateTimeFormatter;

@Log4j2
public class SmaAndClosePriceTA implements TechnicalAnalysis {
    private TimeSeries timeSeries;
    private Strategy strategy;
    private TradingRecord tradingRecord;


    public SmaAndClosePriceTA(TimeSeries timeSeries) {
        this.timeSeries = timeSeries;
        init(timeSeries);
    }

    @Override
    public TimeSeries getTimeSeries() {
        return timeSeries;
    }

    @Override
    public void addBar(Bar bar) {
        timeSeries.addBar(bar);
    }

    @Override
    public boolean shouldEnter() {
        return strategy.shouldEnter(timeSeries.getEndIndex());
    }

    @Override
    public boolean shouldExit() {
        return strategy.shouldExit(timeSeries.getEndIndex());
    }

    private void init(TimeSeries timeSeries) {
        timeSeries.setMaximumBarCount(timeSeries.getBarCount()); // It ensures that your memory consumption won't increase infinitely (the series will turn it into a moving time series.)
        log.info("Initializing SmaAndClosePriceTA (size: {}, start: {}, end: {})",
                timeSeries.getBarCount(),
                timeSeries.getFirstBar().getBeginTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                timeSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

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
}
