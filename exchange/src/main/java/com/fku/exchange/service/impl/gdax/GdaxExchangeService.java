package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.service.impl.ExchangeHelper;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.service.impl.AExchangeService;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.knowm.xchange.service.trade.params.orders.OpenOrdersParams;

@Log4j2
public class GdaxExchangeService extends AExchangeService {
    public GdaxExchangeService(org.knowm.xchange.Exchange xchangeAdapter, CurrencyPair currencyPair) {
        super(xchangeAdapter, currencyPair);
    }

    @Override
    public InstrumentPrice getCurrentPrices()
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        OrderBook orderBook = getOrderBook();
        return new InstrumentPrice(ExchangeHelper.getCurrentBidPrice(orderBook), ExchangeHelper.getCurrentAskPrice(orderBook));
    }

    @Override
    public OpenOrders getOpenOrders()
            throws MerchantExchangeException, MerchantExchangeNonFatalException {
        OpenOrders openOrders = null;
        try {
            OpenOrdersParams openOrdersParams = xchangeAdapter.getTradeService().createOpenOrdersParams();
            openOrders = xchangeAdapter.getTradeService().getOpenOrders(openOrdersParams);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return openOrders;
    }

    private OrderBook getOrderBook()
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
}
