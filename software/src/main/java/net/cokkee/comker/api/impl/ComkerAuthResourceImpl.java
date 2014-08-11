package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerAuthResource;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerAuthResourceImpl implements ComkerAuthResource {

    private static Logger log = LoggerFactory.getLogger(ComkerAuthResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSecurityService securityService = null;

    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }

    
    private ComkerSessionService sessionService = null;

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getSessionInfo() {
        if (log.isDebugEnabled()) {
            log.debug("getSessionInfo() - started");
        }

        ComkerSessionInfo item = new ComkerSessionInfo();

        ComkerUserDetails userDetails = securityService.getUserDetails();
        if (userDetails != null) {
            item.setPermissions(userDetails.getPermissions());
        }

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getSessionInfo() - the list of permissions has {0} items.",
                    new Object[] {item.getPermissions().size()}));
        }
        
        return Response.ok().entity(item).build();
    }
}
