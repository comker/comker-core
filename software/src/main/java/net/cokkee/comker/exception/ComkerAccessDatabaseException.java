package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerAccessDatabaseException extends ComkerAbstractException {

    public static final int CODE = 1405;

    public ComkerAccessDatabaseException (String msg) {
        super(CODE, msg);
    }

    public ComkerAccessDatabaseException (ComkerExceptionExtension status) {
        super(CODE, null, status);
    }

    public ComkerAccessDatabaseException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerAccessDatabaseException (String msg, ComkerExceptionExtension extension, Throwable cause) {
        super(CODE, msg, extension, cause);
    }

    @Deprecated
    public ComkerAccessDatabaseException (int code, String msg) {
        super(code, msg);
    }
}
