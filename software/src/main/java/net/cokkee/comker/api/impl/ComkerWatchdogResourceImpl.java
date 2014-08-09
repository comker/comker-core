package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerWatchdogResource;
import net.cokkee.comker.storage.ComkerWatchdogStorage;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerWatchdogResourceImpl implements ComkerWatchdogResource {

    private static Logger log = LoggerFactory.getLogger(ComkerWatchdogResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerWatchdogStorage watchdogStorage = null;

    public ComkerWatchdogStorage getWatchdogStorage() {
        return watchdogStorage;
    }

    public void setWatchdogStorage(ComkerWatchdogStorage watchdogStorage) {
        this.watchdogStorage = watchdogStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getWatchList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerWatchdogResource.getWatchList() - started");
        }
        List result = getWatchdogStorage().findAll(
                getSessionService().getWatchdogListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerWatchdogResource.getWatchList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerWatchdogDTO>> entity = new GenericEntity<List<ComkerWatchdogDTO>>(result) {};
        return Response.ok(entity).build();
    }

    @Override
    public Response getWatchItem(String id)
                    throws ComkerObjectNotFoundException {
        ComkerWatchdogDTO item = getWatchdogStorage().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException("Watchdog not found");
        }
        return Response.ok().entity(item).build();
    }
}
