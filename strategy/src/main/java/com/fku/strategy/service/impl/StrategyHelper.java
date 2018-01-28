package com.fku.strategy.service.impl;

import com.fku.exchange.domain.ExchangeOrder;
import org.knowm.xchange.dto.trade.LimitOrder;
import org.knowm.xchange.dto.trade.OpenOrders;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class StrategyHelper {

    private StrategyHelper() {
        throw new AssertionError("This class isn't designed for instantiation");
    }

    /**
     * Search order in open orders list. If order is found it was'nt filled
     * @param openOrders open orders from xchange
     * @param order examined order (filled/unfilled)
     * @return true, if order is filled
     */
    public static boolean isOrderFilled(@Nonnull OpenOrders openOrders, @Nonnull ExchangeOrder order) {
        boolean lastOrderFound = false;
        for (final LimitOrder openOrder : openOrders.getOpenOrders()) {
            if (openOrder.getId().equals(order.getId())) {
                lastOrderFound = true;
                break;
            }
        }
        // If the order is not there, it must be filled.
        return !lastOrderFound;
    }

    /**
     *
     * @param buyOrderPrice the base price for calculation
     * @param requiredPercentageProfit in percentage points
     * @return new price to reach required gain
     */
    public static BigDecimal calculateSellPriceWithRequiredProfit(@Nonnull  BigDecimal buyOrderPrice, @Nonnull BigDecimal requiredPercentageProfit) {
        BigDecimal requiredAmountGain = buyOrderPrice.multiply(requiredPercentageProfit);
        BigDecimal sellPriceWithRequiredGain = requiredAmountGain.add(buyOrderPrice);
        return sellPriceWithRequiredGain.setScale(8, RoundingMode.HALF_UP);
    }
}
