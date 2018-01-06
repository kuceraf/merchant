package com.fku.exchange.error;

public final class MerchantExchangeException extends Exception {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantExchangeException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantExchangeException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantExchangeException(String msg, Throwable e) {
        super(msg, e);
    }
}
