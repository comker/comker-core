package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerRoleDao;
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
}
