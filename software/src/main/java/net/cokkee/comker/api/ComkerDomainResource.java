package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.dto.ComkerUserDTO;

/**
 *
 * @author drupalex
 */
@Path("/domain")
@Produces("application/json")
@Api(value = "/domain", description = "Domain API")
public interface ComkerDomainResource {
    
    @GET
    @Path("/profile/{spotcode}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Get the information of spot determined by spotcode",
        notes = "Returns the spot object",
        response = ComkerUserDTO.class)
    public Response getSpotProfile(
            @PathParam("spotcode") String spotcode);

    @GET
    @Path("/profile/{spotcode}/edit")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Load the information of spot determined by spotcode for editing",
        notes = "Returns the spot object",
        response = ComkerUserDTO.class)
    public Response loadSpotProfile(
            @PathParam("spotcode") String spotcode);

    @PUT
    @Path("/profile/{spotcode}/save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Save the information of spot determined by spotcode",
        notes = "Returns the status of operation",
        response = ComkerUserDTO.class)
    public Response saveSpotProfile(
            @PathParam("spotcode") String spotcode,
            ComkerUserDTO spotProfile);
}
