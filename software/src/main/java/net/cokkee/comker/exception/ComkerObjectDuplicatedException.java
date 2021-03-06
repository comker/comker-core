package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
@Deprecated
public class ComkerObjectDuplicatedException extends ComkerEntityProcessingException {

    public static final int CODE = 1409;

    public ComkerObjectDuplicatedException (String msg) {
        super(CODE, msg);
    }

    public ComkerObjectDuplicatedException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerObjectDuplicatedException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerObjectDuplicatedException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
