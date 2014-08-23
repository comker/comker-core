package net.cokkee.comker.model.error;

/**
 *
 * @author drupalex
 */
public class ComkerFieldError extends ComkerMessageResolvable {

    private String field;
    private Object rejectedValue;

    public ComkerFieldError() {
        super();
    }

    public ComkerFieldError(String field, String defaultMessage) {
        super();
        this.field = field;
        this.setDefaultMessage(defaultMessage);
    }

    public ComkerFieldError(String field, Object rejectedValue, 
            String code, Object[] arguments, String defaultMessage) {
        super(code, arguments, defaultMessage);
        this.field = field;
        this.rejectedValue = rejectedValue;
    }
}
