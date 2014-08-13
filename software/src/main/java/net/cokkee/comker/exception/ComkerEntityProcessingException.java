package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 *
 * @author drupalex
 */
public abstract class ComkerEntityProcessingException extends ComkerAbstractException {

    protected ComkerExceptionExtension extension;

    public ComkerEntityProcessingException (int code, String message) {
        super(code, message);
    }

    public ComkerEntityProcessingException (int code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public ComkerEntityProcessingException (int code, String message, ComkerExceptionExtension extension) {
        this(code, message);
        this.extension = extension;
    }

    public ComkerEntityProcessingException (int code, String message, Throwable cause, ComkerExceptionExtension extension) {
        this(code, message, cause);
        this.extension = extension;
    }

    public ComkerExceptionExtension getExtension() {
        return extension;
    }
}
