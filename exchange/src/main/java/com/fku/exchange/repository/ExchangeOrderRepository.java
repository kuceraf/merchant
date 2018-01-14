package com.fku.exchange.repository;

import com.fku.exchange.domain.ExchangeOrder;

import java.util.List;

public interface ExchangeOrderRepository extends MerchantBaseRepository {
    List<ExchangeOrder> findAll();
    List<ExchangeOrder> findBids();
    List<ExchangeOrder> findAsks();
    ExchangeOrder findLast();
    ExchangeOrder save(ExchangeOrder exchangeOrder);
}
