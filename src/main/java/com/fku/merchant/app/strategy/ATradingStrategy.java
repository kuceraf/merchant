package com.fku.merchant.app.strategy;

import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.springframework.beans.factory.annotation.Qualifier;

@Log4j2
public abstract class ATradingStrategy implements TradingStrategy {
    protected final Exchange exchange;

    public ATradingStrategy(@Qualifier("exchange") Exchange exchange) {
        this.exchange = exchange;
    }
}
