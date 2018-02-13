package com.fku.strategy.impl.scalping_sma;

import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.Granularity;
import com.fku.strategy.impl.ATradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Log4j2
public class ScalpingSMAStrategy extends ATradingStrategy implements InitializingBean {
    private static final String START_TIME = "2017-07-01T10:00:00.000000-0500";
    private static final String END_TIME = "2017-07-01T11:00:00.000000-0500";
    private static final String GRANULARITY = "60";

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
//        historicalSeries = exchangeService.getHistoricalTimeSeries(START_TIME, END_TIME, GRANULARITY);
        LAST_BAR_CLOSE_PRICE = historicalSeries.getBar(historicalSeries.getEndIndex()).getClosePrice();

        // init strategy
        ClosePriceIndicator closePrice = new ClosePriceIndicator(historicalSeries);
        SMAIndicator sma = new SMAIndicator(closePrice, 4);

        // Buy when SMA goes over close price
        // Sell when close price goes over SMA
        strategy = new BaseStrategy(
                new OverIndicatorRule(sma, closePrice),
                new UnderIndicatorRule(sma, closePrice));


        // Initializing the trading history
        tradingRecord = new BaseTradingRecord();
    }

    TimeSeries getHistoricalTimeSeriesWith15minGranularity(int numberOfPeriod) throws Exception {
//        Instant endTime = Instant.now();
//        Instant startTime = Instant.from(LocalTime.now().minusMinutes(numberOfPeriod * 15L));
        LocalDateTime endDateTime = LocalDateTime.now();
        LocalDateTime startDateTime = endDateTime.minusSeconds(numberOfPeriod * Granularity.FIVETEN_MINUTES.getSeconds());
        return exchangeService.getHistoricalTimeSeries(startDateTime, endDateTime, Granularity.FIVETEN_MINUTES); // startDateTime need not be taken into account, then it returns longer time series
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        Bar newBar = DummyBarFactory.generateRandomBar(LAST_BAR_CLOSE_PRICE);
        log.info("------------------------------------------------------\n"
                + "Tick "+getExecutionNo()+" added, close price = " + newBar.getClosePrice().doubleValue());
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
