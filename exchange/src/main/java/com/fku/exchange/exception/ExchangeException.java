package com.fku.exchange.exception;

public class ExchangeException extends Exception {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public ExchangeException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public ExchangeException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public ExchangeException(String msg, Throwable e) {
        super(msg, e);
    }
}
