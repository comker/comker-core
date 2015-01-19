package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerMailSendingException extends ComkerEntityProcessingException {

    public static final int CODE = 405;

    public ComkerMailSendingException (String msg) {
        super(CODE, msg);
    }

    public ComkerMailSendingException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerMailSendingException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
