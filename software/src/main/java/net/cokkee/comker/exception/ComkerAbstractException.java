package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractException extends RuntimeException {

    private int code;

    public ComkerAbstractException (int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
