package net.cokkee.comker.model;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.exception.ComkerEntityProcessingException;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.model.error.ComkerEntityError;
import net.cokkee.comker.model.error.ComkerFieldError;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.FieldError;

@javax.xml.bind.annotation.XmlRootElement
public class ComkerExceptionResponse {

    public static final int ERROR = 1;
    public static final int WARNING = 2;
    public static final int INFO = 3;
    public static final int OK = 4;
    public static final int TOO_BUSY = 5;

    public static final String WEBAPP_ERRORS = "WEBAPP_ERRORS";
    public static final String SYSTEM_ERRORS = "SYSTEM_ERRORS";
    public static final String COMKER_ERRORS = "COMKER_ERRORS";

    private int code;
    private String type;
    private String name;
    private String message;

    private ComkerEntityError entityError = null;
    private List<ComkerFieldError> fieldErrors = null;

    public ComkerExceptionResponse() {
    }

    public ComkerExceptionResponse(int code, String message) {
        this.code = code;
        if (code == 500) {
            this.type = SYSTEM_ERRORS;
        } else if (code < 500) {
            this.type = WEBAPP_ERRORS;
        } else if (code > 500) {
            this.type = COMKER_ERRORS;
        }
        this.message = message;
    }

    public ComkerExceptionResponse(ComkerEntityProcessingException exception,
            String message) {
        this((exception != null)?exception.getCode():500, message);
        if (exception != null && exception.getExtension() != null) {
            this.entityError = new ComkerEntityError();
            ComkerDataUtil.copyProperties(exception.getExtension(), this.entityError);
        }
    }

    public ComkerExceptionResponse(ComkerValidationFailedException exception,
            String message) {
        this((exception != null)?exception.getCode():500, message);
        if (exception != null && exception.getFieldErrorCount()>0) {
            this.fieldErrors = new ArrayList<ComkerFieldError>();
            List<FieldError> errors = exception.getFieldErrors();
            for(FieldError error:errors) {
                Object[] args = null;
                if (error.getArguments()!=null) {
                    args = new Object[error.getArguments().length];
                    for(int i=0; i<args.length; i++) {
                        args[i] = ComkerDataUtil.convertObjectToString(error.getArguments()[i]);
                    }
                }
                ComkerFieldError comkerError = new ComkerFieldError(error.getField(),
                        error.getRejectedValue(), error.getCode(), args, error.getDefaultMessage());
                //ComkerDataUtil.copyPropertiesExcludes(error, comkerError, "arguments");
                this.fieldErrors.add(comkerError);
            }
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
