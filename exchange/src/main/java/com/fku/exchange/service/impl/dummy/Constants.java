package com.fku.exchange.service.impl.dummy;

import java.math.BigDecimal;

public class Constants {
    // dummy constants
    public static final BigDecimal LIMIT_BID_PRICE = BigDecimal.valueOf(13728.24); // maximum price that a buyer is willing to pay for a security
    public static final BigDecimal LIMIT_ASK_PRICE = BigDecimal.valueOf(13728.25); // minimum price that a seller is willing to receive for a security
    public static final BigDecimal INSTRUMENT_LAST_PRICE = LIMIT_ASK_PRICE; // actual exchange rate of currency pair (instrument)
    public static final BigDecimal COUNTER_CURRENCY_AMOUNT = BigDecimal.valueOf(100);
    public static final BigDecimal BASE_CURRENCY_AMOUNT = BigDecimal.valueOf(0.00728425);
    public static final String EXISTING_OPEN_ORDER_ID = "ee0c87b1-fd65-4961-b317-7c0852b7f37e";

}
