package net.cokkee.comker.controller;

import javax.servlet.http.HttpServletRequest;
import net.cokkee.comker.exception.ComkerAbstractException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.exception.ComkerValidationFailedException;

import net.cokkee.comker.model.ComkerExceptionResponse;
import net.cokkee.comker.service.ComkerExceptionTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author drupalex
 */
@ControllerAdvice
public class ComkerExceptionHandler {

    private ComkerExceptionTransformer exceptionTransformer = null;

    @Autowired(required = false)
    public void setExceptionTransformer(ComkerExceptionTransformer exceptionTransformer) {
        this.exceptionTransformer = exceptionTransformer;
    }
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ComkerAbstractException.class)
    public @ResponseBody ComkerExceptionResponse handleAbstractException(
            HttpServletRequest req, Exception exception) {
        ComkerExceptionResponse resp = exceptionTransformer.transform(exception);
        resp.setUrl(req.getRequestURL().toString());
        return resp;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ComkerObjectNotFoundException.class)
    public @ResponseBody ComkerExceptionResponse handleObjectNotFoundException(
            HttpServletRequest req, Exception exception) {
        ComkerExceptionResponse resp = exceptionTransformer.transform(exception);
        resp.setUrl(req.getRequestURL().toString());
        return resp;
    }
    
    /**
     * NOT_ACCEPTABLE (406): The requested resource is only capable of 
     * generating content not acceptable according to the Accept headers 
     * sent in the request.
     * 
     * @author drupalex
     * @param req
     * @param exception
     * @return 
     */
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(ComkerValidationFailedException.class)
    public @ResponseBody ComkerExceptionResponse handleValidationFailedException(
            HttpServletRequest req, Exception exception) {
        ComkerExceptionResponse resp = exceptionTransformer.transform(exception);
        resp.setUrl(req.getRequestURL().toString());
        return resp;
    }
    
    
}
