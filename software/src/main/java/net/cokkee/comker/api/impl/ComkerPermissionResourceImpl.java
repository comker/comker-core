package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerPermissionResource;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerPermissionResourceImpl implements ComkerPermissionResource {

    private static Logger log = LoggerFactory.getLogger(ComkerPermissionResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerPermissionStorage permissionStorage = null;

    public void setPermissionStorage(ComkerPermissionStorage permissionStorage) {
        this.permissionStorage = permissionStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getPermissionList(Integer start, Integer limit, String q) {
        if (log.isDebugEnabled()) {
            log.debug("ComkerPermissionResource.getPermissionList() - start...");
        }

        Integer total = permissionStorage.count();

        List collection = permissionStorage.findAll(sessionService.getPager(ComkerPermissionDTO.class, start, limit));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerPermissionResource.getPermissionList() - the list had {0} items.",
                    new Object[] {collection.size()}));
        }

        ComkerPermissionDTO.Pack result = new ComkerPermissionDTO.Pack(total, collection);

        if (log.isDebugEnabled()) {
            log.debug("ComkerPermissionResource.getPermissionList() - done");
        }

        return Response.ok(result).build();
    }

    @Override
    public Response getPermissionItem(String id) {
        return Response.ok().entity(permissionStorage.get(id)).build();
    }
}
