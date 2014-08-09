package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
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
        value = "List all of watchs",
        notes = "Returns list of watchs",
        response = ComkerWatchdogDTO.class,
        responseContainer = "List")
    public Response getWatchList();

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
