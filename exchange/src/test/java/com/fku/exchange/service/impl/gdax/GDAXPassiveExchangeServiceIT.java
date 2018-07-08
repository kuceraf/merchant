package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.Granularity;
import io.reactivex.Observable;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gdax.GDAXExchange;
import org.ta4j.core.Bar;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class GDAXPassiveExchangeServiceIT {
    private static Exchange xchangeReal;
    private static final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;
    private GDAXPassiveExchangeService gdaxExchangeServiceTested;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ExchangeSpecification exSpec = new GDAXExchange().getDefaultExchangeSpecification();
        xchangeReal = ExchangeFactory.INSTANCE.createExchange(exSpec);
    }

    @Before
    public void setUp() throws Exception {
        gdaxExchangeServiceTested = new GDAXPassiveExchangeService(xchangeReal, CURRENCY_PAIR);
    }

    @Test
    public void getHistoricalDataSeries_5MinGranularity() throws Exception {
        List<Bar> bars = gdaxExchangeServiceTested.getHistoricalBars();
        assertThat(bars).hasSize(351);

        for(int i =0; i < bars.size(); i++) {
            Duration betweenPeriod = Duration.between(
                    bars.get(i).getBeginTime() , bars.get(i +1).getBeginTime()
            );
            assertThat(betweenPeriod.getSeconds())
                    .as("Duration between two successive date in time series with 5 min granularity must be 5 min.")
                    .isEqualTo(Granularity.FIVE_MINUTES.getSeconds());
        }
    }

    @Test
    public void observeBars() throws Exception {
        Observable<Bar> barObservable = gdaxExchangeServiceTested.getBarObservable();
        barObservable.subscribe(bar -> {
            System.out.println(bar);
        } );
    }
}
