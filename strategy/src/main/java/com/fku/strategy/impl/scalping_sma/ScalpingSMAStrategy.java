package com.fku.strategy.impl.scalping_sma;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.Granularity;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.impl.ATradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.time.format.DateTimeFormatter;


@Log4j2
public class ScalpingSMAStrategy extends ATradingStrategy implements TradingStrategy, InitializingBean {
    private static final Granularity GRANULARITY = Granularity.ONE_MINUTE;

    /** Close price of the last tick */
    private static Decimal LAST_BAR_CLOSE_PRICE;
    private TimeSeries historicalSeries;
    private Strategy strategy;
    private TradingRecord tradingRecord;

    public ScalpingSMAStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Strategy [{}] initialization", this.getClass());
        // init data
        historicalSeries = exchangeService.getHistoricalTimeSeries(GRANULARITY);
        historicalSeries.setMaximumBarCount(historicalSeries.getBarCount()); // It ensures that your memory consumption won't increase infinitely (the series will turn it into a moving time series.)
        log.info("Historical time series from exchange (size: {}, start: {}, end: {})",
                historicalSeries.getBarCount(),
                historicalSeries.getFirstBar().getBeginTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                historicalSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        LAST_BAR_CLOSE_PRICE = historicalSeries.getBar(historicalSeries.getEndIndex()).getClosePrice();

        // init strategy
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(historicalSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, 20);

        // Buy when SMA (of last x close price) goes over close price
        // Sell when close price goes over SMA
        strategy = new BaseStrategy(
                new OverIndicatorRule(sma, closePriceIndicator),
                new UnderIndicatorRule(sma, closePriceIndicator));


        // init the trading history
        tradingRecord = new BaseTradingRecord();
    }

    @Override
    public TimeSeries getTimeSeries() {
        return historicalSeries;
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        Bar newBar = exchangeService.getHistoricalTimeSeries(GRANULARITY).getLastBar();

        if (newBar.getEndTime().toInstant().compareTo(historicalSeries.getLastBar().getEndTime().toInstant()) <= 0) {
            log.warn("Exchange returned bar witch has endTime [{}] before or equal to last endTime in series [{}] - skipping",
                    newBar.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    historicalSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            return;
        }

        log.info("Adding Bar ({})", newBar);
        historicalSeries.addBar(newBar);

        int endIndex = historicalSeries.getEndIndex();
        if (strategy.shouldEnter(endIndex)) {
            // Our strategy should enter
            log.info("Strategy should ENTER on " + endIndex);
            boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), Decimal.TEN);
            if (entered) {
                Order entry = tradingRecord.getLastEntry();
                log.info("Entered on " + entry.getIndex()
                        + " (price=" + entry.getPrice().doubleValue()
                        + ", amount=" + entry.getAmount().doubleValue() + ")");
            }
        } else if (strategy.shouldExit(endIndex)) {
            // Our strategy should exit
            log.info("Strategy should EXIT on " + endIndex);
            boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), Decimal.TEN);
            if (exited) {
                Order exit = tradingRecord.getLastExit();
                log.info("Exited on " + exit.getIndex()
                        + " (price=" + exit.getPrice().doubleValue()
                        + ", amount=" + exit.getAmount().doubleValue() + ")");
            }
        }
    }
}
