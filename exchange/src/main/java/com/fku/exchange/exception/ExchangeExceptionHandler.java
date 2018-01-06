package com.fku.exchange.exception;

import com.fku.exchange.exception.ExchangeException;
import com.fku.exchange.exception.ExchangeNonFatalException;
import com.fku.exchange.exception.NonFatalHttpErrorCodeType;
import lombok.extern.log4j.Log4j2;
import si.mazi.rescu.HttpStatusException;

import java.io.IOException;

@Log4j2
public class ExchangeExceptionHandler {
    public static void handleException(Exception e) throws ExchangeNonFatalException, ExchangeException {
        if (e instanceof HttpStatusException) {
            HttpStatusException httpException = (HttpStatusException) e;
            if (NonFatalHttpErrorCodeType.containsHttpCode(httpException.getHttpStatusCode())) {
                final String errorMsg = "Non-fatal network exception, code ["+httpException.getHttpStatusCode()+"]";
                log.error(errorMsg);
                throw new ExchangeNonFatalException(errorMsg);
            } else {
                final String errorMsg = "Fatal network exception, code ["+httpException.getHttpStatusCode()+"]";
                log.error(errorMsg + " {}", httpException);
                throw new ExchangeException(errorMsg);
            }
        } else if (e instanceof IOException) {
            // TODO
        } else {
            final String errorMsg = "Unknown error occurred during exchange interaction";
            log.error(errorMsg, e);
            throw new ExchangeException(errorMsg, e);
        }
    }
}
