package com.fku.merchant.app.core.error;

public abstract class MerchantException extends Exception {
    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantException(String msg, Throwable e) {
        super(msg, e);
    }
}
