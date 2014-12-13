package net.cokkee.comker.storage.impl;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.storage.ComkerWatchdogStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
import net.cokkee.comker.model.dpo.ComkerWatchdogDPO;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerWatchdogStorageImpl implements ComkerWatchdogStorage {

    private static Logger log = LoggerFactory.getLogger(ComkerWatchdogStorageImpl.class);
    
    private ComkerWatchdogDao watchdogDao = null;

    public ComkerWatchdogDao getWatchdogDao() {
        return watchdogDao;
    }

    public void setWatchdogDao(ComkerWatchdogDao watchdogDao) {
        this.watchdogDao = watchdogDao;
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
    public List<ComkerWatchdogDTO> findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        return getWatchdogDao().count(sieve);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerWatchdogDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerWatchdogDTO> poList = new ArrayList<ComkerWatchdogDTO>();
        List dbList = getWatchdogDao().findAll(sieve, pager);
        for(Object dbItem:dbList) {
            ComkerWatchdogDTO poItem = new ComkerWatchdogDTO();
            ComkerDataUtil.copyProperties(dbItem, poItem);
            loadAggregationRefs((ComkerWatchdogDPO)dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerWatchdogDTO get(String id) {
        ComkerWatchdogDPO dbItem = getWatchdogDao().get(id);
        ComkerWatchdogDTO poItem = new ComkerWatchdogDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerWatchdogDTO create(ComkerWatchdogDTO item) {
        ComkerWatchdogDPO dbItem = new ComkerWatchdogDPO();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = getWatchdogDao().create(dbItem);
        ComkerWatchdogDTO poItem = new ComkerWatchdogDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerWatchdogDTO item) {
        ComkerWatchdogDPO dbItem = getWatchdogDao().get(item.getId());
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        getWatchdogDao().update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerWatchdogDPO dbItem = getWatchdogDao().get(id);
        getWatchdogDao().delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    private void loadAggregationRefs(ComkerWatchdogDPO dbItem, ComkerWatchdogDTO poItem) {
    }

    private void saveAggregationRefs(ComkerWatchdogDTO poItem, ComkerWatchdogDPO dbItem) {
    }
}
