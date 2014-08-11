package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;

@Api(value = "/permission", description = "Permission API")
@javax.ws.rs.Path("/permission")
public interface ComkerPermissionResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of permissions",
        notes = "Returns list of permissions",
        response = ComkerPermissionDTO.Pack.class)
    public Response getPermissionList(
            @ApiParam(value = "The begin position to get Permissions", required = false)
            @QueryParam("offset") Integer start,
            @ApiParam(value = "How many Permissions do you want to get?", required = false)
            @QueryParam("max") Integer limit,
            @ApiParam(value = "The query that permission's name should be matched", required = false)
            @QueryParam("q") String q);

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find permission by ID",
        notes = "Returns a permission by ID (UUID)",
    response = ComkerPermissionDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Permission not found")})
    public Response getPermissionItem(
            @ApiParam(value = "ID of permission that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id);
}
