package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
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

@Api(value = "admcrew", description = "Administration Crew API")
@Controller
@RequestMapping("/comker/adm/crew")
public class ComkerAdmCrewController {

    private static final Logger log = LoggerFactory.getLogger(ComkerAdmCrewController.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerCrewStorage crewStorage = null;

    @Autowired(required = false)
    public void setCrewStorage(ComkerCrewStorage crewStorage) {
        this.crewStorage = crewStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of crews",
            notes = "Returns list of crews",
            response = ComkerCrewDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", produces="application/json")
    public @ResponseBody ComkerCrewDTO.Pack getCrewList(
            @ApiParam(value = "The begin position to get Crews", required = false)
            @RequestParam(value="start", required=false) Integer start,
            @ApiParam(value = "How many Crews do you want to get?", required = false)
            @RequestParam(value="limit", required=false) Integer limit,
            @ApiParam(value = "The query that crew's name should be matched", required = false)
            @RequestParam(value="q", required=false) String q) {

        Integer total = crewStorage.count();
        List collection = crewStorage.findAll(sessionService.getPager(ComkerCrewDTO.class, start, limit));
        return new ComkerCrewDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a crew by ID",
            notes = "Returns a crew by ID (UUID)",
            response = ComkerCrewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Crew not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerCrewDTO getCrew(
            @ApiParam(value = "ID of crew that needs to be fetched", required = true) 
            @PathVariable String id) {
        return crewStorage.get(id);
    }

    @ApiOperation(
            value = "Add a new crew to the database",
            response = ComkerCrewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.POST, value = "", 
            consumes = "application/json", 
            produces = "application/json")
    public @ResponseBody ComkerCrewDTO createCrew(
            @ApiParam(value = "Crew object that needs to be added to the store", required = true)
            @RequestBody ComkerCrewDTO item) {
        return crewStorage.create(item);
    }
    
    @ApiOperation(
        value = "Update an existing crew",
        response = ComkerCrewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Crew not found"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", 
            consumes = "application/json", 
            produces = "application/json")
    public @ResponseBody ComkerCrewDTO updateCrew(
            @ApiParam(value = "ID of crew that needs to be updated", required = true)
            @PathVariable String id, 
            @RequestBody ComkerCrewDTO item) {
        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }
        crewStorage.update(item);
        return crewStorage.get(id);
    }
    
    @ApiOperation(
            value = "Delete an existing crew",
            response = ComkerCrewDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Crew not found"),
            @ApiResponse(code = 409, message = "Conflict on database updating")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerCrewDTO deleteCrew(
            @ApiParam(value = "ID of crew that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerCrewDTO item = crewStorage.get(id);
        crewStorage.delete(id);
        return item;
    }
}
