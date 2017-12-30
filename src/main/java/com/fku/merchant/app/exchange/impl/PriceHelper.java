package com.fku.merchant.app.exchange.impl;

import com.fku.merchant.app.core.error.MerchantFatalException;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Log4j2
public class PriceHelper {

    private static final String ERR_BAD_ORDER_BOOK = "Can't get current price from empty/null order book";
    private static final String ERR_MISSING_PARAM = "Can't calculate amount with missing parameter";

    /**
     * Example:
     * I have 10 EUR (=counterCurrencyAmount)
     * Want to buy X BTC
     * Current pair price is 7,599.99 EUR for 1 BTC (instrument price BTC/EUR = 7,599.99 EUR) (=currencyPairPrice)
     * X = 10 / 7,599.99
     * X = 0,00131579 BTC
     * I will buy 0,00131579 BTC for 10 EUR at price BTC/EUR = 7,599.99.
     * @param counterCurrencyAmount in BTC/EUR it is amount of EUR
     * @param currencyPairPrice actual price of BTC/EUR instrument
     * @return amount of base currency
     */
    static BigDecimal calculateAmountOfBaseCurrency(BigDecimal counterCurrencyAmount, BigDecimal currencyPairPrice) {
        Assert.notNull(counterCurrencyAmount, ERR_MISSING_PARAM);
        Assert.notNull(currencyPairPrice, ERR_MISSING_PARAM);
        /*
         * Most exchanges (if not all) use 8 decimal places and typically round in favour of the exchange.
         * It's usually safest to round down the order quantity in your calculations.
         */
        return counterCurrencyAmount.divide(currencyPairPrice, 8, RoundingMode.HALF_DOWN);
    }

    /**
     * The bid price represents the maximum price that a buyer (=the one who places buy order)
     * is willing to pay for a security.
     * @param orderBook (in bids price list should contain the highest price at first position)
     * @return Current BID price (the highest bid price)
     * @throws MerchantFatalException if order book is null or empty
     */
    static BigDecimal getCurrentBidPrice(OrderBook orderBook) throws MerchantFatalException {
        if (orderBook == null || orderBook.getBids() == null || orderBook.getBids().size() == 0) {
            throw new MerchantFatalException(ERR_BAD_ORDER_BOOK);
        }
        BigDecimal price = orderBook.getBids().get(0).getLimitPrice();
        log.info("Current highest BID price [{}]", price);
        return price;
    }

    /**
     * The ask price represents the minimum price that a seller (=the one who places sell order)
     * is willing to receive.
     * @param orderBook (in asks price list should contain the lowest price at first position)
     * @return Current ASK price (the lowest ask price)
     * @throws MerchantFatalException if order book is null or empty
     */
    static BigDecimal getCurrentAskPrice(OrderBook orderBook) throws MerchantFatalException {
        if (orderBook == null || orderBook.getAsks() == null || orderBook.getAsks().size() == 0) {
            throw new MerchantFatalException(ERR_BAD_ORDER_BOOK);
        }
        BigDecimal price = orderBook.getAsks().get(0).getLimitPrice();
        log.info("Current highest ASK price [{}]", price);
        return price;
    }
}
