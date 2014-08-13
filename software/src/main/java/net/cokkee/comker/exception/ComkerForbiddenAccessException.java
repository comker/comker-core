package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerForbiddenAccessException extends ComkerEntityProcessingException {

    public static final int CODE = 1403;

    public ComkerForbiddenAccessException (String msg) {
        super(CODE, msg);
    }

    public ComkerForbiddenAccessException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerForbiddenAccessException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerForbiddenAccessException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }

    @Deprecated
    public ComkerForbiddenAccessException (int code, String msg) {
        super(code, msg);
    }
}
