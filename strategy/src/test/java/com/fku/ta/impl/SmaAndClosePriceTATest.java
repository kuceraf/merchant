package com.fku.ta.impl;

import com.fku.exchange.service.impl.dummy.DummyExchangeDataFactory;
import org.junit.Test;
import org.ta4j.core.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SmaAndClosePriceTATest {

    @Test
    public void shouldEnter_false() {
        // given
        TimeSeries timeSeriesDummy = DummyExchangeDataFactory.getTimeSeries(3);
        int i = 1;
        List<Bar> bars = new ArrayList<>();
        for (Bar bar : timeSeriesDummy.getBarData()) {
            BaseBar newBar = new BaseBar(
                    bar.getEndTime(),
                    bar.getOpenPrice(),
                    bar.getMaxPrice(),
                    bar.getMinPrice(),
                    Decimal.valueOf(i), // 1, 2, 3
                    bar.getVolume()
            );
            i++;
            bars.add(newBar);
        }
        TimeSeries timeSeries = new BaseTimeSeries("dummy-time-series", bars);

        // when
        SmaAndClosePriceTA smaAndClosePriceTA = new SmaAndClosePriceTA(timeSeries);

        // than
        // entry if sma(20) > closePrice
        assertThat(smaAndClosePriceTA.shouldEnter()).as("SMA of 1,2,3 is 2 and close price is 3 => do not enter").isFalse();
        // exit if sma(20) < closePrice
        assertThat(smaAndClosePriceTA.shouldExit()).as("SMA of 1,2,3 is 2 and close price is 3 => exit").isTrue();
    }

    @Test
    public void shouldEnter_true() {
        // given
        TimeSeries timeSeriesDummy = DummyExchangeDataFactory.getTimeSeries(3);
        int i = 3;
        List<Bar> bars = new ArrayList<>();
        for (Bar bar : timeSeriesDummy.getBarData()) {
            BaseBar newBar = new BaseBar(
                    bar.getEndTime(),
                    bar.getOpenPrice(),
                    bar.getMaxPrice(),
                    bar.getMinPrice(),
                    Decimal.valueOf(i), // 3, 2, 1
                    bar.getVolume()
            );
            i--;
            bars.add(newBar);
        }
        TimeSeries timeSeries = new BaseTimeSeries("dummy-time-series", bars);

        // when
        SmaAndClosePriceTA smaAndClosePriceTA = new SmaAndClosePriceTA(timeSeries);

        // than
        // entry if sma(20) > closePrice
        assertThat(smaAndClosePriceTA.shouldEnter()).as("SMA of 3,2,1 is 2 and close price is 1 => enter").isTrue();
        assertThat(smaAndClosePriceTA.shouldExit()).as("SMA of 3,2,1 is 2 and close price is 1 => do not exit").isFalse();
    }
}