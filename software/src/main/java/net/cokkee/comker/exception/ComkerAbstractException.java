package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractException extends RuntimeException {

    private int code;
    private ComkerExceptionExtension extension;

    public ComkerAbstractException (int code, String message) {
        super(message);
        this.code = code;
    }

    public ComkerAbstractException (int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ComkerAbstractException (int code, String message, ComkerExceptionExtension status) {
        super();
        this.code = code;
        this.extension = status;
    }

    public ComkerAbstractException (int code, String message, ComkerExceptionExtension status, Throwable cause) {
        super(cause);
        this.code = code;
        this.extension = status;
    }

    public int getCode() {
        return code;
    }

    public ComkerExceptionExtension getExtension() {
        return extension;
    }
}
