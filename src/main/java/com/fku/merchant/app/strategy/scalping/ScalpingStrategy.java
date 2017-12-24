package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.exchange.GdaxExchangeFactory;
import com.fku.merchant.app.strategy.StrategyException;
import com.fku.merchant.app.strategy.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;

@Log4j2
@Component
public class ScalpingStrategy implements TradingStrategy {

    @Value("${exchange.gdax.apiKey}")
    private String apiKey;
    @Value("${exchange.gdax.secret}")
    private String secret;
    @Value("${exchange.gdax.passphrase}")
    private String passphrase;
    private final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;

    private Exchange gdaxExchange;

    @PostConstruct
    public void setUp() {
        gdaxExchange = GdaxExchangeFactory.createExchange(apiKey, secret, passphrase);
    }

    @Override
    public void execute() throws StrategyException {
        log.info("Strategy executed !!!");
//        firstRun();
    }

    private void firstRun() {
        try {
            OrderBook orderBook = gdaxExchange.getMarketDataService().getOrderBook(CURRENCY_PAIR);
//            gdaxExchange.getMarketDataService().getOrderBook(CURRENCY_PAIR);
            // BID = the price at which a market maker is willing to buy
            // ASK = SELL
            orderBook.getBids().stream()
                    .findFirst()
                    .ifPresent((LimitOrder buyLimitOrder) -> {
                        // v orderBook najdu prvni bid limitOrder (obsahuje cenu za kterou se da prave nakoupit - nejvyssi cena)
                        BigDecimal currentBuyPrice = buyLimitOrder.getLimitPrice();

                        LimitOrder newBuyLimitOrder = new LimitOrder(
                                (Order.OrderType.BID),
                                new BigDecimal(".01"), // The amount to trade
                                CurrencyPair.BTC_EUR,
                                null,
                                null,
                                currentBuyPrice // In a BID this is the highest acceptable price
                        );

                        try {
                            gdaxExchange.getTradeService().placeLimitOrder(newBuyLimitOrder);
                        } catch (IOException e) {
                            log.error("Failed to place buy limit order", e);
                        }
                    });

//            LimitOrder.Builder.from()
        } catch (IOException e) {
            log.error("Failed to get Order book",e);
        }
    }
}
