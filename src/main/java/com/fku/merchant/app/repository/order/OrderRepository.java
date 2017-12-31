package com.fku.merchant.app.repository.order;

import com.fku.merchant.app.repository.MerchantBaseRepository;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;

public interface OrderRepository extends MerchantBaseRepository {
    ExchangeOrder findLastOrder();
    ExchangeOrder saveOrder(ExchangeOrder exchangeOrder);
}
