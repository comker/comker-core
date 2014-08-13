package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerIllegalAccessException extends ComkerEntityProcessingException {

    public static final int CODE = 404;

    public ComkerIllegalAccessException (String msg) {
        super(CODE, msg);
    }

    public ComkerIllegalAccessException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerIllegalAccessException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerIllegalAccessException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }

    @Deprecated
    public ComkerIllegalAccessException (int code, String msg) {
        super(code, msg);
    }
}
