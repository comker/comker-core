package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerAccessDatabaseException;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/spot", description = "Spot API")
@javax.ws.rs.Path("/spot")
public class ComkerSpotResource {

    private static Logger log = LoggerFactory.getLogger(ComkerSpotResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSpotDao spotDao = null;

    public ComkerSpotDao getSpotDao() {
        return spotDao;
    }

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of spots",
        notes = "Returns list of spots",
        response = ComkerSpot.class,
        responseContainer = "List")
    public Response getSpotList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerSpotResource.getSpotList() - started");
        }
        List result = getSpotDao().findAllWhere(
                getSessionService().getSpotListCriteria(),
                getSessionService().getSpotListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerSpotResource.getSpotList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerSpot>> entity = new GenericEntity<List<ComkerSpot>>(result) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find spot by ID",
        notes = "Returns a spot by ID (UUID)",
    response = ComkerSpot.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found")})
    public Response getSpotItem(
            @ApiParam(value = "ID of spot that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerSpot item = getSpotDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Spot not found");
        }
        return Response.ok().entity(item).build();
    }

    @javax.ws.rs.POST
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Add a new spot to the database")
    @ApiResponses(value = {
        @ApiResponse(code = 409, message = "Duplicated input")})
    public Response createSpotItem(
            @ApiParam(value = "Spot object that needs to be added to the store", required = true)
                ComkerSpot item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Spot with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerSpot result = getSpotDao().create(item);

        return Response.ok().entity(result).build();
    }

    @javax.ws.rs.PUT
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Consumes({MediaType.APPLICATION_JSON})
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Update an existing spot")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found"),
        @ApiResponse(code = 405, message = "Validation exception")})
    public Response updateSpotItem(
            @ApiParam(value = "ID of spot that needs to be updated", required = true)
            @javax.ws.rs.PathParam("id")
                String id,
            @ApiParam(value = "Spot object that needs to be updated", required = true)
                ComkerSpot item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT Spot with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException(400, "Invalid ID supplied");
        }

        ComkerSpot current = getSpotDao().update(item);
        if (current == null) {
            throw new ComkerObjectNotFoundException(404, "Spot not found");
        }

        return Response.ok().entity(current).build();
    }

    @javax.ws.rs.DELETE
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Delete an existing spot")
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Spot not found"),
        @ApiResponse(code = 405, message = "Access database failure")})
    public Response deleteSpotItem(
            @ApiParam(value = "ID of spot that needs to be deleted", required = true)
            @javax.ws.rs.PathParam("id") String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete Spot with ID:{0}", new Object[] {id}));
        }

        ComkerSpot item = getSpotDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Spot not found");
        }

        try {
            getSpotDao().delete(item);
            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Spot#{0} deleted", new Object[] {id}));
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
