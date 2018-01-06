package com.fku.merchant.app.strategy;

import com.fku.merchant.app.core.exception.MerchantException;

public interface TradingStrategy {
    void execute() throws Exception;
}
