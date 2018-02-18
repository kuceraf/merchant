package com.fku.merchant.app.api;

import com.fku.strategy.domain.ChartDataDTO;
import com.fku.strategy.TradingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ta4j.core.Strategy;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CharDataController {

    final
    TradingStrategy tradingStrategy;

    @Autowired
    public CharDataController(TradingStrategy tradingStrategy) {
        this.tradingStrategy = tradingStrategy;
    }

    @CrossOrigin()
    @RequestMapping("/chart_data")
    public ChartDataDTO timeSeries() {

        return tradingStrategy.getChartData();

//        List<int[]> data = new ArrayList<>();
//        data.add(new int[]{0,0});
//        data.add(new int[]{1,10});
//        data.add(new int[]{2,23});
//        return new ChartDataDTO("Testovaci TS", chartData);
    }
}
