package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.springframework.util.ObjectUtils;
import org.ta4j.core.*;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class GDAXMapper {
    public static List<Bar> remap(GDAXHistoricRates[] gdaxHistoricRates, long periodInSecond) {
        return Arrays.stream(gdaxHistoricRates)
                .sorted(Comparator.comparing(GDAXHistoricRates::getTime)) // time series must be sorted (eg: 0: 2017-07-01T17:00+02:00, 1: 2017-07-01T17:01+02:00 ...)
                .map(rates -> {
                            long epochInMilliseconds = rates.getTime() * 1000; // seconds-based epoch value needs to be converted to milliseconds
                            ZonedDateTime startDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochInMilliseconds), ZoneId.systemDefault());
                            return new BaseBar(
                                    Duration.ofSeconds(periodInSecond),
                                    startDateTime.plusSeconds(periodInSecond),
                                    Decimal.valueOf(rates.getOpen()),
                                    Decimal.valueOf(rates.getHigh()),
                                    Decimal.valueOf(rates.getLow()),
                                    Decimal.valueOf(rates.getClose()),
                                    Decimal.valueOf(rates.getVolume())
                            );
                        }
                )
                .collect(Collectors.toList());
//        return new BaseTimeSeries("gdax-historic-rates", bars);
    }

//    /**
//     *
//     * @param ticker - Snapshot information about the last trade (tick)
//     * @param periodInSecond - time period for mapping
//     * @return
//     */
//    public static Bar remap(Ticker ticker, long periodInSecond) {
//        if (ticker == null) return null;
//        ZonedDateTime startDateTime = ZonedDateTime.ofInstant(ticker.getTimestamp().toInstant(), ZoneId.systemDefault());
//
//        return new BaseBar(
//                Duration.ofSeconds(periodInSecond),
//                startDateTime.plusSeconds(periodInSecond),
//                ticker.getOpen()!=null ? Decimal.valueOf(ticker.getOpen()): Decimal.valueOf(0),
//                ticker.getHigh() !=null ? Decimal.valueOf(ticker.getHigh()): Decimal.valueOf(0),
//                ticker.getLow() != null ? Decimal.valueOf(ticker.getLow()): Decimal.valueOf(0),
//                Decimal.valueOf(Objects.requireNonNull(ticker.getLast())), // mustn't be null - is used for strategy analysis. source GDAX ticker price
//                ticker.getVolume() !=null ? Decimal.valueOf(ticker.getVolume()): Decimal.valueOf(0)
//                );
//    }
}
