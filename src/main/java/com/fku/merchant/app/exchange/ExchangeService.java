package com.fku.merchant.app.exchange;

import com.fku.merchant.app.strategy.dto.OrderState;
import com.fku.merchant.app.strategy.dto.PricePair;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.OrderBook;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    PricePair getCurrentPrices() throws MerchantExchangeException;
    OrderState placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount);
}
