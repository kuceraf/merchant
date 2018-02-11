package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.BaseExchangeService;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.ta4j.core.TimeSeries;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.RestProxyFactory;

import javax.annotation.Nonnull;
import java.io.IOException;

@Log4j2
public class GDAXExchangeService extends BaseExchangeService implements ExchangeService {
    private final GDAXRemoteInterface gdaxInterface;
    public GDAXExchangeService(@Nonnull Exchange xchangeAdapter, @Nonnull CurrencyPair currencyPair) {
        super(xchangeAdapter, currencyPair);
        this.gdaxInterface = RestProxyFactory.createProxy(GDAXRemoteInterface.class,
                xchangeAdapter.getExchangeSpecification().getSslUri(),
                this.getClientConfig());
    }


    /**
     * The granularity field must be one of the following values: {60, 300, 900, 3600, 21600, 86400}.
     * Otherwise, your request will be rejected.
     * These values correspond to timeslices representing one minute, five minutes, fifteen minutes, one hour, six hours, and one day, respectively.
     */
    @Override
    public TimeSeries getHistoricalTimeSeries(String startTime, String endTime, String granularityInSec)
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        GDAXHistoricRates[] gdaxHistoricRates = null;
        try {
            gdaxHistoricRates = gdaxInterface.getHistoricRates(
                    currencyPair.base.getCurrencyCode(),
                    currencyPair.counter.getCurrencyCode(),
                    startTime,
                    endTime,
                    granularityInSec);
        } catch (IOException e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return GDAXMapper.remap(gdaxHistoricRates, Long.parseLong(granularityInSec));
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

    /** Taken from {@link org.knowm.xchange.service.BaseExchangeService#getClientConfig()} */
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
