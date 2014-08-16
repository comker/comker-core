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
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
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
    public List<ComkerRoleDTO> findAll(ComkerPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerRoleDTO.Filter filter) {
        return roleDao.count(filter);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerRoleDTO> findAll(ComkerRoleDTO.Filter filter, ComkerPager pager) {
        List<ComkerRoleDTO> dtoList = new ArrayList<ComkerRoleDTO>();
        List dpoList = roleDao.findAll(filter, pager);
        for(Object dpoItem:dpoList) {
            ComkerRoleDTO dtoItem = new ComkerRoleDTO();
            ComkerDataUtil.copyProperties(dpoItem, dtoItem);
            loadAggregationRefs((ComkerRole)dpoItem, dtoItem);
            dtoList.add(dtoItem);
        }
        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDTO get(String id) {
        ComkerRole dbItem = getNotNull(id);
        ComkerRoleDTO poItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDTO getByCode(String code) {
        ComkerRole dbItem = getNotNullByCode(code);
        ComkerRoleDTO poItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getAuthorities(String id) {
        ComkerRole role = getNotNull(id);

        Set<ComkerPermission> permissions = new HashSet<ComkerPermission>();
        roleDao.collectPermission(permissions, role);

        Set<String> result = new HashSet<String>();
        for(ComkerPermission permission:permissions) {
            result.add(permission.getAuthority());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerRoleDTO create(ComkerRoleDTO item) {
        invokeValidator(roleValidator, item);
        
        ComkerRole dbItem = new ComkerRole();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = roleDao.create(dbItem);
        
        ComkerRoleDTO poItem = new ComkerRoleDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerRoleDTO item) {
        ComkerRole dbItem = getNotNull(item.getId());

        invokeValidator(roleValidator, item);

        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        roleDao.update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerRole dbItem = getNotNull(id);
        roleDao.delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerRole dbItem, ComkerRoleDTO poItem) {
        if (dbItem == null || poItem == null) return;
        List<String> idsOfPermissionList = new ArrayList<String>();
        List<ComkerRoleJoinPermission> list = dbItem.getRoleJoinPermissionList();
        for(ComkerRoleJoinPermission item:list) {
            idsOfPermissionList.add(item.getPermission().getId());
        }
        poItem.setPermissionIds(idsOfPermissionList.toArray(new String[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerRoleDTO poItem, ComkerRole dbItem) {
        if (dbItem == null || poItem == null) return;

        if (poItem.getPermissionIds() == null) return;
        
        List<String> newIds = new ArrayList<String>(Arrays.asList(poItem.getPermissionIds()));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of updated permissions: {0}",
                    new Object[] {newIds.size()}));
        }

        List<ComkerRoleJoinPermission> oldList = dbItem.getRoleJoinPermissionList();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of old permissions: {0}",
                    new Object[] {oldList.size()}));
        }

        List<ComkerRoleJoinPermission> newList = new ArrayList<ComkerRoleJoinPermission>();

        for(ComkerRoleJoinPermission item:oldList) {
            String oldId = item.getPermission().getId();
            if (newIds.contains(oldId)) {
                newList.add(item);
                newIds.remove(oldId);
            }
        }
        for(String newId:newIds) {
            ComkerPermission permission = permissionDao.get(newId);
            if (permission == null) continue;
            newList.add(new ComkerRoleJoinPermission(dbItem, permission));
        }
        dbItem.getRoleJoinPermissionList().clear();
        dbItem.getRoleJoinPermissionList().addAll(newList);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of new permissions: {0}",
                    new Object[] {newList.size()}));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRole getNotNull(String id) {
        ComkerRole dbItem = roleDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException("role_not_found");
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRole getNotNullByCode(String code) {
        ComkerRole dbItem = roleDao.getByCode(code);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException("role_not_found");
        }
        return dbItem;
    }
}
