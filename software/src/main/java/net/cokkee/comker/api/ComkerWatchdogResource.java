package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;

@Api(value = "/watchdog", description = "Watchdog API")
@javax.ws.rs.Path("/watchdog")
public interface ComkerWatchdogResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of watchdogs",
        notes = "Returns list of watchdogs",
        response = ComkerWatchdogDTO.Pack.class)
    public Response getWatchdogList(
            @ApiParam(value = "The begin position to get Watchdogs", required = false)
            @QueryParam("offset") Integer start,
            @ApiParam(value = "How many Watchdogs do you want to get?", required = false)
            @QueryParam("max") Integer limit,
            @ApiParam(value = "The query that watchdog's name should be matched", required = false)
            @QueryParam("q") String q);

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find watchdog by ID",
        notes = "Returns a watchdog by ID (UUID)",
        response = ComkerWatchdogDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Watchdog not found")})
    public Response getWatchItem(
            @ApiParam(value = "ID of watchdog that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id);
}
