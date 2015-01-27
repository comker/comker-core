package net.cokkee.comker.storage.impl;

import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.storage.ComkerPermissionStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.validation.ComkerPermissionValidator;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerPermissionStorageImpl extends ComkerAbstractStorageImpl
        implements ComkerPermissionStorage {

    private ComkerPermissionValidator permissionValidator = null;

    public void setPermissionValidator(ComkerPermissionValidator permissionValidator) {
        this.permissionValidator = permissionValidator;
    }
    
    private ComkerPermissionDao permissionDao = null;

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    private ComkerToolboxService toolboxService = null;

    public void setToolboxService(ComkerToolboxService toolboxService) {
        this.toolboxService = toolboxService;
    }

    @Override
    public Integer count() {
        return count(null);
    }

    @Override
    public List findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    public Integer count(ComkerQuerySieve sieve) {
        return permissionDao.count(sieve);
    }

    @Override
    public List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerPermissionDTO> polist = new ArrayList<ComkerPermissionDTO>();
        List<ComkerPermissionDPO> dblist = permissionDao.findAll(sieve, pager);
        for(ComkerPermissionDPO dbItem:dblist) {
            ComkerPermissionDTO poItem = new ComkerPermissionDTO(
                dbItem.getId(),
                dbItem.getAuthority());
            polist.add(poItem);
        }
        return polist;
    }

    @Override
    public ComkerPermissionDTO get(String id) {
        ComkerPermissionDPO dbItem = permissionDao.get(id);
        ComkerPermissionDTO poItem = new ComkerPermissionDTO(
                dbItem.getId(),
                dbItem.getAuthority());
        return poItem;
    }

    @Override
    public ComkerPermissionDTO getByAuthority(String authority) {
        ComkerPermissionDPO dbItem = permissionDao.getByAuthority(authority);
        ComkerPermissionDTO poItem = new ComkerPermissionDTO(
                dbItem.getId(),
                dbItem.getAuthority());
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerPermissionDTO create(ComkerPermissionDTO item) {
        invokeValidator(permissionValidator, item);

        ComkerPermissionDPO dbItem = new ComkerPermissionDPO(
            item.getAuthority());
        dbItem = permissionDao.save(dbItem);

        ComkerPermissionDTO poItem = new ComkerPermissionDTO(
                dbItem.getId(),
                dbItem.getAuthority());
        return poItem;
    }
}
