package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * FORBIDDEN (403)
 * 
 * @author drupalex
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ComkerForbiddenAccessException extends ComkerEntityProcessingException {

    public static final int CODE = 1403;

    public ComkerForbiddenAccessException (String msg) {
        super(CODE, msg);
    }

    public ComkerForbiddenAccessException (String msg, Throwable cause) {
        super(CODE, msg, cause);
    }

    public ComkerForbiddenAccessException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerForbiddenAccessException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
