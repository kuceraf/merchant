package com.fku.strategy.impl.scalping_ta4j;

import com.fku.analyst.CsvTradesLoader;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.impl.ATradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.*;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;


@Log4j2
public class ScalpingTa4jStrategy extends ATradingStrategy implements InitializingBean {

    /** Close price of the last tick */
    private static Decimal LAST_BAR_CLOSE_PRICE;
    private TimeSeries series;
    private Strategy strategy;
    private TradingRecord tradingRecord;

    public ScalpingTa4jStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Strategy [{}] initialization", this.getClass());
        // init data
        series = CsvTradesLoader.loadBitstampSeries(); // TODO tick from exchange end convert them to time series
        LAST_BAR_CLOSE_PRICE = series.getBar(series.getEndIndex()).getClosePrice();

        // init strategy
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, 4);

        // Buy when SMA goes over close price
        // Sell when close price goes over SMA
        strategy = new BaseStrategy(
                new OverIndicatorRule(sma, closePrice),
                new UnderIndicatorRule(sma, closePrice));


        // Initializing the trading history
        tradingRecord = new BaseTradingRecord();
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        Bar newBar = DummyTickFactory.generateRandomBar(LAST_BAR_CLOSE_PRICE);
        log.info("------------------------------------------------------\n"
                + "Tick "+getExecutionNo()+" added, close price = " + newBar.getClosePrice().toDouble());
        series.addBar(newBar);

        int endIndex = series.getEndIndex();
        if (strategy.shouldEnter(endIndex)) {
            // Our strategy should enter
            log.info("Strategy should ENTER on " + endIndex);
            boolean entered = tradingRecord.enter(endIndex, newBar.getClosePrice(), Decimal.TEN);
            if (entered) {
                Order entry = tradingRecord.getLastEntry();
                log.info("Entered on " + entry.getIndex()
                        + " (price=" + entry.getPrice().toDouble()
                        + ", amount=" + entry.getAmount().toDouble() + ")");
            }
        } else if (strategy.shouldExit(endIndex)) {
            // Our strategy should exit
            log.info("Strategy should EXIT on " + endIndex);
            boolean exited = tradingRecord.exit(endIndex, newBar.getClosePrice(), Decimal.TEN);
            if (exited) {
                Order exit = tradingRecord.getLastExit();
                log.info("Exited on " + exit.getIndex()
                        + " (price=" + exit.getPrice().toDouble()
                        + ", amount=" + exit.getAmount().toDouble() + ")");
            }
        }

    }
}
