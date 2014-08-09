package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerRoleDTO;

@Api(value = "/role", description = "Role API")
@javax.ws.rs.Path("/role")
public interface ComkerRoleResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of roles",
        notes = "Returns list of roles",
        response = ComkerRoleDTO.class,
        responseContainer = "List")
    public Response getRoleList();

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find role by ID",
        notes = "Returns a role by ID (UUID)",
        response = ComkerRoleDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found")})
    public Response getRoleItem(
            @ApiParam(value = "ID of role that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id);

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Add a new role to the database",
        response = ComkerRoleDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createRoleItem(
            @ApiParam(value = "Role object that needs to be added to the store", required = true)
            ComkerRoleDTO item);

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Update an existing role",
        response = ComkerRoleDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateRoleItem(
            @ApiParam(value = "ID of role that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "Role object that needs to be updated", required = true)
                ComkerRoleDTO item);

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Delete an existing role",
        response = ComkerRoleDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    public Response deleteRoleItem(
            @ApiParam(value = "ID of role that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id);
}
