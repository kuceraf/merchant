package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.domain.ExchangeOrder;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.math.BigDecimal;
import java.util.UUID;

import static com.fku.exchange.service.impl.dummy.Constants.*;

public class DummyExchangeService implements ExchangeService {
    @Override
    public String getExchangeName() {
        return "DUMMY";
    }

    @Override
    public CurrencyPair getCurrencyPair() {
        return CurrencyPair.BTC_EUR;
    }

    @Override
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        return new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE);
    }

    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice) throws MerchantExchangeException, MerchantExchangeNonFatalException {
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                orderType,
                limitPrice,
                baseCurrencyAmount);
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws MerchantExchangeException, MerchantExchangeNonFatalException {
        BigDecimal baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, INSTRUMENT_LAST_PRICE);
        return new ExchangeOrder(
                UUID.randomUUID().toString(),
                Order.OrderType.BID,
                currentBidPrice,
                baseCurrencyAmount);
    }

    @Override
    public OpenOrders getOpenOrders() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        return DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder(EXISTING_OPEN_ORDER_ID);
    }
}
