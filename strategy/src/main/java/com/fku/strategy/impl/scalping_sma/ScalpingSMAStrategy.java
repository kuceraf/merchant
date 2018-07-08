package com.fku.strategy.impl.scalping_sma;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.error.MerchantExchangeException;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.Granularity;
import com.fku.strategy.TradingStrategy;
import com.fku.strategy.error.MerchantStrategyException;
import com.fku.strategy.error.StrategyNonFatalException;
import com.fku.strategy.impl.ATradingStrategy;
import com.fku.ta.TechnicalAnalysis;
import com.fku.ta.impl.SmaAndClosePriceTA;
import lombok.extern.log4j.Log4j2;
import org.knowm.xchange.dto.Order;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.beans.factory.InitializingBean;
import org.ta4j.core.*;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.fku.strategy.impl.StrategyHelper.calculateSellPriceWithRequiredProfit;
import static com.fku.strategy.impl.StrategyHelper.isOrderFilled;


@Log4j2
@Deprecated
public class ScalpingSMAStrategy extends ATradingStrategy implements TradingStrategy, InitializingBean {
    private static final Granularity GRANULARITY = Granularity.ONE_MINUTE;

    /** Close price of the last tick */
    private TechnicalAnalysis technicalAnalysis;

    public ScalpingSMAStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TimeSeries timeSeries = new BaseTimeSeries(exchangeService.getHistoricalBars());
        technicalAnalysis = new SmaAndClosePriceTA(timeSeries);
    }


    public TechnicalAnalysis getTechnicalAnalysis() {
        return technicalAnalysis;
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        ExchangeOrder lastOrder = exchangeOrderRepository.findLast();
        refreshTimeSeries();

        if (lastOrder == null) {
            log.info("No previous orders");
            if (technicalAnalysis.shouldEnter()) {
                ExchangeOrder newExchangeOrder = exchangeService.placeBuyOrderAtCurrentPrice(counterCurrencyBuyOrderAmount);
                exchangeOrderRepository.save(newExchangeOrder);
            }
        } else {
            switch (lastOrder.getType()) {
                case BID: //= buying order
                    // last time we place buy order - now we sell it with profit
                    sellWithProfitIfLastBuyOrderIsFilled(lastOrder);
                    break;
                case ASK: //= selling order
                    if(technicalAnalysis.shouldEnter()) {
                        buyIfLastSellOrderIsFilled(lastOrder);
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown order type");
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
            ExchangeOrder newExchangeOrder = exchangeService.placeBuyOrderAtCurrentPrice(counterCurrencyBuyOrderAmount);
            exchangeOrderRepository.save(newExchangeOrder);
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
        if(lastBuyOrder.getType() == org.knowm.xchange.dto.Order.OrderType.ASK) {
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

    private void refreshTimeSeries() throws MerchantExchangeException, StrategyNonFatalException {
        List<Bar> bars = exchangeService.getHistoricalBars();
        Bar newBar = bars.get(bars.size() - 1);
        TimeSeries timeSeries = technicalAnalysis.getTimeSeries();
        if (newBar.getEndTime().toInstant().compareTo(timeSeries.getLastBar().getEndTime().toInstant()) <= 0) {
            log.warn("Exchange returned bar witch has endTime [{}] before or equal to last endTime in series [{}] - skipping",
                    newBar.getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                    timeSeries.getLastBar().getEndTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );
            throw new StrategyNonFatalException("Cannot add new bar to time series");
        }

        log.info("Adding Bar ({})", newBar);
        technicalAnalysis.addBar(newBar);
    }
}
