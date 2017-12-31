package com.fku.merchant.app.strategy.scalping;

import com.fku.merchant.app.core.exception.MerchantException;
import com.fku.merchant.app.exchange.ExchangeService;
import com.fku.merchant.app.strategy.ATradingStrategy;
import com.fku.merchant.app.strategy.dto.OrderState;
import com.fku.merchant.app.strategy.dto.PricePair;
import lombok.extern.log4j.Log4j2;
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

    public ScalpingStrategy(ExchangeService exchangeService) {
        super(exchangeService);
    }

    @PostConstruct
    private void init() {
        log.info("Strategy [{}] is initialized", this.getClass());
        log.info("Strategy executes on [{}] exchange market", exchangeService.getExchangeName());
    }

    @Override
    public void executeStrategySpecific() throws MerchantException {
        PricePair currentPrices = exchangeService.getCurrentPrices();
        BigDecimal currentBidPrice = currentPrices.getBidPrice();

        // TODO look up in DB
        if (lastOrder == null) {
            // zaciname - musime nejdrive nakoupit
            log.debug("First time strategy execution - placing new BUY order");
            exchangeService.placeBuyOrder(currentBidPrice, COUNTER_CURRENCY_BUY_ORDER_AMOUNT);
//            executeFirstTimeBuyOrder(currentBidPrice);
        } else {
            switch (lastOrder.type) {
                case BUY:
                    // umistili jsme pozadavek na nakup - zkusime prodej se ziskem
//                TODO tryPlaceSellOrder();
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
}
