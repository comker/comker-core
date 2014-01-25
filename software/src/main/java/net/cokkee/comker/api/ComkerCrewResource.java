package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.exception.ComkerObjectDuplicatedException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/crew", description = "Crew API")
@javax.ws.rs.Path("/crew")
public class ComkerCrewResource {

    private static Logger log = LoggerFactory.getLogger(ComkerCrewResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerCrewDao crewDao = null;

    public ComkerCrewDao getCrewDao() {
        return crewDao;
    }

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of crews",
        notes = "Returns list of crews",
        response = ComkerCrew.class,
        responseContainer = "List")
    public Response getCrewList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerCrewResource.getCrewList() - started");
        }
        List result = getCrewDao().findAllWhere(
                getSessionService().getCrewListCriteria(),
                getSessionService().getCrewListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerCrewResource.getCrewList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerCrew>> entity = new GenericEntity<List<ComkerCrew>>(result) {};
        return Response.ok(entity).build();
    }

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/{id}")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "Find crew by ID",
        notes = "Returns a crew by ID (UUID)",
    response = ComkerCrew.class)
    @ApiResponses(value = {
        @ApiResponse(code = 400, message = "Invalid ID supplied"),
        @ApiResponse(code = 404, message = "Crew not found")})
    public Response getCrewItem(
            @ApiParam(value = "ID of crew that needs to be fetched", required = true)
            @javax.ws.rs.PathParam("id") String id)
                    throws ComkerObjectNotFoundException {
        ComkerCrew item = getCrewDao().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException(404, "Crew not found");
        }
        return Response.ok().entity(item).build();
    }
}
