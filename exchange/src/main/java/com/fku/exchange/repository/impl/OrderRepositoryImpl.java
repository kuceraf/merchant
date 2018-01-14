package com.fku.exchange.repository.impl;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.repository.ExchangeOrderRepository;
import org.knowm.xchange.dto.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // TODO predelat na JPA
public class OrderRepositoryImpl implements ExchangeOrderRepository {
    private List<ExchangeOrder> exchangeOrders = new ArrayList<>();

    @Override
    public List<ExchangeOrder> findAll() {
        return exchangeOrders;
    }

    @Override
    public List<ExchangeOrder> findBids() {
        return exchangeOrders.stream()
                .collect(Collectors.partitioningBy(ExchangeOrder::isBid))
                .get(true);
    }

    @Override
    public List<ExchangeOrder> findAsks() {
        return exchangeOrders.stream()
                .collect(Collectors.partitioningBy(ExchangeOrder::isAsk))
                .get(true);
    }

    @Override
    public ExchangeOrder findLast() {
        if(exchangeOrders.isEmpty()) {
            return null;
        }
        return exchangeOrders.get(exchangeOrders.size() - 1);
    }

    @Override
    public ExchangeOrder save(ExchangeOrder exchangeOrder) {
        exchangeOrders.add(exchangeOrder);
        return exchangeOrder;
    }
}
