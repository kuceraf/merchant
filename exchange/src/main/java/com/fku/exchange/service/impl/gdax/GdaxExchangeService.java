package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.service.impl.AExchangeService;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.service.BaseExchangeService;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.RestProxyFactory;

import java.io.IOException;

@Log4j2
public class GdaxExchangeService extends AExchangeService {
    private final GDAXCustom gdaxCustom;
    public GdaxExchangeService(org.knowm.xchange.Exchange xchangeAdapter, CurrencyPair currencyPair) {
        super(xchangeAdapter, currencyPair);
        this.gdaxCustom = RestProxyFactory.createProxy(GDAXCustom.class, xchangeAdapter.getExchangeSpecification().getSslUri(), this.getClientConfig());
    }

    @Override
    public void getHistoricalDataSeries(String startTime, String endTime, String granularityInSec)
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        try {
            GDAXHistoricRates[] historicRates = gdaxCustom.getHistoricRates(
                    currencyPair.base.getCurrencyCode(),
                    currencyPair.counter.getCurrencyCode(),
                    startTime,
                    endTime,
                    granularityInSec);
            // https://github.com/timmolter/XChange/wiki/New-Implementation-Best-Practices
            // TODO convert to timeSeries - Then you create an adapter class to take the provider-specific DTO (a raw DTO) and convert it into an XChange DTO
            // see https://github.com/timmolter/XChange/blob/develop/xchange-bitstamp/src/main/java/org/knowm/xchange/bitstamp/BitstampAdapters.java
        } catch (IOException e) {
            ExchangeExceptionHandler.handleException(e);
        }
    }

    @Override
    protected OrderBook getOrderBook()
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        OrderBook orderBook = null;
        try {
            // !!! GDAX need extra arg for order book size
            orderBook = xchangeAdapter.getMarketDataService().getOrderBook(currencyPair, 2);
        } catch (Exception e) {
            log.error("Failed to get order book from xchangeAdapter");
            ExchangeExceptionHandler.handleException(e);
        }
        return orderBook;
    }

    /** Taken from {@link BaseExchangeService#getClientConfig()} */
    private ClientConfig getClientConfig() {
        ClientConfig rescuConfig = new ClientConfig(); // create default rescu config
        // set per exchange connection- and read-timeout (if they have been set in the ExchangeSpecification)
        int customHttpConnTimeout = xchangeAdapter.getExchangeSpecification().getHttpConnTimeout();
        if (customHttpConnTimeout > 0) {
            rescuConfig.setHttpConnTimeout(customHttpConnTimeout);
        }
        int customHttpReadTimeout = xchangeAdapter.getExchangeSpecification().getHttpReadTimeout();
        if (customHttpReadTimeout > 0) {
            rescuConfig.setHttpReadTimeout(customHttpReadTimeout);
        }
        if (xchangeAdapter.getExchangeSpecification().getProxyHost() != null) {
            rescuConfig.setProxyHost(xchangeAdapter.getExchangeSpecification().getProxyHost());
        }
        if (xchangeAdapter.getExchangeSpecification().getProxyPort() != null) {
            rescuConfig.setProxyPort(xchangeAdapter.getExchangeSpecification().getProxyPort());
        }
        return rescuConfig;
    }
}
