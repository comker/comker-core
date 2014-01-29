package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public class ComkerInvalidParameterException extends ComkerAbstractException {

    public ComkerInvalidParameterException (int code, String msg) {
        super(code, msg);
    }
}
