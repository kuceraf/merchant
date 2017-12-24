package com.fku.merchant.app.strategy;

public interface TradingStrategy {
    void execute() throws StrategyException;
}
