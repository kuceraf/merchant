package com.fku.ta;

import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;

public interface TechnicalAnalysis {
    TimeSeries getTimeSeries();
    boolean shouldEnter();
    boolean shouldExit();
    void addBar(Bar bar);
}
