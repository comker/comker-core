package net.cokkee.comker.controller;

import java.text.MessageFormat;
import java.util.UUID;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comker/exception")
public class ComkerExceptionRaiser {

    @RequestMapping(method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE,
            value = "/notfound")
    public @ResponseBody ComkerSessionInfo throwComkerObjectNotFoundException() {
        String id = UUID.randomUUID().toString();
        throw new ComkerObjectNotFoundException(
                "role_with__id__not_found",
                new ComkerExceptionExtension("error.role_with__id__not_found", 
                        new Object[] {id}, 
                        MessageFormat.format("Role object with id:{0} not found", new Object[] {id})));
    }
    
    @RequestMapping(method = RequestMethod.GET, 
            produces = MediaType.APPLICATION_JSON_VALUE, 
            value = "/validationfailed")
    public @ResponseBody ComkerSessionInfo throwComkerValidationFailedException() {
        
        ComkerRoleDTO entity = new ComkerRoleDTO();
        
        ComkerValidationFailedException errors =
                new ComkerValidationFailedException(entity, entity.getClass().getName());
        
        errors.rejectValue("code",
                    "msg.__field__should_be_not_null",
                    new Object[] {"msg.field_code"},
                    "Code value should not be null");
        
        throw errors;
    }
}
