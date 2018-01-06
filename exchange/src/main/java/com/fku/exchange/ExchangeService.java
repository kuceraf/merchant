package com.fku.exchange;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.exchange.exception.ExchangeException;
import com.fku.exchange.exception.ExchangeNonFatalException;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;

import java.math.BigDecimal;

public interface ExchangeService {
    String getExchangeName();
    InstrumentPrice getCurrentPrices() throws ExchangeException, ExchangeNonFatalException;
    ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount) throws ExchangeException, ExchangeNonFatalException;
    OpenOrders getOpenOrders() throws ExchangeException, ExchangeNonFatalException;
    ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws ExchangeException, ExchangeNonFatalException;
}
