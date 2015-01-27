package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.model.dpo.ComkerModuleDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerSpotJoinModuleDPO;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.validation.ComkerSpotValidator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerSpotStorageImpl extends ComkerAbstractStorageImpl
        implements ComkerSpotStorage {

    private static final Logger log = LoggerFactory.getLogger(ComkerSpotStorageImpl.class);

    private ComkerSpotValidator spotValidator = null;

    public void setSpotValidator(ComkerSpotValidator spotValidator) {
        this.spotValidator = spotValidator;
    }

    private ComkerSpotDao spotDao = null;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }
    
    private ComkerModuleDao moduleDao = null;

    public void setModuleDao(ComkerModuleDao moduleDao) {
        this.moduleDao = moduleDao;
    }

    private ComkerToolboxService toolboxService = null;

    public ComkerToolboxService getToolboxService() {
        return toolboxService;
    }

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count() {
        return count(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerSpotDTO> findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        return spotDao.count(sieve);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerSpotDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerSpotDTO> poList = new ArrayList<ComkerSpotDTO>();
        List<ComkerSpotDPO> dbList = spotDao.findAll(sieve, pager);
        for(ComkerSpotDPO dbItem:dbList) {
            ComkerSpotDTO poItem = new ComkerSpotDTO(
                    dbItem.getId(),
                    dbItem.getCode(),
                    dbItem.getName(),
                    dbItem.getDescription());
            loadAggregationRefs(dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSpotDTO get(String id) {
        ComkerSpotDPO dbItem = getNotNull(id);
        ComkerSpotDTO poItem = new ComkerSpotDTO(
                    dbItem.getId(),
                    dbItem.getCode(),
                    dbItem.getName(),
                    dbItem.getDescription());
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSpotDTO getByCode(String code) {
        ComkerSpotDPO dbItem = getNotNullByCode(code);
        ComkerSpotDTO poItem = new ComkerSpotDTO(
                    dbItem.getId(),
                    dbItem.getCode(),
                    dbItem.getName(),
                    dbItem.getDescription());
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String create(ComkerSpotDTO item) {
        invokeValidator(spotValidator, item);

        ComkerSpotDPO dbItem = new ComkerSpotDPO();
        dbItem.update(item.getCode(), item.getName(), item.getDescription());
        saveAggregationRefs(item, dbItem);
        return spotDao.create(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerSpotDTO item) {
        ComkerSpotDPO dbItem = getNotNull(item.getId());

        invokeValidator(spotValidator, item);
        
        dbItem.update(item.getCode(), item.getName(), item.getDescription());
        saveAggregationRefs(item, dbItem);
        spotDao.update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerSpotDPO dbItem = getNotNull(id);
        spotDao.delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerSpotDPO dbItem, ComkerSpotDTO poItem) {
        if (dbItem == null || poItem == null) return;
        List<String> idsOfModuleList = new ArrayList<String>();
        List<ComkerSpotJoinModuleDPO> list = dbItem.getSpotJoinModuleList();
        for(ComkerSpotJoinModuleDPO item:list) {
            idsOfModuleList.add(item.getModule().getId());
        }
        poItem.setModuleIds(idsOfModuleList.toArray(new String[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerSpotDTO poItem, ComkerSpotDPO dbItem) {
        if (dbItem == null || poItem == null) return;

        if (poItem.getModuleIds() == null) return;
        
        List<String> newIds = new ArrayList<String>(Arrays.asList(poItem.getModuleIds()));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of updated modules: {0}",
                    new Object[] {newIds.size()}));
        }

        List<ComkerSpotJoinModuleDPO> oldList = dbItem.getSpotJoinModuleList();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of old modules: {0}",
                    new Object[] {oldList.size()}));
        }

        List<ComkerSpotJoinModuleDPO> newList = new ArrayList<ComkerSpotJoinModuleDPO>();

        for(ComkerSpotJoinModuleDPO item:oldList) {
            String oldId = item.getModule().getId();
            if (newIds.contains(oldId)) {
                newList.add(item);
                newIds.remove(oldId);
            }
        }
        for(String newId:newIds) {
            ComkerModuleDPO module = moduleDao.get(newId);
            if (module == null) continue;
            newList.add(new ComkerSpotJoinModuleDPO(dbItem, module));
        }
        dbItem.getSpotJoinModuleList().clear();
        dbItem.getSpotJoinModuleList().addAll(newList);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of new modules: {0}",
                    new Object[] {newList.size()}));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSpotDPO getNotNull(String id) {
        ComkerSpotDPO dbItem = spotDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "spot_with__id__not_found",
                    new ComkerExceptionExtension("error.spot_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Spot object with id:{0} not found", 
                                    new Object[] {id})));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerSpotDPO getNotNullByCode(String code) {
        ComkerSpotDPO dbItem = spotDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "spot_with__code__not_found",
                    new ComkerExceptionExtension("error.spot_with__code__not_found", 
                            new Object[] {code}, 
                            MessageFormat.format("Spot object with code:{0} not found", 
                                    new Object[] {code})));
        }
        return dbItem;
    }
}
