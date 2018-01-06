package com.fku.exchange.exception;

public final class ExchangeNonFatalException extends Exception {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public ExchangeNonFatalException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public ExchangeNonFatalException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public ExchangeNonFatalException(String msg, Throwable e) {
        super(msg, e);
    }
}
