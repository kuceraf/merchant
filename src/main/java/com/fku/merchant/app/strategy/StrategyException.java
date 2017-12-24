package com.fku.merchant.app.strategy;

public final class StrategyException extends Exception {

    private static final long serialVersionUID = -5066890753686004758L;

    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public StrategyException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public StrategyException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public StrategyException(String msg, Throwable e) {
        super(msg, e);
    }
}
