package com.fku.merchant.app.core.exception;

public final class MerchantNonFatalException extends MerchantException {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantNonFatalException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantNonFatalException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantNonFatalException(String msg, Throwable e) {
        super(msg, e);
    }
}
