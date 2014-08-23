package net.cokkee.comker.model;

import java.io.Serializable;

/**
 *
 * @author drupalex
 */

public class ComkerExceptionExtension implements Serializable {
    
    public ComkerExceptionExtension() {
        super();
    }

    public ComkerExceptionExtension(String code, Object[] arguments, String defaultMessage) {
        this();
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

    public void setCode(String pattern) {
        this.code = pattern;
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
