package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.ActiveExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;

import java.math.BigDecimal;
import java.util.UUID;

import static com.fku.exchange.service.impl.dummy.Constants.INSTRUMENT_LAST_PRICE;
import static com.fku.exchange.service.impl.dummy.Constants.LIMIT_BID_PRICE;

@Log4j2
public class MockedActiveExchangeService implements ActiveExchangeService {
    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException {
        log.info("MOCKED - Placing order [orderType:{}, baseCurrencyAmount:{}, limitPrice:{}]", orderType, baseCurrencyAmount, limitPrice);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                orderType,
                limitPrice,
                baseCurrencyAmount);
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, INSTRUMENT_LAST_PRICE);
        return placeOrder(Order.OrderType.BID, baseCurrencyAmount, currentBidPrice);
    }

    @Override
    public ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyAmount) throws MerchantExchangeException {
        return this.placeBuyOrder(LIMIT_BID_PRICE, counterCurrencyAmount);
    }
}
