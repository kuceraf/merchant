package com.fku.strategy.impl.scalping_sma;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.Granularity;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.error.StrategyNonFatalException;
import com.fku.strategy.impl.ATradingStrategy;
import com.fku.ta.TechnicalAnalysis;
import com.fku.ta.impl.SmaAndClosePriceTA;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.*;

import java.time.format.DateTimeFormatter;


@Log4j2
public class ScalpingSMAStrategy extends ATradingStrategy implements TradingStrategy, InitializingBean {
    private static final Granularity GRANULARITY = Granularity.ONE_MINUTE;

    /** Close price of the last tick */
    private TechnicalAnalysis technicalAnalysis;

    public ScalpingSMAStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TimeSeries timeSeries = exchangeService.getHistoricalTimeSeries(GRANULARITY);
        technicalAnalysis = new SmaAndClosePriceTA(timeSeries);
    }

    @Override
    public TechnicalAnalysis getTechnicalAnalysis() {
        return technicalAnalysis;
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        Bar newBar = exchangeService.getHistoricalTimeSeries(GRANULARITY).getLastBar();

        TimeSeries timeSeries = technicalAnalysis.getTimeSeries();
        if (newBar.getEndTime().toInstant().compareTo(timeSeries.getLastBar().getEndTime().toInstant()) <= 0) {
            log.warn("Exchange returned bar witch has endTime [{}] before or equal to last endTime in series [{}] - skipping",
                    newBar.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    timeSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            throw new StrategyNonFatalException("Cannot add new bar to time series");
        }

        log.info("Adding Bar ({})", newBar);
        technicalAnalysis.addBar(newBar);

//        int endIndex = timeSeries.getEndIndex();
//        if (strategy.shouldEnter(endIndex)) {
//            // sma(20) > closePrice
//            log.info("Strategy should ENTER on " + endIndex);
//            boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), Decimal.TEN);
//            if (entered) {
//                Order entry = tradingRecord.getLastEntry();
//                log.info("Entered on " + entry.getIndex()
//                        + " (price=" + entry.getPrice().doubleValue()
//                        + ", amount=" + entry.getAmount().doubleValue() + ")");
//            }
//        } else if (strategy.shouldExit(endIndex)) {
//            // Our strategy should exit
//            log.info("Strategy should EXIT on " + endIndex);
//            boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), Decimal.TEN);
//            if (exited) {
//                Order exit = tradingRecord.getLastExit();
//                log.info("Exited on " + exit.getIndex()
//                        + " (price=" + exit.getPrice().doubleValue()
//                        + ", amount=" + exit.getAmount().doubleValue() + ")");
//            }
//        }
    }
}
