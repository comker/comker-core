package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public class ComkerObjectNotFoundException extends ComkerAbstractException {

    public ComkerObjectNotFoundException (int code, String msg) {
        super(code, msg);
    }
}
