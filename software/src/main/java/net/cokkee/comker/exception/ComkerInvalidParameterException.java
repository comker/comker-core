package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerInvalidParameterException extends ComkerEntityProcessingException {

    public static final int CODE = 406;
    
    public ComkerInvalidParameterException (String msg) {
        super(CODE, msg);
    }

    public ComkerInvalidParameterException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerInvalidParameterException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerInvalidParameterException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }

    @Deprecated
    public ComkerInvalidParameterException (int code, String msg) {
        super(code, msg);
    }
}
