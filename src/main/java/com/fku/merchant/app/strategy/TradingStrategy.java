package com.fku.merchant.app.strategy;

import com.fku.merchant.app.core.error.MerchantException;

public interface TradingStrategy {
    void execute() throws MerchantException;
}
