package com.fku.strategy;

import org.ta4j.core.TimeSeries;

public interface TradingStrategy {
    void execute() throws Exception;
    Long getExecutionNo();

    // TODO move it to different class (it could be provided by repository)
//    ChartDataDTO getChartData();
    TimeSeries getTimeSeries();
}
