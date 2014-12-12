package net.cokkee.comker.model.error;

import java.io.Serializable;

/**
 *
 * @author drupalex
 */
public class ComkerResolvableMessage implements Serializable {

    public ComkerResolvableMessage() {
    }

    public ComkerResolvableMessage(String code, Object[] arguments, String defaultMessage) {
        this.code = code;
        this.arguments = arguments;
        this.defaultMessage = defaultMessage;
    }

    private String code;
    private Object[] arguments;
    private String defaultMessage;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}
