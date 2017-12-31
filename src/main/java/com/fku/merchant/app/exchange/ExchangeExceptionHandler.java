package com.fku.merchant.app.exchange;

import com.fku.merchant.app.core.exception.MerchantExchangeException;
import com.fku.merchant.app.core.exception.MerchantNonFatalException;
import com.fku.merchant.app.core.exception.NonFatalHttpErrorCodeType;
import lombok.extern.log4j.Log4j2;
import si.mazi.rescu.HttpStatusException;

import java.io.IOException;

@Log4j2
public class ExchangeExceptionHandler {
    public static void handleException(Exception e) throws MerchantNonFatalException, MerchantExchangeException {
        if (e instanceof HttpStatusException) {
            HttpStatusException httpException = (HttpStatusException) e;
            if (NonFatalHttpErrorCodeType.containsHttpCode(httpException.getHttpStatusCode())) {
                final String errorMsg = "Non-fatal network exception, code ["+httpException.getHttpStatusCode()+"]";
                log.error(errorMsg, httpException);
                throw new MerchantNonFatalException(errorMsg);
            } else {
                final String errorMsg = "Fatal network exception, code ["+httpException.getHttpStatusCode()+"]";
                log.error(errorMsg + " {}", httpException);
                throw new MerchantExchangeException(errorMsg);
            }
        } else if (e instanceof IOException) {
            // TODO
        } else {
            final String errorMsg = "Unknown error occurred during exchange interaction";
            log.error(errorMsg, e);
            throw new MerchantExchangeException(errorMsg, e);
        }
    }
}
