package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerFieldDuplicatedException extends ComkerAbstractException {

    public static final int CODE = 1402;

    public ComkerFieldDuplicatedException (String msg) {
        super(CODE, msg);
    }

    public ComkerFieldDuplicatedException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerFieldDuplicatedException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerFieldDuplicatedException (String msg, ComkerExceptionExtension extension, Throwable cause) {
        super(CODE, msg, extension, cause);
    }
    
    @Deprecated
    public ComkerFieldDuplicatedException (int code, String msg) {
        super(code, msg);
    }
}
