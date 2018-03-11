package com.fku.strategy;

import com.fku.ta.TechnicalAnalysis;
import org.ta4j.core.TimeSeries;

public interface TradingStrategy {
    void execute() throws Exception;
    Long getExecutionNo();
    TechnicalAnalysis getTechnicalAnalysis();
}
