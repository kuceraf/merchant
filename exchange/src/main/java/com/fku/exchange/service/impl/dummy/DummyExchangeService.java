package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.service.impl.ExchangeHelper;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.OrderType;
import com.fku.exchange.service.impl.AExchangeService;
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
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        return DummyExchangeDataFactory.createInstrumentPrice();
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantExchangeNonFatalException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, DummyExchangeDataFactory.INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                OrderType.BUY,
                currentBidPrice,
                baseCurrencyAmount);
    }

    @Override
    public OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        return new OpenOrders(Collections.emptyList());
    }
}
