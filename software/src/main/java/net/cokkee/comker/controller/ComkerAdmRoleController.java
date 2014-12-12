package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "admrole", description = "Administration Role API")
@Controller
@RequestMapping("/comker/adm/role")
public class ComkerAdmRoleController {

    private static final Logger log = LoggerFactory.getLogger(ComkerAdmRoleController.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerRoleStorage roleStorage = null;

    @Autowired(required = false)
    public void setRoleStorage(ComkerRoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of roles",
            notes = "Returns list of roles",
            response = ComkerRoleDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", produces="application/json")
    public @ResponseBody ComkerRoleDTO.Pack getRoleList(
            @ApiParam(value = "The begin position to get Roles", required = false)
            @RequestParam(value="start", required=false) Integer start,
            @ApiParam(value = "How many Roles do you want to get?", required = false)
            @RequestParam(value="limit", required=false) Integer limit,
            @ApiParam(value = "The query that role's name should be matched", required = false)
            @RequestParam(value="q", required=false) String q) {

        Integer total = roleStorage.count();
        List collection = roleStorage.findAll(sessionService.getPager(ComkerRoleDTO.class, start, limit));
        return new ComkerRoleDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a role by ID",
            notes = "Returns a role by ID (UUID)",
            response = ComkerRoleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Role not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces="application/json")
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
    @RequestMapping(method = RequestMethod.POST, value = "", 
            consumes = "application/json", 
            produces = "application/json")
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
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", 
            consumes = "application/json", 
            produces = "application/json")
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
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerRoleDTO deleteRole(
            @ApiParam(value = "ID of role that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerRoleDTO item = roleStorage.get(id);
        roleStorage.delete(id);
        return item;
    }
}