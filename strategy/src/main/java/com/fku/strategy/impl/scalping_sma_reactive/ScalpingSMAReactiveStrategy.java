package com.fku.strategy.impl.scalping_sma_reactive;

import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.error.StrategyNonFatalException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseTimeSeries;

import java.time.format.DateTimeFormatter;

@Log4j2
public class ScalpingSMAReactiveStrategy implements TradingStrategy {
    final private ExchangeService exchangeService;
    private TechnicalAnalysisContext taContext;

    public ScalpingSMAReactiveStrategy(ExchangeService exchangeService) throws MerchantExchangeException {
        this.exchangeService = exchangeService;
        init();
    }

    private void init() throws MerchantExchangeException {
        taContext = new TechnicalAnalysisContext(new BaseTimeSeries("gdax-historic-rates"));
        exchangeService.getBarObservable().subscribe(bar -> {
            log.info("*** subsciption to subject {}", bar);

            refreshTimeSeries(bar);
            reactOnChange();
        });
    }

    private void refreshTimeSeries(Bar newBar) {
        if (!taContext.timeSeries.isEmpty() &&
                newBar.getEndTime().toInstant().compareTo(taContext.timeSeries.getLastBar().getEndTime().toInstant()) <= 0) {
            log.warn("Exchange returned bar witch has endTime [{}] before or equal to last endTime in series [{}] - skipping",
                    newBar.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    taContext.timeSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            log.warn("Cannot add new bar to time series");
        } else {
            log.info("Adding Bar ({})", newBar);
            taContext.timeSeries.addBar(newBar);
        }
    }

    private void reactOnChange() {
        if (taContext.shouldEnter()) {
            log.info("Sending BUY order");
        } else if (taContext.shouldExit()) {
            log.info("Sending SELL order");
        } else {
            log.info("No action for this time...");
        }
    }
}
