package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerAccessDatabaseException;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
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
    @javax.ws.rs.Path("/crud")
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
    @javax.ws.rs.Path("/crud/{id}")
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

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new user to the database")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createUserItem(
            @ApiParam(value = "User object that needs to be added to the store", required = true)
                ComkerUser item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create User with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerUser result = getUserDao().create(item);

        return Response.ok().entity(result).build();
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing user")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateUserItem(
            @ApiParam(value = "ID of user that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "User object that needs to be updated", required = true)
                ComkerUser item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT User with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException(400, "Invalid ID supplied");
        }

        ComkerUser current = getUserDao().update(item);
        if (current == null) {
            throw new ComkerObjectNotFoundException(404, "User not found");
        }

        return Response.ok().entity(current).build();
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete an existing user")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "User not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    public Response deleteUserItem(
            @ApiParam(value = "ID of user that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete User with ID:{0}", new Object[] {id}));
        }

        ComkerUser item = getUserDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "User not found");
        }

        try {
            getUserDao().delete(item);
            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("User#{0} deleted", new Object[] {id}));
            }
            return Response.ok().entity(item).build();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format("Exception {0} - message:{1}",
                        new Object[] {e.getClass().getName(), e.getMessage()}));
            }
            throw new ComkerAccessDatabaseException(405, "Access database failure");
        }
    }
}
