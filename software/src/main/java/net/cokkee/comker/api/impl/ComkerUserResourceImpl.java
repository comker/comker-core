package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerUserResource;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerUserResourceImpl implements ComkerUserResource {

    private static Logger log = LoggerFactory.getLogger(ComkerUserResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerUserStorage userStorage = null;

    public void setUserStorage(ComkerUserStorage userStorage) {
        this.userStorage = userStorage;
    }
    
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getUserList(Integer start, Integer limit) {
        if (log.isDebugEnabled()) {
            log.debug("ComkerUserResource.getUserList() - started");
        }

        Integer total = userStorage.count();

        List collection = userStorage.findAll(sessionService.getPager(ComkerUserDTO.class, start, limit));
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerUserResource.getUserList() - the list had {0} items.",
                    new Object[] {collection.size()}));
        }

        ComkerUserDTO.Pack result = new ComkerUserDTO.Pack(total, collection);

        if (log.isDebugEnabled()) {
            log.debug("ComkerUserResource.getUserList() - done");
        }

        return Response.ok(result).build();
    }

    @Override
    public Response getUserItem(String id) {
        return Response.ok().entity(userStorage.get(id)).build();
    }

    @Override
    public Response createUserItem(ComkerUserDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create User with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerUserDTO result = userStorage.create(item);

        return Response.ok().entity(result).build();
    }

    @Override
    public Response updateUserItem(String id, ComkerUserDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT User with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }

        userStorage.update(item);

        ComkerUserDTO current = userStorage.get(id);

        return Response.ok().entity(current).build();
    }

    @Override
    public Response deleteUserItem(String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete User with ID:{0}", new Object[] {id}));
        }

        ComkerUserDTO item = userStorage.get(id);
        
        userStorage.delete(id);
        
        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("User#{0} deleted", new Object[]{id}));
        }
        
        return Response.ok().entity(item).build();
    }
}
