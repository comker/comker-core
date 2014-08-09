package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerNavNodeFormDTO;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;

@Api(value = "/navbar", description = "Navigation Bar API")
@javax.ws.rs.Path("/navbar")
public interface ComkerNavbarResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/tree")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Full tree of navigation nodes",
        notes = "Returns full tree of navigation nodes",
        response = ComkerNavNodeViewDTO.class)
    public Response getTreeFromRoot();

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Add a new node to the database",
        response = ComkerNavNodeViewDTO.class)
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createNode(
            @ApiParam(value = "Node object that needs to be added to the store", required = true)
            ComkerNavNodeFormDTO item);
}
