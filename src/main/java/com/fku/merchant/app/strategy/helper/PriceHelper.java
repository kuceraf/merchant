package com.fku.merchant.app.strategy.helper;

import com.fku.merchant.app.strategy.StrategyException;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.math.BigDecimal;

public class PriceHelper {

    private static final String ERR_BAD_ORDER_BOOK = "Can'n get current price from empty/null order book";

    /**
     * The bid price represents the maximum price that a buyer (=the one who places buy order)
     * is willing to pay for a security.
     * @param orderBook (in bids price list should contain the highest price at first position)
     * @return Current BID price (the highest bid price)
     * @throws StrategyException if order book is null or empty
     */
    static BigDecimal getCurrentBidPrice(OrderBook orderBook) throws StrategyException {
        if (orderBook == null || orderBook.getBids() == null || orderBook.getBids().size() == 0) {
            throw new StrategyException(ERR_BAD_ORDER_BOOK);
        }
        return orderBook.getBids().get(0).getLimitPrice();
    }

    /**
     * The ask price represents the minimum price that a seller (=the one who places sell order)
     * is willing to receive.
     * @param orderBook (in asks price list should contain the lowest price at first position)
     * @return Current ASK price (the lowest ask price)
     * @throws StrategyException if order book is null or empty
     */
    static BigDecimal getCurrentAskPrice(OrderBook orderBook) throws StrategyException {
        if (orderBook == null || orderBook.getAsks() == null || orderBook.getAsks().size() == 0) {
            throw new StrategyException(ERR_BAD_ORDER_BOOK);
        }
        return orderBook.getAsks().get(0).getLimitPrice();
    }
}
