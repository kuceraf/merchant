package com.fku.merchant.app.strategy.impl.scalping;

import com.fku.exchange.ExchangeService;
import com.fku.merchant.app.core.exception.MerchantStrategyException;
import com.fku.merchant.app.repository.order.OrderRepository;
import com.fku.exchange.domain.OrderType;
import com.fku.merchant.app.strategy.impl.ATradingStrategy;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.merchant.app.strategy.impl.StrategyHelper;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

@Log4j2
@Component
public class ScalpingStrategy extends ATradingStrategy {

    private final OrderRepository orderRepository;
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
    public ScalpingStrategy(ExchangeService exchangeService, OrderRepository orderRepository) {
        super(exchangeService);
        this.orderRepository = orderRepository;
    }

    @PostConstruct
    private void info() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchangeService.getExchangeName());
    }

    @Override
    public void executeStrategySpecific() throws Exception {
        ExchangeOrder lastOrder = orderRepository.findLastOrder();
        InstrumentPrice currentPrices = exchangeService.getCurrentPrices();
        BigDecimal currentBidPrice = currentPrices.getBidPrice();

        // TODO look up in DB
        if (lastOrder == null) {
            // first time execution
            log.debug("First time strategy execution - placing new BUY order");
            ExchangeOrder newExchangeOrder = exchangeService.placeBuyOrder(currentBidPrice, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
            orderRepository.saveOrder(newExchangeOrder);
        } else {
            switch (lastOrder.type) {
                case BUY:
                    // umistili jsme pozadavek na nakup - zkusime prodej se ziskem
                trySellWithProfit(lastOrder);
                    break;
                case SELL:
                    // co jsme nakoupili je prodano - nakoupime znovu
//               TODO tryPlaceBuyOrder(currentBidPrice, currentAskPrice);
                    break;
                default:
                    throw new IllegalStateException("Unknown order type");
            }
        }
    }

    void trySellWithProfit(ExchangeOrder lastOrder) throws Exception {
        if(lastOrder.type == OrderType.SELL){
            throw new MerchantStrategyException("Wrong strategy execution flow - two successive SELL orders");
        }
        OpenOrders openOrders = exchangeService.getOpenOrders();
        if (StrategyHelper.isOrderFilled(openOrders, lastOrder)) {
        // The last buy order was filled - we can create SELL order now
            log.info("^^^ Yay!!! Last BUY order (Id:{}) filled at {}",
                    lastOrder.id,
                    lastOrder.price);
        }

        BigDecimal newAskPrice = StrategyHelper.calculateSellPriceWithRequiredProfit(lastOrder.price, MINIMUM_PERCENTAGE_PROFIT);
        exchangeService.placeOrder(Order.OrderType.ASK, lastOrder.amount, newAskPrice);
        // TODO dodelat
    };
}
