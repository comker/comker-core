package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractException extends RuntimeException {

    protected int code;

    public ComkerAbstractException (int code) {
        super();
        this.code = code;
    }

    public ComkerAbstractException (int code, String message) {
        super(message);
        this.code = code;
    }

    public ComkerAbstractException (int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
