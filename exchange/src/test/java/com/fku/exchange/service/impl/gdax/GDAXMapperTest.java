package com.fku.exchange.service.impl.gdax;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRatesTest;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;

import java.io.InputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class GDAXMapperTest {
    @Test
    public void remap_historicRates() throws Exception {
        // given
        // Sample date contains:
        // first bucket start date 1498924740 -> 2017-07-01T17:59+02:00[Europe/Prague] (last bar in time series)
        // second bucket start date 1498924680 -> 2017-07-01T17:58+02:00[Europe/Prague]
        // third bucket start date 1498924620 -> 2017-07-01T17:57+02:00[Europe/Prague]
        // last bucket start  date 1498921200 -> 2017-07-01T17:00+02:00[Europe/Prague]
        // https://api.gdax.com/products/BTC-EUR/candles?start=2017-07-01T10%3A00%3A00.000000-0500&end=2017-07-01T11%3A00%3A00.000000-0500&granularity=60

        InputStream is = GDAXHistoricRatesTest.class.getResourceAsStream("/2017-07-01_product_candles_minute_interval.json");
        ObjectMapper mapper = new ObjectMapper();
        GDAXHistoricRates[] gdaxHistoricRates = mapper.readValue(is, GDAXHistoricRates[].class);

        // when
        List<Bar> barsTested = GDAXMapper.remap(gdaxHistoricRates, 60); // one minutes interval between buckets

        // than
        assertThat(barsTested.get(0).getBeginTime())
                .as("Begin time must be before and time")
                .isBefore(barsTested.get(0).getEndTime());

        assertThat(barsTested.get(0).getBeginTime())
                .as("First bar time must be before last bar time")
                .isBefore(barsTested.get(0).getBeginTime());

        assertThat(barsTested.get(0).getBeginTime())
                .isEqualTo(ZonedDateTime.parse("2017-07-01T17:00+02:00[Europe/Prague]")); // 1498921200 -> 2017-07-01T17:00+02:00[Europe/Prague]
        assertThat(barsTested.get(0).getEndTime())
                .isEqualTo(ZonedDateTime.parse("2017-07-01T17:01+02:00[Europe/Prague]"));

        assertThat(barsTested.get(barsTested.size() - 1).getBeginTime())
                .isEqualTo(ZonedDateTime.parse("2017-07-01T17:59+02:00[Europe/Prague]")); // 1498924740 -> 2017-07-01T17:59+02:00[Europe/Prague]
        assertThat(barsTested.get(barsTested.size() - 1).getEndTime())
                .isEqualTo(ZonedDateTime.parse("2017-07-01T18:00+02:00[Europe/Prague]"));

        assertThat(barsTested.get(0).getTimePeriod())
                .isEqualTo(Duration.ofSeconds(60));

        assertThat(barsTested.get(0))
                .as("Historical data from GDAX (candles) must be remapped time series correctly")
                .extracting(Bar::getOpenPrice, Bar::getMaxPrice, Bar::getMinPrice, Bar::getClosePrice, Bar::getVolume)
                .contains(Decimal.valueOf(2161.91), Decimal.valueOf(2161.91), Decimal.valueOf(2161.91), Decimal.valueOf(2161.91), Decimal.valueOf(0.14));

        assertThat(barsTested.get(barsTested.size() - 1))
                .as("Historical data from GDAX (candles) must be remapped time series correctly")
                .extracting(Bar::getOpenPrice, Bar::getMaxPrice, Bar::getMinPrice, Bar::getClosePrice, Bar::getVolume)
                .contains(Decimal.valueOf(2163.3), Decimal.valueOf(2163.3), Decimal.valueOf(2163.3), Decimal.valueOf(2163.3), Decimal.valueOf(0.04));

        assertThat(barsTested)
                .as("In series from 2017-07-01T17:00 to 2017-07-01T17:59 with granularity 60 seconds must be 59 samples")
                .hasSize(59);
    }
}
