package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.exception.ComkerAccessDatabaseException;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/role", description = "Role API")
@javax.ws.rs.Path("/role")
public class ComkerRoleResource {

    private static Logger log = LoggerFactory.getLogger(ComkerRoleResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerRoleDao roleDao = null;

    public ComkerRoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of roles",
        notes = "Returns list of roles",
        response = ComkerRole.class,
        responseContainer = "List")
    public Response getRoleList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerRoleResource.getRoleList() - started");
        }
        List result = getRoleDao().findAllWhere(
                getSessionService().getRoleListCriteria(),
                getSessionService().getRoleListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerRoleResource.getRoleList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerRole>> entity = new GenericEntity<List<ComkerRole>>(result) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find role by ID",
        notes = "Returns a role by ID (UUID)",
    response = ComkerRole.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found")})
    public Response getRoleItem(
            @ApiParam(value = "ID of role that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerRole item = getRoleDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Role not found");
        }
        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new role to the database")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createRoleItem(
            @ApiParam(value = "Role object that needs to be added to the store", required = true)
                ComkerRole item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Role with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerRole result = getRoleDao().create(item);

        return Response.ok().entity(result).build();
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing role")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateRoleItem(
            @ApiParam(value = "ID of role that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "Role object that needs to be updated", required = true)
                ComkerRole item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT Role with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException(400, "Invalid ID supplied");
        }

        ComkerRole current = getRoleDao().update(item);
        if (current == null) {
            throw new ComkerObjectNotFoundException(404, "Role not found");
        }

        return Response.ok().entity(current).build();
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete an existing role")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Role not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    public Response deleteRoleItem(
            @ApiParam(value = "ID of role that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete Role with ID:{0}", new Object[] {id}));
        }

        ComkerRole item = getRoleDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Role not found");
        }

        try {
            getRoleDao().delete(item);
            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Role#{0} deleted", new Object[] {id}));
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
