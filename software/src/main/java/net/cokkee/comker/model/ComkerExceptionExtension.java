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

    public ComkerExceptionExtension(String pattern, Object[] arguments) {
        this();
        this.name = pattern;
        this.pattern = pattern;
        this.arguments = arguments;
    }

    private String name;
    private String pattern;
    private Object[] arguments;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public void setArguments(Object[] arguments) {
        this.arguments = arguments;
    }
}
