package net.cokkee.comker.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.exception.ComkerEntityProcessingException;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.model.error.ComkerExceptionResponse;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.error.ComkerFieldError;
import net.cokkee.comker.service.ComkerExceptionTransformer;
import net.cokkee.comker.service.ComkerLocalizationService;
import net.cokkee.comker.util.ComkerDataUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.FieldError;

public class ComkerExceptionTransformerImpl implements ComkerExceptionTransformer {

    private static final Logger log = LoggerFactory.getLogger(ComkerExceptionTransformerImpl.class);

    private ComkerLocalizationService localizationService = null;

    public void setLocalizationService(ComkerLocalizationService localizationService) {
        this.localizationService = localizationService;
    }

    @Override
    public ComkerExceptionResponse transform(Exception e) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "Exception catched by ComkerExceptionTransformer: {0}. Message: {1}",
                    new Object[] {e.getClass().getName(), e.getMessage()}));
        }
        
        ComkerExceptionResponse resp = new ComkerExceptionResponse(
                e.getClass().getSimpleName(), e.getMessage());

        if (e instanceof ComkerEntityProcessingException) {
            ComkerEntityProcessingException exception = (ComkerEntityProcessingException) e;
            ComkerExceptionExtension ext = exception.getExtension();
            
            if (ext != null) {
                resp.setMessage(localizationService.getMessage(
                        ext.getCode(), ext.getArguments(), ext.getDefaultMessage()));
            }
            
            return resp;
        } 
        
        if (e instanceof ComkerValidationFailedException) {
            ComkerValidationFailedException exception = (ComkerValidationFailedException) e;

            if (exception.getFieldErrorCount()>0) {
                resp.setFieldErrors(new ArrayList<ComkerFieldError>());
                List<FieldError> errors = exception.getFieldErrors();
                for(FieldError error:errors) {
                    Object[] args = null;
                    if (error.getArguments()!=null) {
                        args = new Object[error.getArguments().length];
                        for(int i=0; i<args.length; i++) {
                            args[i] = ComkerDataUtil.convertObjectToString(error.getArguments()[i]);
                        }
                    }
                    ComkerFieldError comkerError = new ComkerFieldError(
                            error.getField(),
                            error.getRejectedValue(), 
                            error.getCode(), args, 
                            error.getDefaultMessage());
                    //ComkerDataUtil.copyPropertiesExcludes(error, comkerError, "arguments");
                    resp.getFieldErrors().add(comkerError);
                }
            }
            return resp;
        } 

        return resp;
    }
}