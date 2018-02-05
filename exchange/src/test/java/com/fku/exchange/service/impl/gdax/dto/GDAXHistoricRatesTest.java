package com.fku.exchange.service.impl.gdax.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.ta4j.core.Bar;
import org.ta4j.core.Decimal;

import java.io.InputStream;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test GDAXHistoricRates JSON parsing
 */
public class GDAXHistoricRatesTest {
    @Test
    public void testUnmarshal() throws Exception {
        // Read in the JSON from the example resources
        InputStream is = GDAXHistoricRatesTest.class.getResourceAsStream("/2017-07-01_product_candles_minute_interval.json");

        ObjectMapper mapper = new ObjectMapper();
        GDAXHistoricRates[] gdaxHistoricRates = mapper.readValue(is, GDAXHistoricRates[].class);

        assertThat(gdaxHistoricRates[0])
                .as("Example data must be unmarshalled correctly")
                .extracting(GDAXHistoricRates::getTime, GDAXHistoricRates::getLow, GDAXHistoricRates::getHigh,
                        GDAXHistoricRates::getOpen, GDAXHistoricRates::getClose, GDAXHistoricRates::getVolume)
                .contains(1498924740L, 2163.3, 2163.3, 2163.3, 2163.3, 0.04);
    }
}
