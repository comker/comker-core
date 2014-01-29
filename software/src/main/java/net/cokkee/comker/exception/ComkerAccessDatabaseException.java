package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public class ComkerAccessDatabaseException extends ComkerAbstractException {

    public ComkerAccessDatabaseException (int code, String msg) {
        super(code, msg);
    }
}
