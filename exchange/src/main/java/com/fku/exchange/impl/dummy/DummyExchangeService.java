package com.fku.exchange.impl.dummy;

import com.fku.exchange.impl.ExchangeHelper;
import com.fku.exchange.exception.ExchangeException;
import com.fku.exchange.exception.ExchangeNonFatalException;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.OrderType;
import com.fku.exchange.impl.AExchangeService;
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
    public InstrumentPrice getCurrentPrices() throws ExchangeException, ExchangeNonFatalException {
        return DummyExchangeDataFactory.createInstrumentPrice();
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws ExchangeException, ExchangeNonFatalException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, DummyExchangeDataFactory.INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                OrderType.BUY,
                currentBidPrice,
                baseCurrencyAmount);
    }

    @Override
    public OpenOrders getOpenOrders() throws ExchangeException, ExchangeNonFatalException {
        return new OpenOrders(Collections.emptyList());
    }
}
