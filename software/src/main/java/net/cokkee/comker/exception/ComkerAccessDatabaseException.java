package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author drupalex
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class ComkerAccessDatabaseException extends ComkerEntityProcessingException {

    public static final int CODE = 405;

    public ComkerAccessDatabaseException (String msg) {
        super(CODE, msg);
    }

    public ComkerAccessDatabaseException (ComkerExceptionExtension status) {
        super(CODE, null, status);
    }

    public ComkerAccessDatabaseException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }

    public ComkerAccessDatabaseException (String msg, Throwable cause, ComkerExceptionExtension extension) {
        super(CODE, msg, cause, extension);
    }
}
