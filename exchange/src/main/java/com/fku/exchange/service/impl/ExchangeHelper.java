package com.fku.exchange.service.impl;

import com.fku.exchange.error.MerchantExchangeException;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Log4j2
public class ExchangeHelper {

    /*
     * Most exchanges (if not all) use 8 decimal places and typically round in favour of the exchange.
     * It's usually safest to round down the order quantity in your calculations.
     */
//    private static final MathContext EXCHANGE_MATH_CONTEXT = new MathContext(8, RoundingMode.HALF_DOWN);

    private static final String ERR_BAD_ORDER_BOOK = "Can't get current price from empty/null order book";

    private ExchangeHelper() {
        throw new AssertionError("This class isn't designed for instantiation");
    }

    /**
     * Example:
     * I have 10 EUR (=counterCurrencyAmount)
     * Want to buy X BTC
     * Current pair price is 7,599.99 EUR for 1 BTC (instrument price BTC/EUR = 7,599.99 EUR) (=instrumentPrice)
     * X = 10 / 7,599.99
     * X = 0,00131579 BTC
     * I will buy 0,00131579 BTC for 10 EUR at price BTC/EUR = 7,599.99.
     * @param counterCurrencyAmount in BTC/EUR it is amount of EUR
     * @param instrumentPrice actual price of BTC/EUR instrument
     * @return amount of base currency
     */
    public static BigDecimal calculateBaseCurrencyAmount(@Nonnull BigDecimal counterCurrencyAmount, @Nonnull BigDecimal instrumentPrice) {
        return counterCurrencyAmount.divide(instrumentPrice, 8, RoundingMode.HALF_DOWN);
    }

    /**
     * The bid price represents the maximum price that a buyer (=the one who places buy order)
     * is willing to pay for a security.
     * @param orderBook (in bids price list should contain the highest price at first position)
     * @return Current BID price (the highest bid price)
     * @throws MerchantExchangeException if order book is null or empty
     */
    public static BigDecimal getCurrentBidPrice(OrderBook orderBook) throws MerchantExchangeException {
        if (orderBook == null || orderBook.getBids() == null || orderBook.getBids().size() == 0) {
            throw new MerchantExchangeException(ERR_BAD_ORDER_BOOK);
        }
        BigDecimal price = orderBook.getBids().get(0).getLimitPrice();
        log.info("Current highest BID price [{}]", price);
        return price;
    }

    /**
     * The ask price represents the minimum price that a seller (=the one who places sell order)
     * is willing to receive for a security.
     * @param orderBook (in asks price list should contain the lowest price at first position)
     * @return Current ASK price (the lowest ask price)
     * @throws MerchantExchangeException if order book is null or empty
     */
    public static BigDecimal getCurrentAskPrice(OrderBook orderBook) throws MerchantExchangeException {
        if (orderBook == null || orderBook.getAsks() == null || orderBook.getAsks().size() == 0) {
            throw new MerchantExchangeException(ERR_BAD_ORDER_BOOK);
        }
        BigDecimal price = orderBook.getAsks().get(0).getLimitPrice();
        log.info("Current highest ASK price [{}]", price);
        return price;
    }
}
