package net.cokkee.comker.model.error;

import java.util.List;
import net.cokkee.comker.model.error.ComkerEntityError;
import net.cokkee.comker.model.error.ComkerFieldError;

@javax.xml.bind.annotation.XmlRootElement
public class ComkerExceptionResponse {

    @Deprecated
    private int code;

    private String clazz;
    private String label;
    private String url;
    private String message;

    private ComkerEntityError entityError = null;
    private List<ComkerFieldError> fieldErrors = null;

    public ComkerExceptionResponse() {
    }

    public ComkerExceptionResponse(String message) {
        this.message = message;
    }
        
    public ComkerExceptionResponse(String clazz, String label) {
        this.clazz = clazz;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ComkerEntityError getEntityError() {
        return entityError;
    }

    public void setEntityError(ComkerEntityError extension) {
        this.entityError = extension;
    }

    public List<ComkerFieldError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ComkerFieldError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
