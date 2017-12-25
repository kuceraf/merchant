package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.strategy.StrategyException;
import com.fku.merchant.app.strategy.TradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.marketdata.OrderBook;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;

@Log4j2
@Component
public class ScalpingStrategy implements TradingStrategy {

    private final CurrencyPair CURRENCY_PAIR = CurrencyPair.BTC_EUR;
    private final Exchange exchange;

    public ScalpingStrategy(@Qualifier("exchange") Exchange exchange) {
        this.exchange = exchange;
    }

    @PostConstruct
    private void init() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchange.getExchangeSpecification().getExchangeName());
        log.info("Trading currency pair is [{}]", CURRENCY_PAIR);
    }

    @Override
    public void execute() throws StrategyException {
        log.info("Strategy executed !!!");
//        firstRun();
    }

    private void firstRun() {
        try {
            OrderBook orderBook = exchange.getMarketDataService().getOrderBook(CURRENCY_PAIR);
//            exchange.getMarketDataService().getOrderBook(CURRENCY_PAIR);
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
                            exchange.getTradeService().placeLimitOrder(newBuyLimitOrder);
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
