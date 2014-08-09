package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerPermissionResource;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerPermissionResourceImpl implements ComkerPermissionResource {

    private static Logger log = LoggerFactory.getLogger(ComkerPermissionResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public ComkerSessionService getSessionService() {
        return sessionService;
    }

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerPermissionStorage permissionStorage = null;

    public ComkerPermissionStorage getPermissionStorage() {
        return permissionStorage;
    }

    public void setPermissionStorage(ComkerPermissionStorage permissionStorage) {
        this.permissionStorage = permissionStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getPermissionList() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerPermissionResource.getPermissionList() - started");
        }
        List result = getPermissionStorage().findAll(
                getSessionService().getPermissionListPager());
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerPermissionResource.getPermissionList() - the list had {0} items.",
                    new Object[] {result.size()}));
        }
        final GenericEntity<List<ComkerPermissionDTO>> entity = new GenericEntity<List<ComkerPermissionDTO>>(result) {};
        return Response.ok(entity).build();
    }

    @Override
    public Response getPermissionItem(String id)
                    throws ComkerObjectNotFoundException {
        ComkerPermissionDTO item = getPermissionStorage().get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException("Permission not found");
        }
        return Response.ok().entity(item).build();
    }
}
