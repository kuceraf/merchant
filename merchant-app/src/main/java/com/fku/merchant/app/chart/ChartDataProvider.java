package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import com.fku.strategy.domain.ChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

public abstract class ChartDataProvider<T> {
    final private TradingStrategy tradingStrategy;

    @Autowired
    public ChartDataProvider(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }

    public abstract ChartType getType();
    abstract T initData();
    abstract void addChartDataSpec(TimeSeries timeSeries, T data, int i);

    public ChartDTO<T> getChartData(int size) {
        T data = initData();
        TimeSeries timeSeries = tradingStrategy.getTechnicalAnalysis().getTimeSeries();
        ChartDTO<T> chartDTO = new ChartDTO<>();
        chartDTO.setName(getType().name());

        int beginIndex = timeSeries.getBarCount() - size;
        if(beginIndex < 0) return null;
        for (int i = beginIndex; i < timeSeries.getBarCount(); i++) {
            addChartDataSpec(timeSeries, data, i);
        }
        chartDTO.setData(data);
        return chartDTO;
    }
}
