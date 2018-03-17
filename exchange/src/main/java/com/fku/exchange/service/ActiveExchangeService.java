package com.fku.exchange.service;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;

public interface ActiveExchangeService {
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException;
    ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyBuyOrderAmount) throws MerchantExchangeException;

}
