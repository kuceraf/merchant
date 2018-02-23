package com.fku.merchant.app.chart;

import com.fku.strategy.TradingStrategy;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.ta4j.core.Bar;

import java.util.ArrayList;
import java.util.List;

@Component
public class LineChartDataProvider extends ChartDataProvider<List<double[]>> {

    public LineChartDataProvider(TradingStrategy tradingStrategy) {
        super(tradingStrategy);
    }

    @Override
    List<double[]> initData() {
        return new ArrayList<>();
    }

    @Override
    public ChartType getType() {
        return ChartType.LINE;
    }

    @Override
    void addChartDataSpec(Bar bar, List<double[]> data, int i) {
        double[] dataPair = {i ,bar.getClosePrice().intValue()};
        data.add(dataPair);
    }
}
