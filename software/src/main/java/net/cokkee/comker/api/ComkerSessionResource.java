package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.ComkerSessionInfo;

@Api(value = "/session", description = "Authentication & Authorization API")
@javax.ws.rs.Path("/session")
public interface ComkerSessionResource {

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/information")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Returns the information of current session",
        notes = "Returns the information of current session: logedin username, permissions, checkpoint.",
        response = ComkerSessionInfo.class)
    public Response getInformation();
}
