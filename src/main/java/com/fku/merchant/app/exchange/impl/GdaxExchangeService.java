package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.core.error.MerchantException;
import com.fku.merchant.app.exchange.ExchangeExceptionHandler;
import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.core.error.MerchantFatalException;
import com.fku.merchant.app.strategy.dto.OrderState;
import com.fku.merchant.app.strategy.dto.OrderType;
import com.fku.merchant.app.strategy.dto.PricePair;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import java.io.IOException;
import java.math.BigDecimal;


@Log4j2
public class GdaxExchangeService implements ExchangeService {
    private final CurrencyPair currencyPair;
    private final org.knowm.xchange.Exchange xchange;

    public GdaxExchangeService(org.knowm.xchange.Exchange xchange, CurrencyPair currencyPair) {
        this.xchange = xchange;
        this.currencyPair = currencyPair;
    }

    @Override
    public String getExchangeName() {
        return xchange.getExchangeSpecification().getExchangeName();
    }

    @Override
    public PricePair getCurrentPrices() throws MerchantException {
        OrderBook orderBook = getOrderBook();
        return new PricePair(PriceHelper.getCurrentBidPrice(orderBook), PriceHelper.getCurrentAskPrice(orderBook));
    }

    @Override
    public OrderState placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantException {
        OrderState orderState = null;
        try {

            BigDecimal lastCurrencyPairMarketPrice = xchange.getMarketDataService().getTicker(this.currencyPair).getLast();
            BigDecimal amountOfBaseCurrency = PriceHelper.calculateAmountOfBaseCurrency(counterCurrencyAmount, lastCurrencyPairMarketPrice);
            LimitOrder newBuyLimitOrder = new LimitOrder(
                    Order.OrderType.BID,
                    amountOfBaseCurrency, // The amount of base currency to trade
                    this.currencyPair,
                    null,
                    null,
                    currentBidPrice // In a BID this is the highest acceptable price
            );

            log.info("Sending BUY order to exchange --->");
            log.info(newBuyLimitOrder);
            String orderId = xchange.getTradeService().placeLimitOrder(newBuyLimitOrder);

            orderState = new OrderState(orderId, OrderType.BUY, currentBidPrice, amountOfBaseCurrency);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return orderState;
    }


    private OrderBook getOrderBook() throws MerchantFatalException {
        OrderBook orderBook = null;
        try {
            orderBook = xchange.getMarketDataService().getOrderBook(currencyPair, 2);
        } catch (IOException e) {
            log.error("Failed to get order book from xchange");
            throw new MerchantFatalException("Failed to get order book from xchange");
        }
        return orderBook;
    }
}
