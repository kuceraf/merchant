package com.fku.analyst.sma;

import com.fku.analyst.CsvTradesLoader;
import lombok.extern.log4j.Log4j2;
import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

@Log4j2
public class SmaIndicatorProvider {

    public static void main(String[] args) {
        // Getting a time series (from any provider: CSV, web service, etc.)
        TimeSeries series = CsvTradesLoader.loadBitstampSeries();
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series); // z kazdeho ticku v time series ziska atribut close price
        Decimal firstClosePrice = closePrice.getValue(0); /// atribut close price pro prvni tick v time series
        log.info("Close price of first tick in time series: {}", firstClosePrice);

        // Getting the simple moving average (SMA) of the close price over the last 5 ticks
        SMAIndicator shortSma = new SMAIndicator(closePrice, 4); // spocita SMA z 4 ticku

        // Interne probehne vypocet takto:
        // zaciname pocitat s hodnotou: series.getTickData().get(2).getClosePrice(); a koncime: series.getTickData().get(4).getClosePrice();
//        (800.80999999999994543031789362431 + 805.86000000000001364242052659392 + 805.84000000000003183231456205249 + 805.82000000000005002220859751105) / 4 = 804.58250000000001023181539494545

        Decimal mySum = Decimal.valueOf(800.80999999999994543031789362431)
                .plus(Decimal.valueOf(805.86000000000001364242052659392))
                .plus(Decimal.valueOf(805.84000000000003183231456205249))
                .plus(Decimal.valueOf(805.82000000000005002220859751105));
        Decimal myResult = mySum.dividedBy(Decimal.valueOf(4));

        Decimal smaValue = shortSma.getValue(5); // vypocet zacne od indexu 5 -> do vypoctu budou zahrnuty hodnoty close price z time series na indexech: 2,3,4,5
        log.info("4-ticks-SMA value at the 5th index: {}", smaValue);
        log.info("Are calculation equals?: {}",myResult.isEqual(smaValue));
    }
}
