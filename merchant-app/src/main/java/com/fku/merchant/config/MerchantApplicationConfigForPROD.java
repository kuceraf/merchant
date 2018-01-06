package com.fku.merchant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:PROD/merchant.properties")
@Profile("PROD")
public class MerchantApplicationConfigForPROD {
}
