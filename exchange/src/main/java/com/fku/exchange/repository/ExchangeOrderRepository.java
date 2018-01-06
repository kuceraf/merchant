package com.fku.exchange.repository;

import com.fku.exchange.domain.ExchangeOrder;

public interface ExchangeOrderRepository extends MerchantBaseRepository {
    ExchangeOrder findLastOrder();
    ExchangeOrder saveOrder(ExchangeOrder exchangeOrder);
}
