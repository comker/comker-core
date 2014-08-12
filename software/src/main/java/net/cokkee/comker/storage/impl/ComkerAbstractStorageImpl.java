package net.cokkee.comker.storage.impl;

import net.cokkee.comker.exception.ComkerValidationFailedException;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author drupalex
 */
public abstract class ComkerAbstractStorageImpl {

    protected void invokeValidator(Validator validator, Object entity) {
        //if (validator == null || entity == null) return;
        ComkerValidationFailedException errors =
                new ComkerValidationFailedException(entity, entity.getClass().getName());
        ValidationUtils.invokeValidator(validator, entity, errors);
        if (errors.hasErrors()) throw errors;
    }
}
