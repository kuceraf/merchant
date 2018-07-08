package com.fku.exchange.service.impl.gdax.dto;

import org.ta4j.core.Bar;

import java.util.ArrayList;
import java.util.List;

public class MarketData<T> {
    private List<T> data;

    public void addToData(T item) {
        if (data == null) {
            data = new ArrayList<>();
        }
        data.add(item);
    }
    public List<T> getData() {
        return data;
    }
}
