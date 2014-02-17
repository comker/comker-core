package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericEntity;
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
    public Response getNavbarTreeOfRoot() {

        if (log.isDebugEnabled()) {
            log.debug("getNavbarTreeOfRoot() - start");
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree(null);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeOfRoot() - done. Root.id: {0}",
                    new Object[] {item.getId()}));
        }

        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/tree/exclude/{excludeId}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Tree of Navigation Node",
        notes = "Returns Tree of Navigation Node",
        response = ComkerNavbarNode.class)
    public Response getNavbarTreeOfRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true)
            @javax.ws.rs.PathParam("excludeId") String excludeId) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeOfRootExcludeNode() - with excludeNodeId:{0}",
                    new Object[] {excludeId}));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (excludeId != null) {
            params.put(ComkerNavbarDao.FILTER_EXCLUDE_ID, excludeId);
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree(params);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeOfRootExcludeNode() - Root.id: {0}",
                    new Object[] {item.getId()}));
        }

        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/tree/from/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Tree of Navigation Node",
        notes = "Returns Tree of Navigation Node",
        response = ComkerNavbarNode.class)
    public Response getNavbarTreeOfNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true)
            @javax.ws.rs.PathParam("id") String id) {
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeOfNode() - with id:{0}",
                    new Object[] {id}));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (id != null) {
            params.put(ComkerNavbarDao.FILTER_ID, id);
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree(params);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTreeOfNode() - Node.id: {0}",
                    new Object[] {item.getId()}));
        }
        
        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Full list of navigation nodes",
        notes = "Returns full list of navigation nodes",
        response = ComkerNavbarNode.class)
    public Response getNavbarListOfRoot() {

        if (log.isDebugEnabled()) {
            log.debug("getNavbarListOfRoot() - start");
        }

        List list = getNavbarDao().getNavbarList(null);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarListOfRoot() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavbarNode>> entity = new GenericEntity<List<ComkerNavbarNode>>(list) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list/exclude/{excludeId}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "All of navigation node exclude descendants of a navigation node",
        notes = "Returns list of navigation node exclude descendants of a navigation node",
        response = ComkerNavbarNode.class,
        responseContainer = "List")
    public Response getNavbarListOfRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true)
            @javax.ws.rs.PathParam("excludeId") String excludeId) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarListOfRootExcludeNode() - with excludeNodeId:{0}",
                    new Object[] {excludeId}));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (excludeId != null) {
            params.put(ComkerNavbarDao.FILTER_EXCLUDE_ID, excludeId);
        }

        List list = getNavbarDao().getNavbarList(params);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarListOfRootExcludeNode() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavbarNode>> entity = new GenericEntity<List<ComkerNavbarNode>>(list) {};
        return Response.ok(entity).build();
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list/from/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Descendants of a navigation node",
        notes = "Returns list of descendants of a navigation node",
        response = ComkerNavbarNode.class,
        responseContainer = "List")
    public Response getNavbarListOfNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarListOfNode() - with id:{0}",
                    new Object[] {id}));
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (id != null) {
            params.put(ComkerNavbarDao.FILTER_ID, id);
        }

        List list = getNavbarDao().getNavbarList(params);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarListOfNode() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavbarNode>> entity = new GenericEntity<List<ComkerNavbarNode>>(list) {};
        return Response.ok(entity).build();
    }
}
