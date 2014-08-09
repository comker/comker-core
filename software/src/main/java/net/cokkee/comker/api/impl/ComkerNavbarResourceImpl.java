package net.cokkee.comker.api.impl;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerNavbarResource;
import net.cokkee.comker.storage.ComkerNavbarStorage;
import net.cokkee.comker.model.dto.ComkerNavNodeFormDTO;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerNavbarResourceImpl implements ComkerNavbarResource {

    private static Logger log = LoggerFactory.getLogger(ComkerNavbarResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerNavbarStorage navbarStorage = null;

    public void setNavbarStorage(ComkerNavbarStorage navbarStorage) {
        this.navbarStorage = navbarStorage;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    @Override
    public Response getTreeFromRoot() {

        if (log.isDebugEnabled()) {
            log.debug("getTreeFromRoot() - start");
        }

        ComkerNavNodeViewDTO item = navbarStorage.getTree();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getTreeFromRoot() - done. Root.id: {0}",
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
        response = ComkerNavNodeViewDTO.class)
    public Response getTreeFromRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true)
            @javax.ws.rs.PathParam("excludeId") String excludeId) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getTreeFromRootExcludeNode() - with excludeNodeId:{0}",
                    new Object[] {excludeId}));
        }

        ComkerNavNodeViewDTO item = navbarStorage.getTree(null, excludeId);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getTreeFromRootExcludeNode() - Root.id: {0}",
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
        response = ComkerNavNodeViewDTO.class)
    public Response getTreeFromNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true)
            @javax.ws.rs.PathParam("id") String id) {
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getTreeFromNode() - with id:{0}",
                    new Object[] {id}));
        }

        ComkerNavNodeViewDTO item = navbarStorage.getTree(id, null);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getTreeFromNode() - Node.id: {0}",
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
        response = ComkerNavNodeViewDTO.class)
    public Response getListFromRoot() {

        if (log.isDebugEnabled()) {
            log.debug("getListFromRoot() - start");
        }

        List<ComkerNavNodeViewDTO> list = navbarStorage.getList();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getListFromRoot() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavNodeViewDTO>> entity =
                new GenericEntity<List<ComkerNavNodeViewDTO>>(list) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list/exclude/{excludeId}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "All of navigation node exclude descendants of a navigation node",
        notes = "Returns list of navigation node exclude descendants of a navigation node",
        response = ComkerNavNodeViewDTO.class,
        responseContainer = "List")
    public Response getListFromRootExcludeNode(
            @ApiParam(value = "ID of NavbarNode that needs to be excluded", required = true)
            @javax.ws.rs.PathParam("excludeId") String excludeId) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getListFromRootExcludeNode() - with excludeNodeId:{0}",
                    new Object[] {excludeId}));
        }

        List<ComkerNavNodeViewDTO> list = navbarStorage.getList(null, excludeId);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getListFromRootExcludeNode() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavNodeViewDTO>> entity =
                new GenericEntity<List<ComkerNavNodeViewDTO>>(list) {};
        return Response.ok(entity).build();
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Path("/list/from/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Descendants of a navigation node",
        notes = "Returns list of descendants of a navigation node",
        response = ComkerNavNodeViewDTO.class,
        responseContainer = "List")
    public Response getListFromNode(
            @ApiParam(value = "ID of NavbarNode that needs to be loaded", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getListFromNode() - with id:{0}",
                    new Object[] {id}));
        }

        List<ComkerNavNodeViewDTO> list = navbarStorage.getList(id, null);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getListFromNode() - done. Size of list: {0}",
                    new Object[] {list.size()}));
        }

        final GenericEntity<List<ComkerNavNodeViewDTO>> entity =
                new GenericEntity<List<ComkerNavNodeViewDTO>>(list) {};
        return Response.ok(entity).build();
    }

    @Override
    public Response createNode(
            @ApiParam(value = "Node object that needs to be added to the store", required = true)
            ComkerNavNodeFormDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Node with ID:{0}", new Object[] {item.getId()}));
        }

        ComkerNavNodeViewDTO result = navbarStorage.create(item);

        return Response.ok().entity(result).build();
    }
}
