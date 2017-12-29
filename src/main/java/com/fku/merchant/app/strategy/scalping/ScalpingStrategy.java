package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.core.MerchantException;
import com.fku.merchant.app.exchange.Exchange;
import com.fku.merchant.app.strategy.ATradingStrategy;
import com.fku.merchant.app.strategy.dto.PricePair;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Log4j2
@Component
public class ScalpingStrategy extends ATradingStrategy {

    // Constants
    /**
     * Pro maket BTC/EUR je to 10EUR (COUNTER_CURRENCY_BUY_ORDER_AMOUNT = 10)
     *
     * Napr:
     * COUNTER_CURRENCY_BUY_ORDER_AMOUNT = 10
     * Pri nakupu na marketu BTC/EUR definuji, ze chci nakupovat BTC za 10 EUR. Pro realizaci nakupu udelam vypocet:
     * BTC buy order = 10 EUR / (kolik EUR stoji 1 BTC - tj cena BTC/EUR)
     *
     * Mam 10 EUR
     * Chci X BTC
     * Cena 7,599.99 EUR za 1 BTC (nakupni cena instrumentu BTC/EUR = 7,599.99 EUR)
     * X = 10 / 7,599.99
     * X = 0,00131579 BTC
     * Za 10 EUR si mohu koupit 0,00131579 BTC pri cene BTC/EUR = 7,599.99.
     */
    public static final BigDecimal COUNTER_CURRENCY_BUY_ORDER_AMOUNT = BigDecimal.valueOf(700);

    /**
     * The minimum % gain was to achieve before placing a SELL oder.
     */
    public static final BigDecimal MINIMUM_PERCENTAGE_GAIN = BigDecimal.valueOf(0.02);

    private OrderState lastOrder;

    public ScalpingStrategy(Exchange exchange) {
        super(exchange);
    }

    @PostConstruct
    private void init() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchange.getExchangeName());
    }

    @Override
    public void executeStrategySpecific() throws MerchantException {
        PricePair currentPrices = exchange.getCurrentPrices();
        currentPrices.getAskPrice();

        executeFirstTimeBuyOrder();
    }

    private void executeFirstTimeBuyOrder() throws MerchantException {

//        try {
//            // Grab the latest order book for the market.
//
//            // ASK = SELL
//            orderBook.getBids().stream()
//                    .findFirst()
//                    .ifPresent((LimitOrder buyLimitOrder) -> {
//                        // v orderBook najdu prvni bid limitOrder (obsahuje cenu za kterou se da prave nakoupit - nejvyssi cena)
//                        BigDecimal currentBuyPrice = buyLimitOrder.getLimitPrice();
//
//                        LimitOrder newBuyLimitOrder = new LimitOrder(
//                                (Order.OrderType.BID),
//                                new BigDecimal(".01"), // The amount to trade
//                                CurrencyPair.BTC_EUR,
//                                null,
//                                null,
//                                currentBuyPrice // In a BID this is the highest acceptable price
//                        );
//
////                        try {
////                            exchange.getTradeService().placeLimitOrder(newBuyLimitOrder);
////                        } catch (IOException e) {
////                            log.error("Failed to place buy limit order", e);
////                        }
//                    });
//
////            LimitOrder.Builder.from()
//        } catch (IOException e) {
//            log.error("Failed to get Order book",e);
//        }
    }
}
