package net.cokkee.comker.api;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;
import javax.ws.rs.WebApplicationException;

import net.cokkee.comker.exception.ComkerAbstractException;
import net.cokkee.comker.model.error.ComkerExceptionResponse;
import net.cokkee.comker.service.ComkerExceptionTransformer;

@Provider
public class ComkerExceptionMapper implements ExceptionMapper<Exception> {

    private ComkerExceptionTransformer exceptionTransformer = null;

    public void setExceptionTransformer(ComkerExceptionTransformer exceptionTransformer) {
        this.exceptionTransformer = exceptionTransformer;
    }

    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof WebApplicationException) {
            WebApplicationException e = (WebApplicationException) exception;
            return Response
                    .status(e.getResponse().getStatus())
                    .entity(new ComkerExceptionResponse(
                            exception.getClass().getSimpleName(), 
                            exception.getMessage()))
                    .type("application/json").build();
        } else if (exception instanceof ComkerAbstractException) {
            return Response
                    .status(400)
                    .entity(exceptionTransformer.transform(exception))
                    .type("application/json").build();
        } else {
            return Response
                    .status(500)
                    .entity(new ComkerExceptionResponse(
                            exception.getClass().getSimpleName(), 
                            exception.getMessage()))
                    .type("application/json").build();
        }
    }
}