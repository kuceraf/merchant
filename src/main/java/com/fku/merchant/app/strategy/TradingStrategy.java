package com.fku.merchant.app.strategy;

import com.fku.merchant.app.core.MerchantException;

public interface TradingStrategy {
    void execute() throws MerchantException;
}
