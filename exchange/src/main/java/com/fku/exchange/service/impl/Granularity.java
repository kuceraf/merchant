package com.fku.exchange.service.impl;

public enum  Granularity {
    ONE_MINUTE(60),
    FIVE_MINUTES(300),
    FIVETEN_MINUTES(900),
    ONE_HOUR(3600),
    SIX_HOURS(21600),
    ONE_DAY(86400);

    private long seconds;
    Granularity(long seconds){
        this.seconds = seconds;
    }

    public long getSeconds() {
        return seconds;
    }
}
