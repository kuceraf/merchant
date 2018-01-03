package com.fku.merchant.app.exchange.impl.dummy;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.exchange.ExchangeHelper;
import com.fku.merchant.app.exchange.impl.AExchangeService;
import com.fku.merchant.app.repository.order.domain.InstrumentPrice;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import com.fku.merchant.app.repository.order.domain.OrderType;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.UUID;

public class DummyExchangeService extends AExchangeService {
    public DummyExchangeService(Exchange xchange, CurrencyPair currencyPair) {
        super(xchange, currencyPair);
    }

    @Override
    public String getExchangeName() {
        return "DUMMY";
    }

    @Override
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantNonFatalException {
        return DummyExchangeDataFactory.createInstrumentPrice();
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantNonFatalException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, DummyExchangeDataFactory.INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                OrderType.BUY,
                currentBidPrice,
                baseCurrencyAmount);
    }

    @Override
    public OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantNonFatalException {
        return new OpenOrders(Collections.emptyList());
    }
}
