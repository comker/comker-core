package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerObjectDuplicatedException extends ComkerAbstractException {

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

    public ComkerObjectDuplicatedException (String msg, ComkerExceptionExtension extension, Throwable cause) {
        super(CODE, msg, extension, cause);
    }

    @Deprecated
    public ComkerObjectDuplicatedException (int code, String msg) {
        super(code, msg);
    }
}
