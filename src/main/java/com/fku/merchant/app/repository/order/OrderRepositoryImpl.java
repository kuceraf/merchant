package com.fku.merchant.app.repository.order;

import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import org.springframework.stereotype.Component;

@Component // TODO predelat na JPA
public class OrderRepositoryImpl implements OrderRepository {
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
