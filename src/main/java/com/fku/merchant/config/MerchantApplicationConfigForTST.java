package com.fku.merchant.config;

import com.fku.merchant.app.exchange.ExchangeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("classpath:TST.properties")
@Profile("TST")
public class MerchantApplicationConfigForTST {
}
