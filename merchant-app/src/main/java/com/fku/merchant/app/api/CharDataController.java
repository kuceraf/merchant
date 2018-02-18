package com.fku.merchant.app.api;

import com.fku.merchant.app.chart.ChartDataProvider;
import com.fku.strategy.domain.ChartDataDTO;
import com.fku.strategy.TradingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.Strategy;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin()
@RestController
public class CharDataController {
    final
    private ChartDataProvider chartDataProvider;

    @Autowired
    public CharDataController(ChartDataProvider chartDataProvider) {
        this.chartDataProvider = chartDataProvider;
    }

    @RequestMapping("/candlestick")
    public ChartDataDTO candlestick() {
        return chartDataProvider.getCandlestickData();
    }

    @RequestMapping("/line_chart")
    public ChartDataDTO lineChart() {
        return chartDataProvider.getLineChartData();
    }
}
