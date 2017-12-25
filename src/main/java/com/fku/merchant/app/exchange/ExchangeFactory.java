package com.fku.merchant.app.exchange;

import lombok.Setter;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.gdax.GDAXExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.util.Assert;

public class ExchangeFactory extends AbstractFactoryBean<Exchange> {
    private String apiKey;
    private String secret;
    private String passphrase;
    private ExchangeType exchangeType;

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
            default:
                throw new IllegalStateException();
        }
    }

    @Value("${exchange.apiKey}")
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Value("${exchange.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${exchange.passphrase}")
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    @Value("${exchange.type}")
    public void setExchangeType(String exchangeType) {
        Assert.notNull(exchangeType, "ExchangeType must be configured in property file");
        this.exchangeType = ExchangeType.valueOf(exchangeType.toUpperCase());
    }
}
