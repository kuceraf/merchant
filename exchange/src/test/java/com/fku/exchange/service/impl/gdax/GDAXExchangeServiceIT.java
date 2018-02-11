package com.fku.exchange.service.impl.gdax;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gdax.GDAXExchange;
import org.ta4j.core.TimeSeries;

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
        String startTime = "2017-07-01T10:00:00.000000-0500";
        String endTime = "2017-07-01T11:00:00.000000-0500";
        String granularity = "60";
        TimeSeries historicalTimeSeries = gdaxExchangeServiceTested.getHistoricalTimeSeries(startTime, endTime, granularity);
        assertThat(historicalTimeSeries.getBarData()).hasSize(59);
    }

    @Test
    public void getTicks() throws Exception {
        gdaxExchangeServiceTested.getTicker();
    }

}
