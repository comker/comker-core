package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import org.springframework.security.access.annotation.Secured;

@Api(value = "/user", description = "User API")
@Path("/user")
public interface ComkerUserResource {

    @GET
    @Path("/crud")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of users",
        notes = "Returns list of users. Permissions to access: PERM_COMKER_ADM_USER, PERM_COMKER_MOD_USER.",
        response = ComkerUserDTO.class,
        responseContainer = "List")
    @Secured({"PERM_COMKER_ADM_USER", "PERM_COMKER_MOD_USER"})
    public Response getUserList(
            @ApiParam(value = "The begin position to get Spots", required = false)
            @QueryParam("offset") Integer start,
            @ApiParam(value = "How many Spots do you want to get?", required = false)
            @QueryParam("max") Integer limit);

    @GET
    @Path("/crud/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find user by ID",
        notes = "Returns a user by ID (UUID). . Permissions to access: PERM_COMKER_ADM_USER, PERM_COMKER_MOD_USER.",
        response = ComkerUserDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found")})
    @Secured({"PERM_COMKER_ADM_USER", "PERM_COMKER_MOD_USER"})
    public Response getUserItem(
            @ApiParam(value = "ID of user that needs to be fetched", required = true)
            @PathParam("id") String id);

    @POST
    @Path("/crud")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Add a new user to the database",
        notes = "Permissions to access: PERM_COMKER_ADM_USER.")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    @Secured({"PERM_COMKER_ADM_USER"})
    public Response createUserItem(
            @ApiParam(value = "User object that needs to be added to the store", required = true)
                ComkerUserDTO item);

    @PUT
    @Path("/crud/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Update an existing user",
        notes = "Permissions to access: PERM_COMKER_ADM_USER.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    @Secured({"PERM_COMKER_ADM_USER"})
    public Response updateUserItem(
            @ApiParam(value = "ID of user that needs to be updated", required = true)
            @PathParam("id")
                String id,
            @ApiParam(value = "User object that needs to be updated", required = true)
                ComkerUserDTO item);

    @DELETE
    @Path("/crud/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Delete an existing user",
        notes = "Permissions to access: PERM_COMKER_ADM_USER.")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    @Secured({"PERM_COMKER_ADM_USER"})
    public Response deleteUserItem(
            @ApiParam(value = "ID of user that needs to be deleted", required = true)
            @PathParam("id") String id);
}
