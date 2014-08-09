package net.cokkee.comker.api.impl;

import net.cokkee.comker.api.ComkerSessionResource;
import java.text.MessageFormat;
import java.util.Calendar;
import javax.ws.rs.core.Response;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerSessionResourceImpl implements ComkerSessionResource {

    private static Logger log = LoggerFactory.getLogger(ComkerSessionResourceImpl.class);

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

    @Override
    public Response getInformation() {
        if (log.isDebugEnabled()) {
            log.debug("getInformation() - started");
        }

        ComkerSessionInfo item = new ComkerSessionInfo();

        item.setTimestamp(Calendar.getInstance().getTime());
        
        ComkerUserDetails userDetails = getSecurityService().getUserDetails();
        if (userDetails != null) {
            item.setPermissions(userDetails.getPermissions());
        }

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "getInformation() - the list of permissions has {0} items.",
                    new Object[] {item.getPermissions().size()}));
        }
        
        return Response.ok().entity(item).build();
    }
}
