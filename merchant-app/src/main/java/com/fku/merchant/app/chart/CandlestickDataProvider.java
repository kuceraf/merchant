//package com.fku.merchant.app.chart;
//
//import com.fku.strategy.TradingStrategy;
//import org.ta4j.core.Bar;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CandlestickDataProvider extends ChartDataProvider<List<double[]>> {
//
//    public CandlestickDataProvider(TradingStrategy tradingStrategy) {
//        super(tradingStrategy);
//    }
//
//    @Override
//    public ChartType getType() {
//        return ChartType.CANDLESTICK;
//    }
//
//    @Override
//    List<double[]> initData() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    void addChartDataSpec(Bar bar, List<double[]> data, int i) {
//        double endTime = (double) bar.getEndTime().toInstant().toEpochMilli();
//        double[] dataPair = {
//                endTime,
//                bar.getMinPrice().doubleValue(),
//                bar.getOpenPrice().doubleValue(),
//                bar.getClosePrice().doubleValue(),
//                bar.getMaxPrice().doubleValue()};
//        data.add(dataPair);
//    }
//}
