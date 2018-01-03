package com.fku.merchant.app.exchange;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantNonFatalException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantNonFatalException;
    OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantNonFatalException;
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws MerchantExchangeException, MerchantNonFatalException;
}
