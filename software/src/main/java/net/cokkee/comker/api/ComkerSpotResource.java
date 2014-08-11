package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerSpotDTO;

@Api(value = "/spot", description = "Spot API")
@javax.ws.rs.Path("/spot")
public interface ComkerSpotResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of spots",
        notes = "Returns list of spots",
        response = ComkerSpotDTO.Pack.class)
    public Response getSpotList(
            @ApiParam(value = "The begin position to get Spots", required = false)
            @QueryParam("offset") Integer start,
            @ApiParam(value = "How many Spots do you want to get?", required = false)
            @QueryParam("max") Integer limit,
            @ApiParam(value = "The query that spot's name should be matched", required = false)
            @QueryParam("q") String q);

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find spot by ID",
        notes = "Returns a spot by ID (UUID)",
    response = ComkerSpotDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found")})
    public Response getSpotItem(
            @ApiParam(value = "ID of spot that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id);

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new spot to the database")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createSpotItem(
            @ApiParam(value = "Spot object that needs to be added to the store", required = true)
                ComkerSpotDTO item);

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing spot")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateSpotItem(
            @ApiParam(value = "ID of spot that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "Spot object that needs to be updated", required = true)
                ComkerSpotDTO item);

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete an existing spot")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    public Response deleteSpotItem(
            @ApiParam(value = "ID of spot that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id);
}
