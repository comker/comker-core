package net.cokkee.comker.exception;

/**
 *
 * @author drupalex
 */
public class ComkerFieldDuplicatedException extends ComkerAbstractException {

    public ComkerFieldDuplicatedException (int code, String msg) {
        super(code, msg);
    }
}
