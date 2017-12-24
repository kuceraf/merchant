package com.fku.merchant.app.exchange;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.ExchangeSpecification;
import org.knowm.xchange.gdax.GDAXExchange;

public class GdaxExchangeFactory {
    public static Exchange createExchange(String apiKey, String secret, String passphrase) {
        ExchangeSpecification exSpec = new GDAXExchange().getDefaultExchangeSpecification();
        exSpec.setApiKey(apiKey);
        exSpec.setSecretKey(secret);
        exSpec.setExchangeSpecificParametersItem("passphrase", passphrase);
        return ExchangeFactory.INSTANCE.createExchange(exSpec);
    }
}
