package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public class ComkerMailParsingException extends ComkerEntityProcessingException {

    public static final int CODE = 405;

    public ComkerMailParsingException (String msg) {
        super(CODE, msg);
    }

    public ComkerMailParsingException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerMailParsingException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
