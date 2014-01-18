package net.cokkee.comker.api;

import net.cokkee.comker.exception.ComkerAbstractException;
import net.cokkee.comker.model.ComkerExceptionResponse;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

@Provider
public class ComkerExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        if (exception instanceof javax.ws.rs.WebApplicationException) {
            javax.ws.rs.WebApplicationException e = (javax.ws.rs.WebApplicationException) exception;
            return Response
                    .status(e.getResponse().getStatus())
                    .entity(new ComkerExceptionResponse(ComkerExceptionResponse.ERROR, e.getMessage()))
                    .type("application/json").build();
        } else if (exception instanceof com.fasterxml.jackson.core.JsonParseException) {
            return Response
                    .status(400)
                    .entity(new ComkerExceptionResponse(ComkerExceptionResponse.ERROR, "bad input")).build();
        } else if (exception instanceof ComkerAbstractException) {
            ComkerAbstractException e = (ComkerAbstractException) exception;
            return Response
                    .status(e.getCode())
                    .entity(new ComkerExceptionResponse(ComkerExceptionResponse.ERROR, e.getMessage()))
                    .type("application/json").build();
        } else {
            return Response
                    .status(500)
                    .entity(new ComkerExceptionResponse(ComkerExceptionResponse.ERROR, "something bad happened"))
                    .type("application/json").build();
        }
    }
}