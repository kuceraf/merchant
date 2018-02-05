package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import org.ta4j.core.*;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GDAXMapper {
    public static TimeSeries remap(GDAXHistoricRates[] gdaxHistoricRates) {
        List<Bar> ticks = Arrays.stream(gdaxHistoricRates)
                .map(rates -> {
                            long epochInMilliseconds = rates.getTime() * 1000; // seconds-based epoch value needs to be converted to milliseconds
                            ZonedDateTime tradeTimestamp = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochInMilliseconds), ZoneId.systemDefault());
                            return new BaseBar(
                                    tradeTimestamp,
                                    rates.getOpen(),
                                    rates.getHigh(),
                                    rates.getLow(),
                                    rates.getClose(),
                                    rates.getVolume()
                            );
                        }
                )
                .collect(Collectors.toList());
        return new BaseTimeSeries("gdax-historic-rates", ticks);
    }
}
