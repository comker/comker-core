package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerRoleResource;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerRoleResourceImpl implements ComkerRoleResource {

    private static Logger log = LoggerFactory.getLogger(ComkerRoleResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerRoleStorage roleStorage = null;

    public void setRoleStorage(ComkerRoleStorage roleStorage) {
        this.roleStorage = roleStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getRoleList(Integer start, Integer limit, String q) {
        if (log.isDebugEnabled()) {
            log.debug("ComkerRoleResource.getRoleList() - start...");
        }

        Integer total = roleStorage.count();

        List collection = roleStorage.findAll(sessionService.getPager(ComkerRoleDTO.class, start, limit));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerRoleResource.getRoleList() - the list had {0} items.",
                    new Object[] {collection.size()}));
        }

        ComkerRoleDTO.Pack result = new ComkerRoleDTO.Pack(total, collection);

        if (log.isDebugEnabled()) {
            log.debug("ComkerRoleResource.getRoleList() - done");
        }

        return Response.ok(result).build();
    }

    @Override
    public Response getRoleItem(String id) {
        return Response.ok().entity(roleStorage.get(id)).build();
    }

    @Override
    public Response createRoleItem(ComkerRoleDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Role with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerRoleDTO result = roleStorage.create(item);

        return Response.ok().entity(result).build();
    }

    @Override
    public Response updateRoleItem(String id, ComkerRoleDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT Role with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }

        roleStorage.update(item);

        return Response.ok().entity(roleStorage.get(id)).build();
    }

    @Override
    public Response deleteRoleItem(String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete Role with ID:{0}", new Object[] {id}));
        }

        ComkerRoleDTO item = roleStorage.get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException("Role not found");
        }

        roleStorage.delete(id);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Role#{0} deleted", new Object[] {id}));
        }
        
        return Response.ok().entity(item).build();
    }
}
