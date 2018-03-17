package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.PassiveExchangeService;
import com.fku.exchange.service.impl.BaseExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import com.fku.exchange.service.impl.Granularity;
import com.fku.exchange.service.impl.gdax.dto.GDAXHistoricRates;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;
import org.ta4j.core.Bar;
import org.ta4j.core.TimeSeries;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.RestProxyFactory;

import javax.annotation.Nonnull;
import java.io.IOException;

@Log4j2
public class GDAXPassiveExchangeService extends BaseExchangeService implements PassiveExchangeService {
    private final GDAXRemoteInterface gdaxInterface;
    public GDAXPassiveExchangeService(@Nonnull Exchange xchangeAdapter, @Nonnull CurrencyPair currencyPair) {
        super(xchangeAdapter, currencyPair);
        this.gdaxInterface = RestProxyFactory.createProxy(GDAXRemoteInterface.class,
                xchangeAdapter.getExchangeSpecification().getSslUri(),
                this.getClientConfig());
    }

    @Override
    public CurrencyPair getCurrencyPair() {
        return currencyPair;
    }

    @Override
    public String getExchangeName() {
        return xchangeAdapter.getExchangeSpecification().getExchangeName();
    }

    @Override
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException {
        OrderBook orderBook = getOrderBook();
        return new InstrumentPrice(ExchangeHelper.getCurrentBidPrice(orderBook), ExchangeHelper.getCurrentAskPrice(orderBook));
    }

    public OpenOrders getOpenOrders() throws MerchantExchangeException {
        OpenOrders openOrders = null;
        try {
            OpenOrdersParams openOrdersParams = xchangeAdapter.getTradeService().createOpenOrdersParams();
            openOrders = xchangeAdapter.getTradeService().getOpenOrders(openOrdersParams);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return openOrders;
    }

    @Override
    public TimeSeries getHistoricalTimeSeries(@Nonnull Granularity granularity) throws MerchantExchangeException {
        GDAXHistoricRates[] gdaxHistoricRates = null;
        try {
            gdaxHistoricRates = gdaxInterface.getHistoricRates(
                    currencyPair.base.getCurrencyCode(),
                    currencyPair.counter.getCurrencyCode(),
                    String.valueOf(granularity.getSeconds())); // must be one of the following values: {60 (1min), 300 (5min), 900 (15min), 3600 (1h), 21600(6h), 86400 (1d)}, otherwise, your request will be rejected.
        } catch (IOException e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return GDAXMapper.remap(gdaxHistoricRates, granularity.getSeconds());
    }

    @Override
    public Bar getLastBar(@Nonnull Granularity granularity) throws MerchantExchangeException {
        return getHistoricalTimeSeries(granularity).getLastBar();
    }

    private OrderBook getOrderBook() throws MerchantExchangeException {
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
