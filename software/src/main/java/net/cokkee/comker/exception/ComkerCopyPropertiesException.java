package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerCopyPropertiesException extends ComkerEntityProcessingException {

    public static final int CODE = 404;

    public ComkerCopyPropertiesException (String msg) {
        super(CODE, msg);
    }

    public ComkerCopyPropertiesException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerCopyPropertiesException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerCopyPropertiesException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
