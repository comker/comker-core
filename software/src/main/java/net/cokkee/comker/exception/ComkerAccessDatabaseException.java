package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerAccessDatabaseException extends ComkerEntityProcessingException {

    public static final int CODE = 405;

    public ComkerAccessDatabaseException (String msg) {
        super(CODE, msg);
    }

    public ComkerAccessDatabaseException (ComkerExceptionExtension status) {
        super(CODE, null, status);
    }

    public ComkerAccessDatabaseException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerAccessDatabaseException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }

    @Deprecated
    public ComkerAccessDatabaseException (int code, String msg) {
        super(code, msg);
    }
}
