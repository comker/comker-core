package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerInvalidParameterException extends ComkerAbstractException {

    public static final int CODE = 1406;
    
    public ComkerInvalidParameterException (String msg) {
        super(CODE, msg);
    }

    public ComkerInvalidParameterException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerInvalidParameterException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerInvalidParameterException (String msg, ComkerExceptionExtension extension, Throwable cause) {
        super(CODE, msg, extension, cause);
    }

    @Deprecated
    public ComkerInvalidParameterException (int code, String msg) {
        super(code, msg);
    }
}
