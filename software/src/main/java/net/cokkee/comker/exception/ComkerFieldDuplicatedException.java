package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
@Deprecated
public class ComkerFieldDuplicatedException extends ComkerEntityProcessingException {

    public static final int CODE = 402;

    public ComkerFieldDuplicatedException (String msg) {
        super(CODE, msg);
    }

    public ComkerFieldDuplicatedException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerFieldDuplicatedException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerFieldDuplicatedException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
