package com.fku.exchange.repository.impl;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.repository.ExchangeOrderRepository;
import org.springframework.stereotype.Component;

@Component // TODO predelat na JPA
public class OrderRepositoryImpl implements ExchangeOrderRepository {
    private ExchangeOrder lastOrder = null;

    @Override
    public ExchangeOrder findLastOrder() {
        return lastOrder;
    }

    @Override
    public ExchangeOrder saveOrder(ExchangeOrder exchangeOrder) {
        this.lastOrder = exchangeOrder;
        return exchangeOrder;
    }
}
