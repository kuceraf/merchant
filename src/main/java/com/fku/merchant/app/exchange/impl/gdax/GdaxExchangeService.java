package com.fku.merchant.app.exchange.impl.gdax;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.exchange.ExchangeExceptionHandler;
import com.fku.merchant.app.exchange.ExchangeHelper;
import com.fku.merchant.app.exchange.impl.AExchangeService;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
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
            throws MerchantExchangeException, MerchantNonFatalException {
        OrderBook orderBook = getOrderBook();
        return new InstrumentPrice(ExchangeHelper.getCurrentBidPrice(orderBook), ExchangeHelper.getCurrentAskPrice(orderBook));
    }

    @Override
    public OpenOrders getOpenOrders()
            throws MerchantExchangeException, MerchantNonFatalException {
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
            throws MerchantExchangeException, MerchantNonFatalException {
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
