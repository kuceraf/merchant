package com.fku.merchant.app.exchange;

import com.fku.merchant.app.core.exception.MerchantException;
import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.strategy.dto.OrderState;
import com.fku.merchant.app.strategy.dto.PricePair;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    PricePair getCurrentPrices() throws MerchantExchangeException, MerchantNonFatalException;
    OrderState placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantNonFatalException;
}
