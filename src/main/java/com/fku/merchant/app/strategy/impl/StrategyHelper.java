package com.fku.merchant.app.strategy.impl;

import com.fku.merchant.app.core.exception.MerchantException;
import com.fku.merchant.app.repository.order.domain.ExchangeOrder;
import com.sun.istack.internal.NotNull;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;

// TODO test this!!!
public class StrategyHelper {
    /**
     * Search order in open orders list. If order is found it was'nt filled
     * @param openOrders open orders from xchange
     * @param order examined order (filled/unfilled)
     * @return true, if order is filled
     */
    public static boolean isOrderFilled(OpenOrders openOrders, ExchangeOrder order) {
        Assert.notNull(openOrders, "OpenOrders mustn't be null");
        Assert.notNull(order, "Order mustn't be null");
        boolean lastOrderFound = false;
        for (final LimitOrder openOrder : openOrders.getOpenOrders()) {
            if (openOrder.getId().equals(order.id)) {
                lastOrderFound = true;
                break;
            }
        }
        // If the order is not there, it must be filled.
        return !lastOrderFound;
    }

    /**
     *
     * @param buyOrderPrice the base order for calculation
     * @param requiredPercentageProfit percentage points
     * @return new sell order price to reach required gain
     */
    public static BigDecimal calculateSellPriceWithRequiredProfit(BigDecimal buyOrderPrice, BigDecimal requiredPercentageProfit) {
        Assert.notNull(buyOrderPrice, "buyOrderPrice mustn't be null");
        Assert.notNull(requiredPercentageProfit, "requiredPercentageProfit mustn't be null");
        BigDecimal requiredAmountGain = buyOrderPrice.multiply(requiredPercentageProfit);
        BigDecimal sellPriceWithRequiredGain = requiredAmountGain.add(buyOrderPrice);
        return sellPriceWithRequiredGain.setScale(8, RoundingMode.HALF_UP);
    }
}
