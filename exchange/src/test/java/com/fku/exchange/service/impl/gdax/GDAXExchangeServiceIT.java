package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.Granularity;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gdax.GDAXExchange;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class GDAXExchangeServiceIT {
    private static Exchange xchangeReal;
    private static final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;
    private GDAXExchangeService gdaxExchangeServiceTested;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ExchangeSpecification exSpec = new GDAXExchange().getDefaultExchangeSpecification();
        xchangeReal = ExchangeFactory.INSTANCE.createExchange(exSpec);
    }

    @Before
    public void setUp() throws Exception {
        gdaxExchangeServiceTested = new GDAXExchangeService(xchangeReal, CURRENCY_PAIR);
    }

    @Test
    public void getHistoricalDataSeries() throws Exception {
        TimeSeries historicalTimeSeries = gdaxExchangeServiceTested.getHistoricalTimeSeries(LocalDateTime.parse("2017-07-01T10:00:00"),
                                                                                            LocalDateTime.parse("2017-07-01T11:00:00"),
                                                                                            Granularity.ONE_MINUTE);
        assertThat(historicalTimeSeries.getBarData()).hasSize(59);
    }

    @Test
    public void getHistoricalDataSeries_5MinGranularity() throws Exception {
        TimeSeries historicalTimeSeries = gdaxExchangeServiceTested.getHistoricalTimeSeries(Granularity.FIVE_MINUTES);
        assertThat(historicalTimeSeries.getBarData()).hasSize(351);

        for(int i =0; i < historicalTimeSeries.getEndIndex(); i++) {
            Duration betweenPeriod = Duration.between(
                    historicalTimeSeries.getBar(i).getBeginTime() , historicalTimeSeries.getBar(i +1).getBeginTime()
            );
            assertThat(betweenPeriod.getSeconds())
                    .as("Duration between two successive date in time series with 5 min granularity must be 5 min.")
                    .isEqualTo(Granularity.FIVE_MINUTES.getSeconds());
        }
    }

//    @Test
//    public void getBar() throws Exception {
//        Bar bar = gdaxExchangeServiceTested.getBar(Granularity.FIVE_MINUTES);
//        assertThat(bar).isNotNull();
//        assertThat(bar.getClosePrice()).isNotNull();
//        Duration betweenBeginAndEnd = Duration.between(
//                bar.getBeginTime(), bar.getEndTime()
//        );
//        assertThat(betweenBeginAndEnd.getSeconds())
//                .isEqualTo(Granularity.FIVE_MINUTES.getSeconds());
//    }
}
