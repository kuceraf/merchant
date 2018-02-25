package com.fku.strategy.error;

public class StrategyNonFatalException extends MerchantStrategyException {
    public StrategyNonFatalException(String msg) {
        super(msg);
    }

    public StrategyNonFatalException(Throwable e) {
        super(e);
    }

    public StrategyNonFatalException(String msg, Throwable e) {
        super(msg, e);
    }
}
