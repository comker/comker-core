package net.cokkee.comker.exception;

import net.cokkee.comker.model.ComkerExceptionExtension;

/**
 * 
 * @author drupalex
 */
public class ComkerObjectNotFoundException extends ComkerEntityProcessingException {

    public static final int CODE = 401;

    public ComkerObjectNotFoundException (String msg) {
        super(CODE, msg);
    }

    public ComkerObjectNotFoundException (String msg, ComkerExceptionExtension extension) {
        super(CODE, msg, extension);
    }
}
