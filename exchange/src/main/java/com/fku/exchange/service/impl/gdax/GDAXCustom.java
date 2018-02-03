package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import org.knowm.xchange.gdax.dto.GDAXException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public interface GDAXCustom {
// https://github.com/timmolter/XChange/issues/1580
    @GET
    @Path("products/{baseCurrency}-{targetCurrency}/candles?start={startTime}&end={endTime}&granularity={granularityInSecs}")
    GDAXHistoricRates[] getHistoricRates(@PathParam("baseCurrency") String baseCurrency, @PathParam("targetCurrency") String targetCurrency,
                                         @PathParam("startTime") String startTime, @PathParam("endTime") String endTime,
                                         @PathParam("granularityInSecs") String granularityInSecs) throws GDAXException, IOException;
}
