package com.fku.merchant.app.core;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class ShutdownManager {
    @Autowired
    private ApplicationContext appContext;

    public void initiateShutdown(int returnCode) {
        log.info("Shutting down application [{}], context ID [{}]", appContext.getApplicationName(), appContext.getId());
        SpringApplication.exit(appContext, () -> returnCode);
    }
}
