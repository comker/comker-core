package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(value = "comker_adm_permission", description = "Administration Permission API")
@Controller
@RequestMapping("/comker/adm/permission")
public class ComkerAdmPermissionController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerPermissionStorage permissionStorage = null;

    @Autowired(required = false)
    @Qualifier(value = "comkerPermissionStorage")
    public void setPermissionStorage(ComkerPermissionStorage permissionStorage) {
        this.permissionStorage = permissionStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of permissions",
            notes = "Returns list of permissions",
            response = ComkerPermissionDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", produces="application/json")
    public @ResponseBody ComkerPermissionDTO.Pack getPermissionList(
            @ApiParam(value = "The begin position to get Permissions", required = false)
            @RequestParam(value="start", required=false) Integer start,
            @ApiParam(value = "How many Permissions do you want to get?", required = false)
            @RequestParam(value="limit", required=false) Integer limit,
            @ApiParam(value = "The query that permission's name should be matched", required = false)
            @RequestParam(value="q", required=false) String q) {

        Integer total;
        List collection;
        
        if (q == null) {
            total = permissionStorage.count();
            collection = permissionStorage.findAll(
                    sessionService.getPager(ComkerPermissionDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        } else {
            ComkerQuerySieve sieve = sessionService.getSieve(ComkerPermissionDTO.class)
                            .setCriterion("OR_AUTHORITY", q);
            
            total = permissionStorage.count(sieve);
            collection = permissionStorage.findAll(sieve,
                    sessionService.getPager(ComkerPermissionDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        }
        
        return new ComkerPermissionDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a permission by ID",
            notes = "Returns a permission by ID (UUID)",
            response = ComkerPermissionDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Permission not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerPermissionDTO getPermission(
            @ApiParam(value = "ID of permission that needs to be fetched", required = true) 
            @PathVariable String id) {
        return permissionStorage.get(id);
    }
}
