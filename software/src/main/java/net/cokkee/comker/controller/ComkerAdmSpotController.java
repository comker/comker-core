package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
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

@Api(value = "admspot", description = "Administration Spot API")
@Controller
@RequestMapping("/comker/adm/spot")
public class ComkerAdmSpotController {

    private static final Logger log = LoggerFactory.getLogger(ComkerAdmSpotController.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSpotStorage spotStorage = null;

    @Autowired(required = false)
    public void setSpotStorage(ComkerSpotStorage spotStorage) {
        this.spotStorage = spotStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of spots",
            notes = "Returns list of spots",
            response = ComkerSpotDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", produces="application/json")
    public @ResponseBody ComkerSpotDTO.Pack getSpotList(
            @ApiParam(value = "The begin position to get Spots", required = false)
            @RequestParam(value="start", required=false) Integer start,
            @ApiParam(value = "How many Spots do you want to get?", required = false)
            @RequestParam(value="limit", required=false) Integer limit,
            @ApiParam(value = "The query that spot's name should be matched", required = false)
            @RequestParam(value="q", required=false) String q) {

        Integer total = spotStorage.count();
        List collection = spotStorage.findAll(sessionService.getPager(ComkerSpotDTO.class, start, limit));
        return new ComkerSpotDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a spot by ID",
            notes = "Returns a spot by ID (UUID)",
            response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Spot not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerSpotDTO getSpot(
            @ApiParam(value = "ID of spot that needs to be fetched", required = true) 
            @PathVariable String id) {
        return spotStorage.get(id);
    }

    @ApiOperation(
            value = "Add a new spot to the database",
            response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.POST, value = "", 
            consumes = "application/json", 
            produces = "application/json")
    public @ResponseBody ComkerSpotDTO createSpot(
            @ApiParam(value = "Spot object that needs to be added to the store", required = true)
            @RequestBody ComkerSpotDTO item) {
        return spotStorage.create(item);
    }
    
    @ApiOperation(
        value = "Update an existing spot",
        response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Spot not found"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", 
            consumes = "application/json", 
            produces = "application/json")
    public @ResponseBody ComkerSpotDTO updateSpot(
            @ApiParam(value = "ID of spot that needs to be updated", required = true)
            @PathVariable String id, 
            @RequestBody ComkerSpotDTO item) {
        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }
        spotStorage.update(item);
        return spotStorage.get(id);
    }
    
    @ApiOperation(
            value = "Delete an existing spot",
            response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Spot not found"),
            @ApiResponse(code = 409, message = "Conflict on database updating")})
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerSpotDTO deleteSpot(
            @ApiParam(value = "ID of spot that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerSpotDTO item = spotStorage.get(id);
        spotStorage.delete(id);
        return item;
    }
}
