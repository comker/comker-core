package net.cokkee.comker.exception;

import net.cokkee.comker.model.error.ComkerExceptionExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NOT_ACCEPTABLE (406): The requested resource is only capable of 
 * generating content not acceptable according to the Accept headers 
 * sent in the request.
 * 
 * @author drupalex
 */
@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class ComkerInvalidParameterException extends ComkerEntityProcessingException {

    public static final int CODE = 406;
    
    public ComkerInvalidParameterException (String msg) {
        super(CODE, msg);
    }

    public ComkerInvalidParameterException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }
}
