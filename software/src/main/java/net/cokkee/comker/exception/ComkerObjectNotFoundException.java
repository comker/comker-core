package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerObjectNotFoundException extends ComkerAbstractException {

    public static final int CODE = 401;

    public ComkerObjectNotFoundException (String msg) {
        super(CODE, msg);
    }

    public ComkerObjectNotFoundException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerObjectNotFoundException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerObjectNotFoundException (String msg, ComkerExceptionExtension extension, Throwable cause) {
        super(CODE, msg, extension, cause);
    }

    @Deprecated
    public ComkerObjectNotFoundException (int code, String msg) {
        super(code, msg);
    }
}
