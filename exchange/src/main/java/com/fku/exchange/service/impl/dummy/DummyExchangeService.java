package com.fku.exchange.service.impl.dummy;

import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.service.impl.Granularity;
import io.reactivex.Observable;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.ta4j.core.Bar;

import java.math.BigDecimal;
import java.util.List;
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
    public InstrumentPrice getCurrentPrices() throws MerchantExchangeException {
        return new InstrumentPrice(LIMIT_BID_PRICE, LIMIT_ASK_PRICE);
    }


    @Override
    public void nextBar() throws MerchantExchangeException {
        throw new UnsupportedOperationException("Not implemented!"); // TODO
    }

    @Override
    public Observable<Bar> getBarObservable() throws MerchantExchangeException {
        throw new UnsupportedOperationException("Not implemented!"); // TODO
    }

    @Override
    public List<Bar> getHistoricalBars() throws MerchantExchangeException {
        throw new UnsupportedOperationException("Not implemented!"); // TODO
    }

//    @Override
//    public Bar getBar(Granularity granularity) throws MerchantExchangeException, MerchantExchangeNonFatalException {
////     pouzit   DummyBarFactory.generateRandomBar(LAST_BAR_CLOSE_PRICE, GRANULARITY.getSeconds());
//        throw new UnsupportedOperationException("Not implemented!"); // TODO
//    }

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
        return this.placeBuyOrder(this.getCurrentPrices().getBidPrice(), counterCurrencyBuyOrderAmount);
    }

    @Override
    public OpenOrders getOpenOrders() throws MerchantExchangeException {
        return DummyExchangeDataFactory.getOpenOrdersWithAskOpenOrder(EXISTING_OPEN_ORDER_ID);
    }
}
