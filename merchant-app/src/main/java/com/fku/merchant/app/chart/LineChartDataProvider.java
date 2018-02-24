package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import org.springframework.stereotype.Component;
import org.ta4j.core.Bar;

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
    void addChartDataSpec(Bar bar, List<DateAndDouble> data, int i) {
        DateAndDouble dateAndDouble = new DateAndDouble(bar.getEndTime().toInstant().toEpochMilli(), bar.getClosePrice().doubleValue());
        data.add(dateAndDouble);
    }

    class DateAndDouble {
        private long dateTime;
        private Double closePrice;

        DateAndDouble(long dateTime, Double closePrice) {
            this.dateTime = dateTime;
            this.closePrice = closePrice;
        }

        public long getDateTime() {
            return dateTime;
        }

        public Double getClosePrice() {
            return closePrice;
        }
    }
}
