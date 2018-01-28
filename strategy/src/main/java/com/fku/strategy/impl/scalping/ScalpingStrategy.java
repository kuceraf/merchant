package com.fku.strategy.impl.scalping;

import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.error.MerchantExchangeNonFatalException;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.domain.InstrumentPrice;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.impl.ATradingStrategy;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import static com.fku.strategy.impl.StrategyHelper.calculateSellPriceWithRequiredProfit;
import static com.fku.strategy.impl.StrategyHelper.isOrderFilled;

@Log4j2
public class ScalpingStrategy extends ATradingStrategy {

    /**
     * Pro maket BTC/EUR je to 10EUR (counterCurrencyBuyOrderAmount = 10)
     *
     * Napr:
     * counterCurrencyBuyOrderAmount = 10
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
    @Setter
    @Value("${strategy.counterCurrencyBuyOrderAmount}")
    public BigDecimal counterCurrencyBuyOrderAmount;

    @Setter
    @Value("${strategy.minimumPercentageProfit}")
    private BigDecimal minimumPercentageProfit; // The minimum % gain was to achieve before placing a SELL order.


    public ScalpingStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @PostConstruct
    private void info() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchangeService.getExchangeName());
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
                    sellWithProfitIfLastBuyOrderIsFilled(lastOrder);
                    break;
                case ASK: //= selling order
                    buyIfLastSellOrderIsFilled(lastOrder);
                    break;
                default:
                    throw new IllegalStateException("Unknown order type");
            }
        }
    }

    @Override
    protected void checkProfitability() throws MerchantStrategyException {
        ExchangeOrder lastOrder = exchangeOrderRepository.findLast();
        if(lastOrder != null && lastOrder.isAsk()) {
            // last order is SELL order - the calculated profit will be achieved after the sell order fills
            List<ExchangeOrder> buyOrders = exchangeOrderRepository.findBids();
            List<ExchangeOrder> sellOrders = exchangeOrderRepository.findAsks();
            CurrencyPair currencyPair = exchangeService.getCurrencyPair();
            if(!ProfitabilityHelper.isProfitable(buyOrders, sellOrders, currencyPair)) {
                throw new MerchantStrategyException("Strategy is lossy - stopping execution");
            }
        }
    }

    /**
     * Check if the last SELL order has been filled
     * If it has been filled place new BUY order.
     * @param lastAskOrder last exchange order
     * @throws Exception from exchange
     */
    private void buyIfLastSellOrderIsFilled(ExchangeOrder lastAskOrder) throws Exception {
        if(lastAskOrder.getType() == Order.OrderType.BID) {
            throw new MerchantStrategyException("Wrong strategy execution flow - two successive BUY orders");
        }
        OpenOrders openOrders = exchangeService.getOpenOrders();
        if (isOrderFilled(openOrders, lastAskOrder)) {
            // The last sell order is filled - we can create BUY order now
            log.info("^^^ Yay!!! Last SELL order (Id:{}) filled at {}",
                    lastAskOrder.getId(),
                    lastAskOrder.getPrice());
            placeBuyOrderAtCurrentPrice();
        } else {
        // SELL order not filled yet - log why
            BigDecimal currentAskPrice = exchangeService.getCurrentPrices().getAskPrice();
            BigDecimal lastOrderAksPrice = lastAskOrder.getPrice();
            if (currentAskPrice.compareTo(lastOrderAksPrice) < 0) {
                log.info("<<< Current ask price {} is LOWER then last SELL order price {} - holding last SELL order...",
                        currentAskPrice,
                        lastOrderAksPrice);
            } else if (currentAskPrice.compareTo(lastOrderAksPrice) > 0) {
                log.error( ">>> Current ask price {} is HIGHER than last SELL order price {} - IMPOSSIBLE! Merchant must have sold?????",
                        currentAskPrice,
                        lastOrderAksPrice);

            } else if (currentAskPrice.compareTo(lastOrderAksPrice) == 0) {
                log.info("=== Current ask price {} is EQUAL to last order price {} - holding last SELL order...",
                        currentAskPrice,
                        lastOrderAksPrice);
            }
        }
    }

    /**
     * Check if the last BUY order has been filled.
     * If it has been filled place new SELL order with increased price to gain required profit
     * @param lastBuyOrder last exchange order
     * @throws Exception from the exchange
     */
    private void sellWithProfitIfLastBuyOrderIsFilled(ExchangeOrder lastBuyOrder) throws Exception {
        if(lastBuyOrder.getType() == Order.OrderType.ASK) {
            throw new MerchantStrategyException("Wrong strategy execution flow - two successive SELL orders");
        }
        OpenOrders openOrders = exchangeService.getOpenOrders();
        if (isOrderFilled(openOrders, lastBuyOrder)) {
        // The last buy order is filled - we can create SELL order now
            log.info("^^^ Yay!!! Last BUY order (Id:{}) filled at {}",
                    lastBuyOrder.getId(),
                    lastBuyOrder.getPrice());

            BigDecimal newAskPrice = calculateSellPriceWithRequiredProfit(lastBuyOrder.getPrice(), minimumPercentageProfit);
            ExchangeOrder newExchangeSellOrder = exchangeService.placeOrder(Order.OrderType.ASK, lastBuyOrder.getAmount(), newAskPrice);
            exchangeOrderRepository.save(newExchangeSellOrder);
        } else {
            // BUY order not filled yet.
            log.info("!!! Still have BUY Order {} waiting to fill at {} holding last BUY order...",
                    lastBuyOrder.getId(),
                    lastBuyOrder.getPrice());
        }
    }

    private void placeBuyOrderAtCurrentPrice() throws MerchantExchangeException, MerchantExchangeNonFatalException {
        InstrumentPrice currentPrices = exchangeService.getCurrentPrices();
        BigDecimal currentBidPrice = currentPrices.getBidPrice();
        ExchangeOrder newExchangeOrder = exchangeService.placeBuyOrder(currentBidPrice, counterCurrencyBuyOrderAmount);
        exchangeOrderRepository.save(newExchangeOrder);
    }
}
