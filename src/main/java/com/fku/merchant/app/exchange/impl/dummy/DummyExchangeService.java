package com.fku.merchant.app.exchange.impl.dummy;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.exchange.PriceHelper;
import com.fku.merchant.app.exchange.impl.AExchangeService;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import com.fku.merchant.app.repository.order.domain.OrderType;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;

import java.math.BigDecimal;
import java.util.UUID;

public class DummyExchangeService extends AExchangeService {
    public DummyExchangeService(Exchange xchange, CurrencyPair currencyPair) {
        super(xchange, currencyPair);
    }

    @Override
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantNonFatalException {
        return DummyExchangeDataFactory.createInstrumentPrice();
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantNonFatalException {
        BigDecimal baseCurrencyAmount = PriceHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, DummyExchangeDataFactory.INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                OrderType.BUY,
                currentBidPrice,
                baseCurrencyAmount);
    }
}
