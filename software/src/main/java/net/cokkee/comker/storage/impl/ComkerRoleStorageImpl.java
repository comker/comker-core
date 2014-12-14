package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerRoleValidator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerRoleStorageImpl extends ComkerAbstractStorageImpl
        implements ComkerRoleStorage {

    private static Logger log = LoggerFactory.getLogger(ComkerRoleStorageImpl.class);

    private ComkerRoleValidator roleValidator = null;

    public void setRoleValidator(ComkerRoleValidator roleValidator) {
        this.roleValidator = roleValidator;
    }

    private ComkerRoleDao roleDao = null;

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count() {
        return count(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerRoleDTO> findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        return roleDao.count(sieve);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerRoleDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerRoleDTO> dtoList = new ArrayList<ComkerRoleDTO>();
        List dpoList = roleDao.findAll(sieve, pager);
        for(Object dpoItem:dpoList) {
            ComkerRoleDTO dtoItem = new ComkerRoleDTO();
            ComkerDataUtil.copyProperties(dpoItem, dtoItem);
            loadAggregationRefs((ComkerRoleDPO)dpoItem, dtoItem);
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDTO get(String id) {
        ComkerRoleDPO dpoItem = getNotNull(id);
        ComkerRoleDTO dtoItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dpoItem, dtoItem);
        loadAggregationRefs(dpoItem, dtoItem);
        return dtoItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDTO getByCode(String code) {
        ComkerRoleDPO dbItem = getNotNullByCode(code);
        ComkerRoleDTO poItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getAuthorities(String id) {
        ComkerRoleDPO role = getNotNull(id);

        Set<ComkerPermissionDPO> permissions = new HashSet<ComkerPermissionDPO>();
        roleDao.collectPermission(permissions, role);

        Set<String> result = new HashSet<String>();
        for(ComkerPermissionDPO permission:permissions) {
            result.add(permission.getAuthority());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerRoleDTO create(ComkerRoleDTO item) {
        invokeValidator(roleValidator, item);
        
        ComkerRoleDPO dpoItem = new ComkerRoleDPO();
        ComkerDataUtil.copyProperties(item, dpoItem);
        saveAggregationRefs(item, dpoItem);
        dpoItem = roleDao.create(dpoItem);
        
        ComkerRoleDTO dtoItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dpoItem, dtoItem);
        loadAggregationRefs(dpoItem, dtoItem);
        return dtoItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerRoleDTO item) {
        ComkerRoleDPO dbItem = getNotNull(item.getId());

        invokeValidator(roleValidator, item);

        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        roleDao.update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerRoleDPO dbItem = getNotNull(id);
        roleDao.delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerRoleDPO dbItem, ComkerRoleDTO poItem) {
        if (dbItem == null || poItem == null) return;
        List<String> permissionIds = new ArrayList<String>();
        List<ComkerRoleJoinPermissionDPO> list = dbItem.getRoleJoinPermissionList();
        for(ComkerRoleJoinPermissionDPO item:list) {
            permissionIds.add(item.getPermission().getId());
        }
        poItem.setPermissionIds(permissionIds.toArray(new String[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerRoleDTO poItem, ComkerRoleDPO dbItem) {
        if (dbItem == null || poItem == null) return;

        if (poItem.getPermissionIds() == null) return;
        
        List<String> newIds = new ArrayList<String>(Arrays.asList(poItem.getPermissionIds()));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of updated permissions: {0}",
                    new Object[] {newIds.size()}));
        }

        List<ComkerRoleJoinPermissionDPO> oldList = dbItem.getRoleJoinPermissionList();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of old permissions: {0}",
                    new Object[] {oldList.size()}));
        }

        List<ComkerRoleJoinPermissionDPO> newList = new ArrayList<ComkerRoleJoinPermissionDPO>();

        for(ComkerRoleJoinPermissionDPO item:oldList) {
            String oldId = item.getPermission().getId();
            if (newIds.contains(oldId)) {
                newList.add(item);
                newIds.remove(oldId);
            }
        }
        for(String newId:newIds) {
            ComkerPermissionDPO permission = permissionDao.get(newId);
            if (permission == null) continue;
            newList.add(new ComkerRoleJoinPermissionDPO(dbItem, permission));
        }
        dbItem.getRoleJoinPermissionList().clear();
        dbItem.getRoleJoinPermissionList().addAll(newList);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of new permissions: {0}",
                    new Object[] {newList.size()}));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDPO getNotNull(String id) {
        ComkerRoleDPO dbItem = roleDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "role_with__id__not_found",
                    new ComkerExceptionExtension("error.role_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Role object with id:{0} not found", 
                                    new Object[] {id})));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDPO getNotNullByCode(String code) {
        ComkerRoleDPO dbItem = roleDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "role_with__code__not_found",
                    new ComkerExceptionExtension("error.role_with__code__not_found", 
                            new Object[] {code}, 
                            MessageFormat.format("Role object with code:{0} not found", 
                                    new Object[] {code})));
        }
        return dbItem;
    }
}
