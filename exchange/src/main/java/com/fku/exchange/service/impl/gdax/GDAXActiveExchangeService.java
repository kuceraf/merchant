package com.fku.exchange.service.impl.gdax;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.ExchangeExceptionHandler;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.service.ActiveExchangeService;
import com.fku.exchange.service.PassiveExchangeService;
import com.fku.exchange.service.impl.BaseExchangeService;
import com.fku.exchange.service.impl.ExchangeHelper;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;

import javax.annotation.Nonnull;
import java.math.BigDecimal;

@Log4j2
public class GDAXActiveExchangeService extends BaseExchangeService implements ActiveExchangeService {
    private final PassiveExchangeService passiveExchangeService;
    public GDAXActiveExchangeService(@Nonnull Exchange xchangeAdapter,
                                        @Nonnull CurrencyPair currencyPair,
                                        @Nonnull PassiveExchangeService passiveExchangeService) {
        super(xchangeAdapter, currencyPair);
        this.passiveExchangeService = passiveExchangeService;
    }

    @Override
    public ExchangeOrder placeOrder(Order.OrderType orderType, BigDecimal baseCurrencyAmount, BigDecimal limitPrice)
            throws MerchantExchangeException {
        ExchangeOrder exchangeOrder = null;
        LimitOrder newBuyLimitOrder = new LimitOrder.Builder(orderType, this.currencyPair)
                .originalAmount(baseCurrencyAmount)
                .limitPrice(limitPrice)
                .build();

        log.info("Sending {} order to exchange (price:{}, amount:{})--->", orderType.toString(), limitPrice, baseCurrencyAmount);

        try {
            String orderId = xchangeAdapter.getTradeService().placeLimitOrder(newBuyLimitOrder);
            exchangeOrder = new ExchangeOrder(
                    orderId,
                    orderType,
                    limitPrice,
                    baseCurrencyAmount
            );
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }

        return exchangeOrder;
    }

    @Override
    public ExchangeOrder placeBuyOrder(BigDecimal currentBidPrice, BigDecimal counterCurrencyAmount)
            throws MerchantExchangeException {
        BigDecimal baseCurrencyAmount = null;
        try {
            BigDecimal lastInstrumentMarketPrice = xchangeAdapter.getMarketDataService().getTicker(this.currencyPair).getLast();
            baseCurrencyAmount = ExchangeHelper.calculateBaseCurrencyAmount(counterCurrencyAmount, lastInstrumentMarketPrice);
        } catch (Exception e) {
            ExchangeExceptionHandler.handleException(e);
        }
        return placeOrder(Order.OrderType.BID, baseCurrencyAmount, currentBidPrice);
    }

    @Override
    public ExchangeOrder placeBuyOrderAtCurrentPrice(BigDecimal counterCurrencyBuyOrderAmount) throws MerchantExchangeException {
        return this.placeBuyOrder(passiveExchangeService.getCurrentPrices().getBidPrice(), counterCurrencyBuyOrderAmount);
    }
}
