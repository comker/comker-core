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
@Path("/account")
@Produces("application/json")
@Api(value = "/account", description = "User Account API")
public interface ComkerMemberResource {
    
    @GET
    @Path("/profile/{username}")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Get the information of user determined by username",
        notes = "Returns the user account object",
        response = ComkerUserDTO.class)
    public Response getUserProfile(
            @PathParam("username") String username);

    @GET
    @Path("/profile/{username}/edit")
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Load the information of user determined by username for editing",
        notes = "Returns the user account object",
        response = ComkerUserDTO.class)
    public Response loadUserProfile(
            @PathParam("username") String username);

    @PUT
    @Path("/profile/{username}/save")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Save the information of user determined by username",
        notes = "Returns the status of operation",
        response = ComkerUserDTO.class)
    public Response saveUserProfile(
            @PathParam("username") String username,
            ComkerUserDTO userProfile);

    @POST
    @Path("/changepassword")
    @Produces({MediaType.APPLICATION_JSON})
    public Response changePassword(
            @FormParam("current_password") String currentPassword,
            @FormParam("new_password") String newPassword);

    @POST
    @Path("/resetpassword")
    @Produces({MediaType.APPLICATION_JSON})
    public Response resetPassword(
            @FormParam("email_or_username") String emailOrUsername);

    @GET
    @Path("/resetpassword/confirmation/{code}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response resetPasswordConfirmation(
            @PathParam("code") String confirmationCode);
}
