package com.fku.exchange.repository;

import com.fku.exchange.domain.ExchangeOrder;

public interface OrderRepository extends MerchantBaseRepository {
    ExchangeOrder findLastOrder();
    ExchangeOrder saveOrder(ExchangeOrder exchangeOrder);
}
