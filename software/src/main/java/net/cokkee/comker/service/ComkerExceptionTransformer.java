package net.cokkee.comker.service;

import net.cokkee.comker.model.ComkerExceptionResponse;

/**
 *
 * @author drupalex
 */
public interface ComkerExceptionTransformer {
    public ComkerExceptionResponse transform(Exception e);
}
