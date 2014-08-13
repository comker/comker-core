package net.cokkee.comker.storage.impl;

import java.util.ArrayList;
import java.util.List;
import net.cokkee.comker.dao.ComkerWatchdogDao;
import net.cokkee.comker.storage.ComkerWatchdogStorage;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
import net.cokkee.comker.model.po.ComkerWatchdog;
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
        return getWatchdogDao().count();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerWatchdogDTO> findAll(ComkerPager filter) {
        List<ComkerWatchdogDTO> poList = new ArrayList<ComkerWatchdogDTO>();
        List dbList = getWatchdogDao().findAll(filter);
        for(Object dbItem:dbList) {
            ComkerWatchdogDTO poItem = new ComkerWatchdogDTO();
            ComkerDataUtil.copyProperties(dbItem, poItem);
            loadAggregationRefs((ComkerWatchdog)dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerWatchdogDTO get(String id) {
        ComkerWatchdog dbItem = getWatchdogDao().get(id);
        ComkerWatchdogDTO poItem = new ComkerWatchdogDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerWatchdogDTO create(ComkerWatchdogDTO item) {
        ComkerWatchdog dbItem = new ComkerWatchdog();
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
        ComkerWatchdog dbItem = getWatchdogDao().get(item.getId());
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        getWatchdogDao().update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerWatchdog dbItem = getWatchdogDao().get(id);
        getWatchdogDao().delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    private void loadAggregationRefs(ComkerWatchdog dbItem, ComkerWatchdogDTO poItem) {
    }

    private void saveAggregationRefs(ComkerWatchdogDTO poItem, ComkerWatchdog dbItem) {
    }
}