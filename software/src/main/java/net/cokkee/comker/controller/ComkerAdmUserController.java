package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "User", description = "Administration User API")
@Controller
@RequestMapping("/comker/adm/user")
public class ComkerAdmUserController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerUserStorage userStorage = null;

    @Autowired(required = false)
    @Qualifier(value = "comkerUserStorage")
    public void setUserStorage(ComkerUserStorage userStorage) {
        this.userStorage = userStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of users",
            notes = "Returns list of users",
            response = ComkerUserDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerUserDTO.Pack getUserList(
            @ApiParam(value = "The begin position to get Users", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_START, required=false) Integer start,
            @ApiParam(value = "How many Users do you want to get?", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_LIMIT, required=false) Integer limit,
            @ApiParam(value = "The query that user's name should be matched", required = false)
            @RequestParam(value=ComkerBaseConstant.QUERY_STRING, required=false) String q) {

        Integer total;
        List<ComkerUserDTO> collection;
        
        if (q == null) {
            total = userStorage.count();
            collection = userStorage.findAll(
                    sessionService.getPager(ComkerUserDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        } else {
            ComkerQuerySieve sieve = sessionService.getSieve(ComkerUserDTO.class)
                            .setCriterion("OR_EMAIL", q)
                            .setCriterion("OR_USERNAME", q)
                            .setCriterion("OR_FULLNAME", q);
            
            total = userStorage.count(sieve);
            collection = userStorage.findAll(sieve,
                    sessionService.getPager(ComkerUserDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
            for(ComkerUserDTO user:collection) {
                user.setPassword(null);
            }
        }
        
        return new ComkerUserDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a user by ID",
            notes = "Returns a user by ID (UUID)",
            response = ComkerUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerUserDTO getUser(
            @ApiParam(value = "ID of user that needs to be fetched", required = true) 
            @PathVariable String id) {
        ComkerUserDTO user = userStorage.get(id);
        user.setPassword(null);
        return user;
    }

    @ApiOperation(
            value = "Add a new user to the database",
            response = ComkerUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.POST, value = "", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerUserDTO createUser(
            @ApiParam(value = "User object that needs to be added to the store", required = true)
            @RequestBody ComkerUserDTO item) {
        return userStorage.get(userStorage.create(item));
    }
    
    @ApiOperation(
        value = "Update an existing user",
        response = ComkerUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerUserDTO updateUser(
            @ApiParam(value = "ID of user that needs to be updated", required = true)
            @PathVariable String id, 
            @RequestBody ComkerUserDTO item) {
        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }
        userStorage.update(item);
        return userStorage.get(id);
    }
    
    @ApiOperation(
            value = "Delete an existing user",
            response = ComkerUserDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 409, message = "Conflict on database updating")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerUserDTO deleteUser(
            @ApiParam(value = "ID of user that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerUserDTO item = userStorage.get(id);
        userStorage.delete(id);
        return item;
    }
}
