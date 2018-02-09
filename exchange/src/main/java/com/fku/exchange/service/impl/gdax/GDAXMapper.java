package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import org.ta4j.core.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GDAXMapper {
    public static TimeSeries remap(GDAXHistoricRates[] gdaxHistoricRates, long durationInSecond) {
        List<Bar> ticks = Arrays.stream(gdaxHistoricRates)
                .sorted(Comparator.comparing(GDAXHistoricRates::getTime)) // time series must be sorted (eg: 0: 2017-07-01T17:00+02:00, 1: 2017-07-01T17:01+02:00 ...)
                .map(rates -> {
                            long epochInMilliseconds = rates.getTime() * 1000; // seconds-based epoch value needs to be converted to milliseconds
                            ZonedDateTime startTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochInMilliseconds), ZoneId.systemDefault());
                            return new BaseBar(
                                    Duration.ofSeconds(durationInSecond),
                                    startTime.plusSeconds(durationInSecond),
                                    Decimal.valueOf(rates.getOpen()),
                                    Decimal.valueOf(rates.getHigh()),
                                    Decimal.valueOf(rates.getLow()),
                                    Decimal.valueOf(rates.getClose()),
                                    Decimal.valueOf(rates.getVolume())
                            );
                        }
                )
                .collect(Collectors.toList());
        return new BaseTimeSeries("gdax-historic-rates", ticks);
    }
}
