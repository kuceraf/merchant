package com.fku.merchant.app.exchange.impl.gdax;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.exchange.ExchangeExceptionHandler;
import com.fku.merchant.app.exchange.PriceHelper;
import com.fku.merchant.app.exchange.impl.AExchangeService;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;


@Log4j2
public class GdaxExchangeService extends AExchangeService {
    public GdaxExchangeService(org.knowm.xchange.Exchange xchange, CurrencyPair currencyPair) {
        super(xchange, currencyPair);
    }

    @Override
    public InstrumentPrice getCurrentPrices()
            throws MerchantExchangeException, MerchantNonFatalException {
        OrderBook orderBook = getOrderBook();
        return new InstrumentPrice(PriceHelper.getCurrentBidPrice(orderBook), PriceHelper.getCurrentAskPrice(orderBook));
    }

    private OrderBook getOrderBook()
            throws MerchantExchangeException, MerchantNonFatalException {
        OrderBook orderBook = null;
        try {
            // !!! GDAX need extra arg for order book size
            orderBook = xchange.getMarketDataService().getOrderBook(currencyPair, 2);
        } catch (Exception e) {
            log.error("Failed to get order book from xchange");
            ExchangeExceptionHandler.handleException(e);
        }
        return orderBook;
    }
}
