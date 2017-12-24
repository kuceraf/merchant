package com.fku.merchant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@PropertySource("classpath:PROD.properties")
@Profile("PROD")
public class MerchantApplicationConfigForPROD
{
}
