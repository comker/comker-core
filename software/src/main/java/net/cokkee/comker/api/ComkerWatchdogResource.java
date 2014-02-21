package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerWatchdog;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/watchdog", description = "Watchdog API")
@javax.ws.rs.Path("/watchdog")
public class ComkerWatchdogResource {

    private static Logger log = LoggerFactory.getLogger(ComkerWatchdogResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerWatchdogDao watchdogDao = null;

    public ComkerWatchdogDao getWatchdogDao() {
        return watchdogDao;
    }

    public void setWatchdogDao(ComkerWatchdogDao watchdogDao) {
        this.watchdogDao = watchdogDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of watchs",
        notes = "Returns list of watchs",
        response = ComkerWatchdog.class,
        responseContainer = "List")
    public Response getWatchList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerWatchdogResource.getWatchList() - started");
        }
        List result = getWatchdogDao().findAllWhere(
                getSessionService().getWatchdogListCriteria(),
                getSessionService().getWatchdogListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerWatchdogResource.getWatchList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerWatchdog>> entity = new GenericEntity<List<ComkerWatchdog>>(result) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/crud/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find watchdog by ID",
        notes = "Returns a watchdog by ID (UUID)",
    response = ComkerWatchdog.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Watchdog not found")})
    public Response getWatchItem(
            @ApiParam(value = "ID of watchdog that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerWatchdog item = getWatchdogDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Watchdog not found");
        }
        return Response.ok().entity(item).build();
    }
}
