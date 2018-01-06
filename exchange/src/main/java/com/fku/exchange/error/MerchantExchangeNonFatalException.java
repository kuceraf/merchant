package com.fku.exchange.error;

public final class MerchantExchangeNonFatalException extends Exception {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantExchangeNonFatalException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantExchangeNonFatalException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantExchangeNonFatalException(String msg, Throwable e) {
        super(msg, e);
    }
}
