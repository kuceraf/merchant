package com.fku.strategy.impl.scalping_sma;

import com.fku.exchange.repository.ExchangeOrderRepository;
import com.fku.exchange.service.ExchangeService;
import com.fku.exchange.service.impl.gdax.GDAXExchangeService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.gdax.GDAXExchange;
import org.ta4j.core.TimeSeries;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class ScalpingSMAStrategyTest {
    private static ExchangeService gdaxExchangeServiceReal;
    private ExchangeOrderRepository exchangeOrderRepositoryMocked = mock(ExchangeOrderRepository.class);
    private ScalpingSMAStrategy strategyTested;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ExchangeSpecification exSpec = new GDAXExchange().getDefaultExchangeSpecification();
        Exchange xchangeReal = ExchangeFactory.INSTANCE.createExchange(exSpec);
        // TODO replace it by MOCK !!!
        gdaxExchangeServiceReal = new GDAXExchangeService(xchangeReal, CurrencyPair.BTC_EUR);
    }

    @Before
    public void setUp() throws Exception {
        strategyTested = new ScalpingSMAStrategy(gdaxExchangeServiceReal, exchangeOrderRepositoryMocked);
    }
}