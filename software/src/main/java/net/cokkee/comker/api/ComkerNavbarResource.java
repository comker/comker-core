package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
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
        value = "Tree of Navigation Node",
        notes = "Returns Tree of Navigation Node",
        response = ComkerNavbarNode.class)
    public Response getNavbarTree() {
        if (log.isDebugEnabled()) {
            log.debug("getNavbarTree() - started");
        }

        ComkerNavbarNode item = getNavbarDao().getNavbarTree();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getNavbarTree() - Root's code: {0}",
                    new Object[] {item.getCode()}));
        }
        
        return Response.ok().entity(item).build();
    }
}
