package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import org.springframework.stereotype.Component;
import org.ta4j.core.Bar;
import org.ta4j.core.Decimal;
import org.ta4j.core.TimeSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;

import java.util.ArrayList;
import java.util.List;

@Component
public class LineChartDataProvider extends ChartDataProvider<List<LineChartDataProvider.DateAndDouble>> {

    public LineChartDataProvider(TradingStrategy tradingStrategy) {
        super(tradingStrategy);
    }

    @Override
    List<DateAndDouble> initData() {
        return new ArrayList<>();
    }

    @Override
    public ChartType getType() {
        return ChartType.LINE;
    }

    @Override
    void addChartDataSpec(TimeSeries timeSeries, List<DateAndDouble> data, int i) {
        Bar bar = timeSeries.getBar(i);
        long dateTime = bar.getEndTime().toInstant().toEpochMilli();
        double closePrice = bar.getClosePrice().doubleValue();

        // FIXME neni potreba vzdy znovu pocitat sma
        ClosePriceIndicator closePriceIndicator = new ClosePriceIndicator(timeSeries);
        SMAIndicator sma = new SMAIndicator(closePriceIndicator, 20);
        Decimal smaClosePrice = sma.getValue(i);

        DateAndDouble dateAndDouble = new DateAndDouble(dateTime, closePrice, smaClosePrice.doubleValue());
        data.add(dateAndDouble);
    }

    class DateAndDouble {
        private long dateTime;
        private Double closePrice;
        private Double smaClosePrice;

        DateAndDouble(long dateTime, Double closePrice, Double smaClosePrice) {
            this.dateTime = dateTime;
            this.closePrice = closePrice;
            this.smaClosePrice = smaClosePrice;
        }


        public long getDateTime() {
            return dateTime;
        }

        public Double getClosePrice() {
            return closePrice;
        }

        public Double getSmaClosePrice() {
            return smaClosePrice;
        }
    }
}
