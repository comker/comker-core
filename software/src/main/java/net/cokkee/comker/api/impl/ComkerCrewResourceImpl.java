package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerCrewResource;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerCrewResourceImpl implements ComkerCrewResource {

    private static Logger log = LoggerFactory.getLogger(ComkerCrewResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerCrewStorage crewStorage = null;

    public ComkerCrewStorage getCrewStorage() {
        return crewStorage;
    }

    public void setCrewStorage(ComkerCrewStorage crewStorage) {
        this.crewStorage = crewStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getCrewList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerCrewResource.getCrewList() - started");
        }
        List result = getCrewStorage().findAll(
                getSessionService().getCrewListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerCrewResource.getCrewList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerCrewDTO>> entity = new GenericEntity<List<ComkerCrewDTO>>(result) {};
        return Response.ok(entity).build();
    }

    @Override
    public Response getCrewItem(String id) {
        ComkerCrewDTO item = getCrewStorage().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException("Crew not found");
        }
        return Response.ok().entity(item).build();
    }
}
