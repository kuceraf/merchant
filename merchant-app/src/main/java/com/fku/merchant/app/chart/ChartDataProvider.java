package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import com.fku.strategy.domain.ChartDataDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChartDataProvider {
    final
    private TradingStrategy tradingStrategy;

    @Autowired
    public ChartDataProvider(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }


    public ChartDataDTO getCandlestickData() {
        TimeSeries timeSeries = tradingStrategy.getTimeSeries();

        ChartDataDTO chartDataDTO = new ChartDataDTO();
        chartDataDTO.setName("candlestick");
        // time, low, high, open, close
        List<double[]> data = new ArrayList<>();
        chartDataDTO.setData(data);

        for (int i = 0; i < timeSeries.getBarCount(); i++) {
            Bar bar = timeSeries.getBar(i);
            double endTime = (double) bar.getEndTime().toInstant().toEpochMilli();
            double[] dataPair = {
                    endTime,
                    bar.getMinPrice().doubleValue(),
                    bar.getOpenPrice().doubleValue(),
                    bar.getClosePrice().doubleValue(),
                    bar.getMaxPrice().doubleValue()};
            data.add(dataPair);
        }
        return chartDataDTO;
    }

    public ChartDataDTO getLineChartData() {
        TimeSeries timeSeries = tradingStrategy.getTimeSeries();

        ChartDataDTO chartDataDTO = new ChartDataDTO();
        chartDataDTO.setName("Close price line");
        List<double[]> data = new ArrayList<>();
        chartDataDTO.setData(data);

        for (int i = 0; i < timeSeries.getBarCount(); i++) {
            double[] dataPair = {i ,timeSeries.getBar(i).getClosePrice().intValue()};
            data.add(dataPair);
        }
        return chartDataDTO;
    }
}
