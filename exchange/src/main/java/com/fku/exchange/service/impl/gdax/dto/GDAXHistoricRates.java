package com.fku.exchange.service.impl.gdax.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.math.BigDecimal;

@JsonDeserialize(using = GDAXHistoricRates.GDAXOrderDeserializer.class)
public class GDAXHistoricRates {
    // API doc https://docs.gdax.com/#get-historic-rates
    private final long time;     // bucket start time (seconds-based epoch value)
    private final double low;    // lowest price during the bucket interval
    private final double high;   // highest price during the bucket interval
    private final double open;   // opening price (first trade) in the bucket interval
    private final double close;  // closing price (last trade) in the bucket interval
    private final double volume; // volume of trading activity during the bucket interval

    public GDAXHistoricRates(Long time, double low, double high, double open, double close, double volume) {
        this.time = time;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

    public long getTime() {
        return time;
    }

    public double getLow() {
        return low;
    }

    public double getHigh() {
        return high;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    public String toString() {
        return "GDAXHistoricRates [time=" + time + ", low=" + low + ", high=" + high + ", open=" + open + ", close=" + close + ", volume=" + volume + "]";
    }

    static class GDAXOrderDeserializer extends JsonDeserializer {
        @Override
        public GDAXHistoricRates deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);
            if (node.isArray()) {
                long time = node.path(0).asLong();
                double low = node.path(1).asDouble();
                double high = node.path(2).asDouble();
                double open = node.path(3).asDouble();
                double close = node.path(4).asDouble();
                double volume = node.path(5).asDouble();
                return new GDAXHistoricRates(time,low,high,open,close,volume);
            }
            return null;
        }
    }
}