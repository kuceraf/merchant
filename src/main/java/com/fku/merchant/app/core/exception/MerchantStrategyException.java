package com.fku.merchant.app.core.exception;

/**
 * When this exception is thrown is must cause the application shutdown
 */
public final class MerchantStrategyException extends MerchantException {

    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantStrategyException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantStrategyException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantStrategyException(String msg, Throwable e) {
        super(msg, e);
    }
}
