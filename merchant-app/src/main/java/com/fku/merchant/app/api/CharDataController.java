package com.fku.merchant.app.api;

import com.fku.merchant.app.chart.ChartDataProvider;
import com.fku.merchant.app.chart.ChartType;
import com.fku.strategy.domain.ChartDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin()
@RestController
public class CharDataController {
    final private Map<ChartType, ChartDataProvider> chartDataProviderMap;

    @Autowired
    public CharDataController(Set<ChartDataProvider> chartDataProviders) {
        chartDataProviderMap = chartDataProviders.stream()
                .collect(Collectors.toMap(ChartDataProvider::getType,provider -> provider));
    }

//    TODO implementovat jako podtyp ChartDataProvider
//    @RequestMapping("/candlestick")
//    public ChartDTO candlestick() {
//        return chartDataProvider.getCandlestickData(20);
//    }

    @RequestMapping("/line_chart")
    public ChartDTO lineChart() {
        return chartDataProviderMap.get(ChartType.LINE).getChartData(50);
    }
}
