package com.fku.merchant.app.strategy;

import com.fku.merchant.app.core.exception.MerchantException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.core.exception.MerchantStrategyException;

public interface TradingStrategy {
    void execute() throws MerchantException;
}
