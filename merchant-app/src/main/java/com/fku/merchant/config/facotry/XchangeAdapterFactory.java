package com.fku.merchant.config.facotry;

import com.fku.exchange.SupportedExchangeType;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.gdax.GDAXExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

public class XchangeAdapterFactory extends AbstractFactoryBean<Exchange> {
    @Value("${exchange.apiKey}")
    private String apiKey;
    @Value("${exchange.secret}")
    private String secret;
    @Value("${exchange.passphrase}")
    private String passphrase;

    private SupportedExchangeType exchangeType;

    public XchangeAdapterFactory(SupportedExchangeType exchangeType) {
        this.exchangeType = exchangeType;
    }

    @Override
    public Class<?> getObjectType() {
        return Exchange.class;
    }

    @Override
    protected Exchange createInstance() throws Exception { //by default factory crate singleton, this is called once
        switch (exchangeType) {
            case GDAX:
                Assert.notNull(apiKey, "ApiKey must be configured for GDAX exchange in property file");
                Assert.notNull(secret, "Secret must be configured for GDAX exchange in property file");
                Assert.notNull(passphrase, "Passphrase must be configured for GDAX exchange in property file");
                ExchangeSpecification exSpec = new GDAXExchange().getDefaultExchangeSpecification();
                exSpec.setApiKey(apiKey);
                exSpec.setSecretKey(secret);
                exSpec.setExchangeSpecificParametersItem("passphrase", passphrase);
                return org.knowm.xchange.ExchangeFactory.INSTANCE.createExchange(exSpec);
            case DUMMY:
                return null;
            default:
                throw new IllegalStateException();
        }
    }
}
