package net.cokkee.comker.exception;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;

import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author drupalex
 */
public class ComkerValidationFailedException extends ComkerAbstractException
        implements BindingResult {

    public static final int CODE = 410;

    private final BindingResult bindingResult;

    /**
     * Create a new NutrixValidationFailedException instance for a
     * BindingResult.
     *
     * @param bindingResult the BindingResult instance to wrap
     */
    public ComkerValidationFailedException(BindingResult bindingResult) {
        super(CODE);
        Assert.notNull(bindingResult, "BindingResult must not be null");
        this.bindingResult = bindingResult;
    }

    /**
     * Create a new NutrixValidationFailedException instance for a target bean.
     *
     * @param target target bean to bind onto
     * @param objectName the name of the target object
     * @see BeanPropertyBindingResult
     */
    public ComkerValidationFailedException(Object target, String objectName) {
        super(CODE);
        Assert.notNull(target, "Target object must not be null");
        this.bindingResult = new BeanPropertyBindingResult(target, objectName);
    }

    /**
     * Return the BindingResult that this ComkerValidationFailedException wraps.
     * Will typically be a BeanPropertyBindingResult.
     *
     * @see BeanPropertyBindingResult
     */
    public final BindingResult getBindingResult() {
        return this.bindingResult;
    }

    @Override
    public String getObjectName() {
        return this.bindingResult.getObjectName();
    }

    @Override
    public void setNestedPath(String nestedPath) {
        this.bindingResult.setNestedPath(nestedPath);
    }

    @Override
    public String getNestedPath() {
        return this.bindingResult.getNestedPath();
    }

    @Override
    public void pushNestedPath(String subPath) {
        this.bindingResult.pushNestedPath(subPath);
    }

    @Override
    public void popNestedPath() throws IllegalStateException {
        this.bindingResult.popNestedPath();
    }

    @Override
    public void reject(String errorCode) {
        this.bindingResult.reject(errorCode);
    }

    @Override
    public void reject(String errorCode, String defaultMessage) {
        this.bindingResult.reject(errorCode, defaultMessage);
    }

    @Override
    public void reject(String errorCode, Object[] errorArgs, String defaultMessage) {
        this.bindingResult.reject(errorCode, errorArgs, defaultMessage);
    }

    @Override
    public void rejectValue(String field, String errorCode) {
        this.bindingResult.rejectValue(field, errorCode);
    }

    @Override
    public void rejectValue(String field, String errorCode, String defaultMessage) {
        this.bindingResult.rejectValue(field, errorCode, defaultMessage);
    }

    @Override
    public void rejectValue(String field, String errorCode, Object[] errorArgs, String defaultMessage) {
        this.bindingResult.rejectValue(field, errorCode, errorArgs, defaultMessage);
    }

    @Override
    public void addAllErrors(Errors errors) {
        this.bindingResult.addAllErrors(errors);
    }

    @Override
    public boolean hasErrors() {
        return this.bindingResult.hasErrors();
    }

    @Override
    public int getErrorCount() {
        return this.bindingResult.getErrorCount();
    }

    @Override
    public List getAllErrors() {
        return this.bindingResult.getAllErrors();
    }

    @Override
    public boolean hasGlobalErrors() {
        return this.bindingResult.hasGlobalErrors();
    }

    @Override
    public int getGlobalErrorCount() {
        return this.bindingResult.getGlobalErrorCount();
    }

    @Override
    public List getGlobalErrors() {
        return this.bindingResult.getGlobalErrors();
    }

    @Override
    public ObjectError getGlobalError() {
        return this.bindingResult.getGlobalError();
    }

    @Override
    public boolean hasFieldErrors() {
        return this.bindingResult.hasFieldErrors();
    }

    @Override
    public int getFieldErrorCount() {
        return this.bindingResult.getFieldErrorCount();
    }

    @Override
    public List getFieldErrors() {
        return this.bindingResult.getFieldErrors();
    }

    @Override
    public FieldError getFieldError() {
        return this.bindingResult.getFieldError();
    }

    @Override
    public boolean hasFieldErrors(String field) {
        return this.bindingResult.hasFieldErrors(field);
    }

    @Override
    public int getFieldErrorCount(String field) {
        return this.bindingResult.getFieldErrorCount(field);
    }

    @Override
    public List getFieldErrors(String field) {
        return this.bindingResult.getFieldErrors(field);
    }

    @Override
    public FieldError getFieldError(String field) {
        return this.bindingResult.getFieldError(field);
    }

    @Override
    public Object getFieldValue(String field) {
        return this.bindingResult.getFieldValue(field);
    }

    @Override
    public Class getFieldType(String field) {
        return this.bindingResult.getFieldType(field);
    }

    @Override
    public Object getTarget() {
        return this.bindingResult.getTarget();
    }

    @Override
    public Map getModel() {
        return this.bindingResult.getModel();
    }

    @Override
    public Object getRawFieldValue(String field) {
        return this.bindingResult.getRawFieldValue(field);
    }

    @Override
    public PropertyEditor findEditor(String field, Class valueType) {
        return this.bindingResult.findEditor(field, valueType);
    }

    @Override
    public PropertyEditorRegistry getPropertyEditorRegistry() {
        return this.bindingResult.getPropertyEditorRegistry();
    }

    @Override
    public void addError(ObjectError error) {
        this.bindingResult.addError(error);
    }

    @Override
    public String[] resolveMessageCodes(String errorCode, String field) {
        return this.bindingResult.resolveMessageCodes(errorCode, field);
    }

    @Override
    public void recordSuppressedField(String field) {
        this.bindingResult.recordSuppressedField(field);
    }

    @Override
    public String[] getSuppressedFields() {
        return this.bindingResult.getSuppressedFields();
    }

    /**
     * Returns diagnostic information about the errors held in this object.
     */
    @Override
    public String getMessage() {
        return this.bindingResult.toString();
    }

    @Override
    public boolean equals(Object other) {
        return (this == other || this.bindingResult.equals(other));
    }

    @Override
    public int hashCode() {
        return this.bindingResult.hashCode();
    }

    @Override
    public String[] resolveMessageCodes(String string) {
        return this.bindingResult.resolveMessageCodes(string);
    }
}
