package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
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

@Api(value = "Site", description = "Administration Site API")
@Controller
@RequestMapping("/comker/adm/spot")
public class ComkerAdmSpotController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSpotStorage spotStorage = null;

    @Autowired(required = false)
    @Qualifier(value = "comkerSpotStorage")
    public void setSpotStorage(ComkerSpotStorage spotStorage) {
        this.spotStorage = spotStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of spots",
            notes = "Returns list of spots",
            response = ComkerSpotDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", 
            produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerSpotDTO.Pack getSpotList(
            @ApiParam(value = "The begin position to get Spots", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_START, required=false) Integer start,
            @ApiParam(value = "How many Spots do you want to get?", required = false)
            @RequestParam(value=ComkerBaseConstant.PAGER_LIMIT, required=false) Integer limit,
            @ApiParam(value = "The query that spot's name should be matched", required = false)
            @RequestParam(value=ComkerBaseConstant.QUERY_STRING, required=false) String q) {

        Integer total;
        List collection;
        
        if (q == null) {
            total = spotStorage.count();
            collection = spotStorage.findAll(
                    sessionService.getPager(ComkerSpotDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        } else {
            ComkerQuerySieve sieve = sessionService.getSieve(ComkerSpotDTO.class)
                            .setCriterion("OR_CODE", q)
                            .setCriterion("OR_NAME", q);
            
            total = spotStorage.count(sieve);
            collection = spotStorage.findAll(sieve,
                    sessionService.getPager(ComkerSpotDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        }
        
        return new ComkerSpotDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a spot by ID",
            notes = "Returns a spot by ID (UUID)",
            response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Spot not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", 
            produces=MediaType.APPLICATION_JSON_VALUE)
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
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerSpotDTO createSpot(
            @ApiParam(value = "Spot object that needs to be added to the store", required = true)
            @RequestBody ComkerSpotDTO item) {
        return spotStorage.get(spotStorage.create(item));
    }
    
    @ApiOperation(
        value = "Update an existing spot",
        response = ComkerSpotDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Spot not found"),
            @ApiResponse(code = 406, message = "Validation error (Duplicated object)")})
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}", 
            consumes = MediaType.APPLICATION_JSON_VALUE, 
            produces = MediaType.APPLICATION_JSON_VALUE)
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
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody ComkerSpotDTO deleteSpot(
            @ApiParam(value = "ID of spot that needs to be deleted", required = true)
            @PathVariable String id) {
        ComkerSpotDTO item = spotStorage.get(id);
        spotStorage.delete(id);
        return item;
    }
}
