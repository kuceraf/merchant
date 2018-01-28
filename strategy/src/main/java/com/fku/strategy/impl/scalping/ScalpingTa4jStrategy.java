package com.fku.strategy.impl.scalping;

import com.fku.exchange.domain.ExchangeOrder;
import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.strategy.impl.ATradingStrategy;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Log4j2
@Component
public class ScalpingTa4jStrategy extends ATradingStrategy {

    public ScalpingTa4jStrategy(ExchangeService exchangeService, ExchangeOrderRepository exchangeOrderRepository) {
        super(exchangeService, exchangeOrderRepository);
    }

    @PostConstruct
    public void init() {
        log.info("Strategy [{}] initialization", this.getClass());
    }

    @Override
    protected void executeStrategySpecific() throws Exception {
        ExchangeOrder lastOrder = exchangeOrderRepository.findLast();
        if (lastOrder == null) {
            log.info("First time strategy execution");
//            placeBuyOrderAtCurrentPrice();
        } else {

        }
    }
}
