package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
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

@Api(value = "Role", description = "Administration Role API")
@Controller
@RequestMapping("/comker/adm")
public class ComkerAdmRoleController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerRoleStorage roleStorage = null;

    @Autowired(required = false)
    @Qualifier(value = "comkerRoleStorage")
    public void setRoleStorage(ComkerRoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of global roles",
            notes = "Returns list of global roles",
            response = ComkerRoleDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "/globalroles", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO.Pack getGlobalRoleList(
            @ApiParam(value = "The begin position to get Global Roles", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_START, required=false) Integer start,
            @ApiParam(value = "How many Global Roles do you want to get?", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_LIMIT, required=false) Integer limit,
            @ApiParam(value = "The query that role's name should be matched", required = false)
            @RequestParam(value=ComkerBaseConstant.QUERY_STRING, required=false) String q) {

        Integer total;
        List collection;
        
        ComkerQuerySieve sieve = (new ComkerQuerySieve()).setCriterion("global", Boolean.TRUE);
        
        if (q != null) {
            sieve.setCriterion("OR_CODE", q).setCriterion("OR_NAME", q);
        }
        
        total = roleStorage.count(sieve);
        collection = roleStorage.findAll(sieve,
                sessionService.getPager(ComkerRoleDTO.class)
                        .updateStart(start)
                        .updateLimit(limit));
        
        return new ComkerRoleDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "List all of scoped roles",
            notes = "Returns list of scoped roles",
            response = ComkerRoleDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "/scopedroles", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO.Pack getScopedRoleList(
            @ApiParam(value = "The begin position to get Global Roles", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_START, required=false) Integer start,
            @ApiParam(value = "How many Global Roles do you want to get?", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_LIMIT, required=false) Integer limit,
            @ApiParam(value = "The query that role's name should be matched", required = false)
            @RequestParam(value=ComkerBaseConstant.QUERY_STRING, required=false) String q) {

        Integer total;
        List collection;
        
        ComkerQuerySieve sieve = (new ComkerQuerySieve()).setCriterion("global", Boolean.FALSE);
        
        if (q != null) {
            sieve.setCriterion("OR_CODE", q).setCriterion("OR_NAME", q);
        }
        
        total = roleStorage.count(sieve);
        collection = roleStorage.findAll(sieve,
                sessionService.getPager(ComkerRoleDTO.class)
                        .updateStart(start)
                        .updateLimit(limit));
        
        return new ComkerRoleDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "List all of roles",
            notes = "Returns list of roles",
            response = ComkerRoleDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "/role", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO.Pack getRoleList(
            @ApiParam(value = "The begin position to get Roles", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_START, required=false) Integer start,
            @ApiParam(value = "How many Roles do you want to get?", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_LIMIT, required=false) Integer limit,
            @ApiParam(value = "The query that role's name should be matched", required = false)
            @RequestParam(value=ComkerBaseConstant.QUERY_STRING, required=false) String q) {

        Integer total;
        List collection;
        
        if (q == null) {
            total = roleStorage.count();
            collection = roleStorage.findAll(
                    sessionService.getPager(ComkerRoleDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        } else {
            ComkerQuerySieve sieve = sessionService.getSieve(ComkerRoleDTO.class)
                            .setCriterion("OR_CODE", q)
                            .setCriterion("OR_NAME", q);
            
            total = roleStorage.count(sieve);
            collection = roleStorage.findAll(sieve,
                    sessionService.getPager(ComkerRoleDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        }
        
        return new ComkerRoleDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a role by ID",
            notes = "Returns a role by ID (UUID)",
            response = ComkerRoleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Role not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/role/{id}", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO getRole(
            @ApiParam(value = "ID of role that needs to be fetched", required = true) 
            @PathVariable String id) {
        return roleStorage.get(id);
    }

    @ApiOperation(
            value = "Add a new role to the database",
            response = ComkerRoleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.POST, value = "/role", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO createRole(
            @ApiParam(value = "Role object that needs to be added to the store", required = true)
            @RequestBody ComkerRoleDTO item) {
        return roleStorage.create(item);
    }
    
    @ApiOperation(
        value = "Update an existing role",
        response = ComkerRoleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Role not found"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.PUT, value = "/role/{id}", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO updateRole(
            @ApiParam(value = "ID of role that needs to be updated", required = true)
            @PathVariable String id, 
            @RequestBody ComkerRoleDTO item) {
        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }
        roleStorage.update(item);
        return roleStorage.get(id);
    }
    
    @ApiOperation(
            value = "Delete an existing role",
            response = ComkerRoleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Role not found"),
            @ApiResponse(code = 409, message = "Conflict on database updating")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/role/{id}", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerRoleDTO deleteRole(
            @ApiParam(value = "ID of role that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerRoleDTO item = roleStorage.get(id);
        roleStorage.delete(id);
        return item;
    }
}
