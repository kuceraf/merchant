package com.fku.exchange.service.impl.gdax.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

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
        InputStream is = GDAXHistoricRatesTest.class.getResourceAsStream("/product_candles.json");

        ObjectMapper mapper = new ObjectMapper();
        GDAXHistoricRates[] gdaxHistoricRates = mapper.readValue(is, GDAXHistoricRates[].class);

        // Verify that the example data was unmarshalled correctly
        assertThat(gdaxHistoricRates[0].getTime()).isEqualTo(1498924740);
        assertThat(gdaxHistoricRates[0].getLow()).isEqualTo(BigDecimal.valueOf(2163.3));
        assertThat(gdaxHistoricRates[0].getHigh()).isEqualTo(BigDecimal.valueOf(2163.3));
        assertThat(gdaxHistoricRates[0].getOpen()).isEqualTo(BigDecimal.valueOf(2163.3));
        assertThat(gdaxHistoricRates[0].getClose()).isEqualTo(BigDecimal.valueOf(2163.3));
        assertThat(gdaxHistoricRates[0].getVolume()).isEqualTo(BigDecimal.valueOf(0.04));
    }
}
