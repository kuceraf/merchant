package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.ActiveExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;
import java.util.UUID;

import static com.fku.exchange.service.impl.dummy.Constants.INSTRUMENT_LAST_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_BID_PRICE;

public class MockedActiveExchangeService implements ActiveExchangeService {
    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException {
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                orderType,
                limitPrice,
                baseCurrencyAmount);
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                Order.OrderType.BID,
                currentBidPrice,
                baseCurrencyAmount);
    }

    @Override
    public ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyBuyOrderAmount) throws MerchantExchangeException {
        return this.placeBuyOrder(LIMIT_BID_PRICE, counterCurrencyBuyOrderAmount);
    }
}
