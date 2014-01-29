package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/permission", description = "Permission API")
@javax.ws.rs.Path("/permission")
public class ComkerPermissionResource {

    private static Logger log = LoggerFactory.getLogger(ComkerPermissionResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerPermissionDao permissionDao = null;

    public ComkerPermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of permissions",
        notes = "Returns list of permissions",
        response = ComkerPermission.class,
        responseContainer = "List")
    public Response getPermissionList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerPermissionResource.getPermissionList() - started");
        }
        List result = getPermissionDao().findAllWhere(
                getSessionService().getPermissionListCriteria(),
                getSessionService().getPermissionListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerPermissionResource.getPermissionList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerPermission>> entity = new GenericEntity<List<ComkerPermission>>(result) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find permission by ID",
        notes = "Returns a permission by ID (UUID)",
    response = ComkerPermission.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Permission not found")})
    public Response getPermissionItem(
            @ApiParam(value = "ID of permission that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerPermission item = getPermissionDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Permission not found");
        }
        return Response.ok().entity(item).build();
    }
}
