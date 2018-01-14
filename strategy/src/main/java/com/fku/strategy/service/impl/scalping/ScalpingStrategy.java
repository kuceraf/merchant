package com.fku.strategy.service.impl.scalping;

import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.service.impl.ATradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.stereotype.Component;
import org.ta4j.core.indicators.MACDIndicator;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

import static com.fku.strategy.service.impl.StrategyHelper.calculateSellPriceWithRequiredProfit;
import static com.fku.strategy.service.impl.StrategyHelper.isOrderFilled;

@Log4j2
@Component
public class ScalpingStrategy extends ATradingStrategy {

    private MACDIndicator macdIndicator;
    private final ExchangeOrderRepository exchangeOrderRepository;
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
    public static final BigDecimal MINIMUM_PERCENTAGE_PROFIT = BigDecimal.valueOf(0.02);
    public ScalpingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService);
        this.exchangeOrderRepository = exchangeOrderRepository;
    }

    @PostConstruct
    private void info() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchangeService.getExchangeName());

        // TODO
//        macdIndicator = new MACDIndicator();
    }

    @Override
    public void executeStrategySpecific() throws Exception {
        ExchangeOrder lastOrder = exchangeOrderRepository.findLast();

        if (lastOrder == null) {
            log.info("First time strategy execution - placing new BUY order");
            placeBuyOrderAtCurrentPrice();
        } else {
            switch (lastOrder.getType()) {
                case BID: //= buying order
                    // last time we place buy order - now we sell it with profit
                    sellWithProfitWhenLastBuyOrderIsFilled(lastOrder);
                    break;
                case ASK: //= selling order
                    buyWhenLastSellOrderIsFilled(lastOrder);
                    break;
                default:
                    throw new IllegalStateException("Unknown order type");
            }
        }
    }

    /**
     * Check if the last SELL order has been filled
     * If it has been filled place new BUY order.
     * @param lastOrder last exchange order
     * @throws Exception from exchange
     */
    private void buyWhenLastSellOrderIsFilled(ExchangeOrder lastOrder) throws Exception {
        if(lastOrder.getType() == Order.OrderType.BID) {
            throw new MerchantStrategyException("Wrong strategy execution flow - two successive BUY orders");
        }
        OpenOrders openOrders = exchangeService.getOpenOrders();
        if (isOrderFilled(openOrders, lastOrder)) {
            // The last sell order is filled - we can create BUY order now
            log.info("^^^ Yay!!! Last SELL order (Id:{}) filled at {}",
                    lastOrder.getId(),
                    lastOrder.getPrice());
            placeBuyOrderAtCurrentPrice();
        } else {
        // SELL order not filled yet.

        }
    }

    /**
     * Check if the last BUY order has been filled.
     * If it has been filled place new SELL order with increased price to gain required profit
     * @param lastOrder last exchange order
     * @throws Exception from the exchange
     */
    private void sellWithProfitWhenLastBuyOrderIsFilled(ExchangeOrder lastOrder) throws Exception {
        if(lastOrder.getType() == Order.OrderType.ASK) {
            throw new MerchantStrategyException("Wrong strategy execution flow - two successive SELL orders");
        }
        OpenOrders openOrders = exchangeService.getOpenOrders();
        if (isOrderFilled(openOrders, lastOrder)) {
        // The last buy order is filled - we can create SELL order now
            log.info("^^^ Yay!!! Last BUY order (Id:{}) filled at {}",
                    lastOrder.getId(),
                    lastOrder.getPrice());

            BigDecimal newAskPrice = calculateSellPriceWithRequiredProfit(lastOrder.getPrice(), MINIMUM_PERCENTAGE_PROFIT);
            ExchangeOrder newExchangeSellOrder = exchangeService.placeOrder(Order.OrderType.ASK, lastOrder.getAmount(), newAskPrice);
            exchangeOrderRepository.save(newExchangeSellOrder);
        } else {
            // BUY order not filled yet.
            log.info("!!! Still have BUY Order {} waiting to fill at {} holding last BUY order...",
                    lastOrder.getId(),
                    lastOrder.getPrice());
        }
    }

    private void placeBuyOrderAtCurrentPrice() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        InstrumentPrice currentPrices = exchangeService.getCurrentPrices();
        BigDecimal currentBidPrice = currentPrices.getBidPrice();
        ExchangeOrder newExchangeOrder = exchangeService.placeBuyOrder(currentBidPrice, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
        exchangeOrderRepository.save(newExchangeOrder);
    }
}
