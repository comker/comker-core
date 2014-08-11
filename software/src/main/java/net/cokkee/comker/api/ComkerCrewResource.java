package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerCrewDTO;

@Api(value = "/crew", description = "Crew API")
@javax.ws.rs.Path("/crew")
public interface ComkerCrewResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of crews",
        notes = "Returns list of crews",
        response = ComkerCrewDTO.Pack.class)
    public Response getCrewList(
            @ApiParam(value = "The begin position to get Crews", required = false)
            @QueryParam("offset") Integer start,
            @ApiParam(value = "How many Crews do you want to get?", required = false)
            @QueryParam("max") Integer limit,
            @ApiParam(value = "The query that crew's name should be matched", required = false)
            @QueryParam("q") String q);

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find crew by ID",
        notes = "Returns a crew by ID (UUID)",
    response = ComkerCrewDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Crew not found")})
    public Response getCrewItem(
            @ApiParam(value = "ID of crew that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id);
}
