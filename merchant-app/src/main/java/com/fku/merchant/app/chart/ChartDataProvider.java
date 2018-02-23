package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import com.fku.strategy.domain.ChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

import java.util.ArrayList;
import java.util.List;

public abstract class ChartDataProvider<T> {
    final private TradingStrategy tradingStrategy;
//    private Map<ChartType, ChartDataProvider> providerMap = new HashMap<>();

    @Autowired
    public ChartDataProvider(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }

    public abstract ChartType getType();
    abstract T initData();
    abstract void addChartDataSpec(Bar bar, T data, int i);

    public ChartDTO<T> getChartData(int size) {
        T data = initData();
        TimeSeries timeSeries = tradingStrategy.getTimeSeries();
        ChartDTO<T> chartDTO = new ChartDTO<>();
        chartDTO.setName(getType().name());

        int beginIndex = timeSeries.getBarCount() - size;
        if(beginIndex < 0) return null;
        for (int i = beginIndex; i < timeSeries.getBarCount(); i++) {
            addChartDataSpec(timeSeries.getBar(i), data, i);
        }
        chartDTO.setData(data);
        return chartDTO;
    }

    public ChartDTO getCandlestickData(int size) {
        TimeSeries timeSeries = tradingStrategy.getTimeSeries();

        ChartDTO<List<double[]>> chartDataDTO = new ChartDTO<>();
        chartDataDTO.setName("candlestick");
        // time, low, high, open, close
        List<double[]> data = new ArrayList<>();
        chartDataDTO.setData(data);

        int beginIndex = timeSeries.getBarCount() - size;
        if(beginIndex < 0) return null;
        for (int i = beginIndex; i < timeSeries.getBarCount(); i++) {
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

//    public ChartDTO<List<double[]>> getLineChartData(int size) {
//        TimeSeries timeSeries = tradingStrategy.getTimeSeries();
//
//        ChartDTO<List<double[]>> chartDataDTO = new ChartDTO<>();
//        chartDataDTO.setName("Close price line");
//        List<double[]> data = new ArrayList<>();
//        chartDataDTO.setData(data);
//
//        int beginIndex = timeSeries.getBarCount() - size;
//        if(beginIndex < 0) return null;
//        for (int i = beginIndex; i < timeSeries.getBarCount(); i++) {
//            double[] dataPair = {i ,timeSeries.getBar(i).getClosePrice().intValue()};
//            data.add(dataPair);
//        }
//        return chartDataDTO;
//    }
}
