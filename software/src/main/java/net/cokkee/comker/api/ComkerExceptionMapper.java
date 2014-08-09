package net.cokkee.comker.api;

import java.text.MessageFormat;
import net.cokkee.comker.exception.ComkerAbstractException;
import net.cokkee.comker.model.ComkerExceptionResponse;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.ComkerExceptionExtension;
import net.cokkee.comker.service.ComkerLocaleMessageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class ComkerExceptionMapper implements ExceptionMapper<Exception> {

    private static Logger log = LoggerFactory.getLogger(ComkerExceptionMapper.class);

    private ComkerLocaleMessageService localeMessageService = null;

    public void setLocaleMessageService(ComkerLocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    @Override
    public Response toResponse(Exception exception) {
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "Exception catched by ComkerExceptionMapper: {0}. Message: {1}",
                    new Object[] {exception.getClass().getName(), exception.getMessage()}));
            exception.printStackTrace();
        }

        if (exception instanceof javax.ws.rs.WebApplicationException) {
            javax.ws.rs.WebApplicationException e = (javax.ws.rs.WebApplicationException) exception;
            return Response
                    .status(e.getResponse().getStatus())
                    .entity(new ComkerExceptionResponse(e.getResponse().getStatus(), e.getMessage()))
                    .type("application/json").build();
        } else if (exception instanceof ComkerAbstractException) {
            ComkerAbstractException e = (ComkerAbstractException) exception;
            ComkerExceptionExtension ext = e.getExtension();
            
            String msgName = (ext != null) ? ext.getName() : null;
            String message = (ext != null) ? localeMessageService.getMessage(ext.getPattern(),
                        ext.getArguments(), e.getMessage()) : e.getMessage();
            
            return Response
                    .status(e.getCode())
                    .entity(new ComkerExceptionResponse(e.getCode(), message, msgName))
                    .type("application/json").build();
        } else {
            return Response
                    .status(500)
                    .entity(new ComkerExceptionResponse(500, exception.getMessage()))
                    .type("application/json").build();
        }
    }
}