package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import org.springframework.security.access.annotation.Secured;

@Api(value = "/user", description = "User API")
@javax.ws.rs.Path("/user")
public interface ComkerUserResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of users",
        notes = "Returns list of users. Permissions to access: PERM_COMKER_ADM_USER, PERM_COMKER_MOD_USER.",
        response = ComkerUserDTO.class,
        responseContainer = "List")
    @Secured({"PERM_COMKER_ADM_USER", "PERM_COMKER_MOD_USER"})
    public Response getUserList();

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
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
            @javax.ws.rs.PathParam("id") String id);

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Add a new user to the database",
        notes = "Permissions to access: PERM_COMKER_ADM_USER.")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    @Secured({"PERM_COMKER_ADM_USER"})
    public Response createUserItem(
            @ApiParam(value = "User object that needs to be added to the store", required = true)
                ComkerUserDTO item);

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
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
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "User object that needs to be updated", required = true)
                ComkerUserDTO item);

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
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
            @javax.ws.rs.PathParam("id") String id);
}
