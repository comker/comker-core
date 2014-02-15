package net.cokkee.comker.api;

import com.wordnik.swagger.annotations.*;
import java.text.MessageFormat;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api(value = "/auth", description = "Authentication & Authorization API")
@javax.ws.rs.Path("/auth")
public class ComkerAuthResource {

    private static Logger log = LoggerFactory.getLogger(ComkerAuthResource.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSecurityService securityService = null;

    public ComkerSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }

    
    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @javax.ws.rs.GET
    @javax.ws.rs.Path("/session")
    @javax.ws.rs.Produces({MediaType.APPLICATION_JSON})
    @ApiOperation(
        value = "List all of spots",
        notes = "Returns list of spots",
        response = ComkerSessionInfo.class)
    public Response getSessionInfo() {
        if (log.isDebugEnabled()) {
            log.debug("getSessionInfo() - started");
        }

        ComkerSessionInfo item = new ComkerSessionInfo();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getSessionInfo() - the list of permissions has {0} items.",
                    new Object[] {item.getPermissions().size()}));
        }
        
        return Response.ok().entity(item).build();
    }
}
