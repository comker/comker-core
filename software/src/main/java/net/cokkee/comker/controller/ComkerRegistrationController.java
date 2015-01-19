package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.Calendar;

import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.model.msg.ComkerInformationResponse;
import net.cokkee.comker.storage.ComkerRegistrationStorage;
import net.cokkee.comker.service.ComkerSessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Api(value = "comker_registration", description = "Registration API")
@Controller
@RequestMapping("/comker/registration")
public class ComkerRegistrationController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerRegistrationStorage registrationStorage = null;

    @Autowired(required = false)
    @Qualifier(value = "comkerRegistrationStorage")
    public void setRegistrationStorage(ComkerRegistrationStorage registrationStorage) {
        this.registrationStorage = registrationStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
        value = "Register a new Account",
        response = ComkerInformationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated email)")})
    @RequestMapping(method = RequestMethod.POST, 
            value = "/register", 
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerInformationResponse doRegistration(
            @ApiParam(value = "Registration form to be applied", required = true)
            @RequestBody ComkerRegistrationDTO form) {
        
        return registrationStorage.register(form);
    }
    
    @ApiOperation(
        value = "Confirm a registration",
        response = ComkerInformationResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Ok"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated email)")})
    @RequestMapping(method = RequestMethod.GET, value = "/confirm/{code}")
    public RedirectView confirm(
            @ApiParam(value = "Registration code that needs to be confirmed", required = true) 
            @PathVariable String code) {
        
        int result = registrationStorage.confirm(code);
        
        RedirectView redirectView = new RedirectView();
        
        switch(result) {
            case 0:
                redirectView.setUrl("index.html#/dashboard");
                break;
            case 1:
                redirectView.setUrl("index.html#/registrationfailed");
                break;
            default:
                redirectView.setUrl("index.html#/registrationfailed");
        }
        
        return redirectView;
    }
}
