package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public class ComkerObjectDuplicatedException extends ComkerAbstractException {

    public ComkerObjectDuplicatedException (int code, String msg) {
        super(code, msg);
    }
}
