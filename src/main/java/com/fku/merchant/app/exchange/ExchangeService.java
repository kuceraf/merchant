package com.fku.merchant.app.exchange;

import com.fku.merchant.app.core.error.MerchantException;
import com.fku.merchant.app.strategy.dto.OrderState;
import com.fku.merchant.app.strategy.dto.PricePair;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    PricePair getCurrentPrices() throws MerchantException;
    OrderState placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantException;
}
