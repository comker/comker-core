package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerObjectDuplicatedException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/user", description = "User API")
@javax.ws.rs.Path("/user")
public class ComkerUserResource {

    private static Logger log = LoggerFactory.getLogger(ComkerUserResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerUserDao userDao = null;

    public ComkerUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of users",
        notes = "Returns list of users",
        response = ComkerUser.class,
        responseContainer = "List")
    public Response getUserList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerUserResource.getUserList() - started");
        }
        List result = getUserDao().findAllWhere(
                getSessionService().getUserListCriteria(),
                getSessionService().getUserListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerUserResource.getUserList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerUser>> entity = new GenericEntity<List<ComkerUser>>(result) { };
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find user by ID",
        notes = "Returns a user by ID (UUID)",
    response = ComkerUser.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found")})
    public Response getUserItem(
            @ApiParam(value = "ID of user that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerUser item = getUserDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "User not found");
        }
        return Response.ok().entity(item).build();
    }
}
