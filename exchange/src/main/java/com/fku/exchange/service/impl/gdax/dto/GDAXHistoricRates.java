package com.fku.exchange.service.impl.gdax.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;
import java.math.BigDecimal;

@JsonDeserialize(using = GDAXHistoricRates.GDAXOrderDeserializer.class)
public class GDAXHistoricRates {
    private final Long time;        // bucket start time
    private final BigDecimal low;   // lowest price during the bucket interval
    private final BigDecimal high;  // highest price during the bucket interval
    private final BigDecimal open;  // opening price (first trade) in the bucket interval
    private final BigDecimal close; // closing price (last trade) in the bucket interval
    private final BigDecimal volume; // volume of trading activity during the bucket interval

    public GDAXHistoricRates(Long time, BigDecimal low, BigDecimal high, BigDecimal open, BigDecimal close, BigDecimal volume) {
        this.time = time;
        this.low = low;
        this.high = high;
        this.open = open;
        this.close = close;
        this.volume = volume;
    }

    public Long getTime() {
        return time;
    }

    public BigDecimal getLow() {
        return low;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public BigDecimal getVolume() {
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
                BigDecimal low = new BigDecimal(node.path(1).asText());
                BigDecimal high = new BigDecimal(node.path(2).asText());
                BigDecimal open = new BigDecimal(node.path(3).asText());
                BigDecimal close = new BigDecimal(node.path(4).asText());
                BigDecimal volume = new BigDecimal(node.path(5).asText());
                return new GDAXHistoricRates(time,low,high,open,close,volume);
            }
            return null;
        }
    }
}