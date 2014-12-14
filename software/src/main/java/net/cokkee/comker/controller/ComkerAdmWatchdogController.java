package net.cokkee.comker.controller;

import com.wordnik.swagger.annotations.*;
import java.util.List;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.storage.ComkerWatchdogStorage;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
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

@Api(value = "admwatchdog", description = "Administration Watchdog API")
@Controller
@RequestMapping("/comker/adm/watchdog")
public class ComkerAdmWatchdogController {

    private ComkerSessionService sessionService = null;

    @Autowired(required = false)
    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerWatchdogStorage watchdogStorage = null;

    @Autowired(required = false)
    public void setWatchdogStorage(ComkerWatchdogStorage watchdogStorage) {
        this.watchdogStorage = watchdogStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation(
            value = "List all of watchdogs",
            notes = "Returns list of watchdogs",
            response = ComkerWatchdogDTO.Pack.class)
    @RequestMapping(method = RequestMethod.GET, value = "", produces="application/json")
    public @ResponseBody ComkerWatchdogDTO.Pack getWatchdogList(
            @ApiParam(value = "The begin position to get Watchdogs", required = false)
            @RequestParam(value="start", required=false) Integer start,
            @ApiParam(value = "How many Watchdogs do you want to get?", required = false)
            @RequestParam(value="limit", required=false) Integer limit,
            @ApiParam(value = "The query that watchdog's name should be matched", required = false)
            @RequestParam(value="q", required=false) String q) {

        Integer total;
        List collection;
        
        if (q == null) {
            total = watchdogStorage.count();
            collection = watchdogStorage.findAll(
                    sessionService.getPager(ComkerWatchdogDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        } else {
            ComkerQuerySieve sieve = sessionService.getSieve(ComkerWatchdogDTO.class)
                            .setCriterion("OR_USERNAME", q)
                            .setCriterion("OR_METHOD_NAME", q)
                            .setCriterion("OR_COMMENT", q);
            
            total = watchdogStorage.count(sieve);
            collection = watchdogStorage.findAll(sieve,
                    sessionService.getPager(ComkerWatchdogDTO.class)
                            .updateStart(start)
                            .updateLimit(limit));
        }
        
        return new ComkerWatchdogDTO.Pack(total, collection);
    }
    
    @ApiOperation(
            value = "Get a watchdog by ID",
            notes = "Returns a watchdog by ID (UUID)",
            response = ComkerWatchdogDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Watchdog not found")})
    @RequestMapping(method = RequestMethod.GET, value = "/{id}", produces="application/json")
    public @ResponseBody ComkerWatchdogDTO getWatchdog(
            @ApiParam(value = "ID of watchdog that needs to be fetched", required = true) 
            @PathVariable String id) {
        return watchdogStorage.get(id);
    }
}
