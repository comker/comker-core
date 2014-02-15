package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.model.po.ComkerNavbarNode;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/navbar", description = "Navigation Bar API")
@javax.ws.rs.Path("/navbar")
public class ComkerNavbarResource {

    private static Logger log = LoggerFactory.getLogger(ComkerNavbarResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerNavbarDao navbarDao = null;

    public ComkerNavbarDao getNavbarDao() {
        return navbarDao;
    }

    public void setNavbarDao(ComkerNavbarDao navbarDao) {
        this.navbarDao = navbarDao;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/tree")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Full tree of navigation nodes",
        notes = "Returns full tree of navigation nodes",
        response = ComkerNavbarNode.class)
    public Response getNavbarTreeRoot() {

        if (log.isDebugEnabled()) {
            log.debug("getNavbarTreeRoot()");
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeRoot() - Root code: {0}",
                    new Object[] {item.getCode()}));
        }

        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/tree/{mode}/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Tree of Navigation Node",
        notes = "Returns Tree of Navigation Node",
        response = ComkerNavbarNode.class)
    public Response getNavbarTree(
            @ApiParam(value = "Loading mode", required = false)
            @javax.ws.rs.PathParam("mode") String mode,
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = false)
            @javax.ws.rs.PathParam("id") String id) {
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTree() - with mode:{0} & id:{1}",
                    new Object[] {mode, id}));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (mode != null && id != null) {
            params.put(ComkerNavbarDao.FILTER_ID, id);
            params.put(ComkerNavbarDao.FILTER_MODE, mode);
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree(params);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTree() - Root code: {0}",
                    new Object[] {item.getCode()}));
        }
        
        return Response.ok().entity(item).build();
    }
}
