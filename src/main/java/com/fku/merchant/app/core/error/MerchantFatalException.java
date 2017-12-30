package com.fku.merchant.app.core.error;

import com.fku.merchant.app.core.error.MerchantException;

/**
 * When this exception is thrown is must cause the application shutdown
 */
public final class MerchantFatalException extends MerchantException {

    /**
     * Constructor builds exception with error message.
     *
     * @param msg the error message.
     */
    public MerchantFatalException(String msg) {
        super(msg);
    }

    /**
     * Constructor builds exception from original throwable.
     *
     * @param e the original exception.
     */
    public MerchantFatalException(Throwable e) {
        super(e);
    }

    /**
     * Constructor builds exception with error message and original throwable.
     *
     * @param msg the error message.
     * @param e   the original exception.
     */
    public MerchantFatalException(String msg, Throwable e) {
        super(msg, e);
    }
}
