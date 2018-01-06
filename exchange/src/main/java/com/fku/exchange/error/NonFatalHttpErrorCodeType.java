package com.fku.exchange.error;

import java.util.Arrays;

public enum NonFatalHttpErrorCodeType {
    BAD_GATEWAY(502),
    SERVICE_UNAVAILABLE(503),
    GATEWAY_TIMEOUT(504),
    UNKNOWN_ERROR(520),
    CONNECTION_TIME_OUT(522),
    SSL_HANDSHAKE_FAILED(525)
    ;

    private int httpCode;
    NonFatalHttpErrorCodeType(int httpCode) {
        this.httpCode = httpCode;
    }

    public static boolean containsHttpCode(int httpCode) {
        return Arrays.stream(NonFatalHttpErrorCodeType.values())
                .anyMatch(type -> type.httpCode == httpCode);
    }
}
