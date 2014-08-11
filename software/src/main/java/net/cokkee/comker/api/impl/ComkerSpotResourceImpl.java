package net.cokkee.comker.api.impl;

import java.text.MessageFormat;
import java.util.List;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.ComkerSpotResource;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.exception.ComkerAccessDatabaseException;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.service.ComkerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComkerSpotResourceImpl implements ComkerSpotResource {

    private static Logger log = LoggerFactory.getLogger(ComkerSpotResourceImpl.class);

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private ComkerSessionService sessionService = null;

    public void setSessionService(ComkerSessionService sessionService) {
        this.sessionService = sessionService;
    }

    private ComkerSpotStorage spotStorage = null;

    public void setSpotStorage(ComkerSpotStorage spotStorage) {
        this.spotStorage = spotStorage;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    public Response getSpotList(Integer start, Integer limit, String q) {
        if (log.isDebugEnabled()) {
            log.debug("ComkerSpotResource.getSpotList() - start...");
        }
        
        Integer total = spotStorage.count();
        
        List collection = spotStorage.findAll(sessionService.getPager(ComkerSpotDTO.class, start, limit));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format(
                    "ComkerSpotResource.getSpotList() - the list had {0} items.",
                    new Object[] {collection.size()}));
        }

        ComkerSpotDTO.Pack result = new ComkerSpotDTO.Pack(total, collection);

        if (log.isDebugEnabled()) {
            log.debug("ComkerSpotResource.getSpotList() - done");
        }

        return Response.ok(result).build();
    }

    @Override
    public Response getSpotItem(String id)
                    throws ComkerObjectNotFoundException {
        ComkerSpotDTO item = spotStorage.get(id);
        return Response.ok().entity(item).build();
    }

    @Override
    public Response createSpotItem(ComkerSpotDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Create Spot with ID:{0}",
                    new Object[] {item.getId()}));
        }

        ComkerSpotDTO result = spotStorage.create(item);

        return Response.ok().entity(result).build();
    }

    @Override
    public Response updateSpotItem(String id, ComkerSpotDTO item) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("PUT Spot with id:{0}", new Object[] {id}));
        }

        if (!id.equals(item.getId())) {
            throw new ComkerInvalidParameterException("Invalid ID supplied");
        }

        spotStorage.update(item);

        ComkerSpotDTO current = spotStorage.get(item.getId());
        if (current == null) {
            throw new ComkerObjectNotFoundException("Spot not found");
        }

        return Response.ok().entity(current).build();
    }

    @Override
    public Response deleteSpotItem(String id) {

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Delete Spot with ID:{0}", new Object[] {id}));
        }

        ComkerSpotDTO item = spotStorage.get(id);
        if (item == null) {
            throw new ComkerObjectNotFoundException("Spot not found");
        }

        try {
            spotStorage.delete(id);
            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Spot#{0} deleted", new Object[] {id}));
            }
            return Response.ok().entity(item).build();
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format("Exception {0} - message:{1}",
                        new Object[] {e.getClass().getName(), e.getMessage()}));
            }
            throw new ComkerAccessDatabaseException("Access database failure");
        }
    }
}
