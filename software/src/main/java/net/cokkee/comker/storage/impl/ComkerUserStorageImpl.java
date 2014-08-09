package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.ComkerExceptionExtension;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
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
public class ComkerUserStorageImpl implements ComkerUserStorage {

    private static Logger log = LoggerFactory.getLogger(ComkerUserStorageImpl.class);
    
    private ComkerUserDao userDao = null;

    public ComkerUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }
    
    private ComkerCrewDao crewDao = null;

    public ComkerCrewDao getCrewDao() {
        return crewDao;
    }

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }
    
    private ComkerRoleDao roleDao = null;

    public ComkerRoleDao getRoleDao() {
        return roleDao;
    }

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
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
        return getUserDao().count();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerUserDTO> findAll(ComkerPager filter) {
        List<ComkerUserDTO> poList = new ArrayList<ComkerUserDTO>();
        List dbList = getUserDao().findAll(filter);
        for(Object dbItem:dbList) {
            ComkerUserDTO poItem = new ComkerUserDTO();
            ComkerDataUtil.copyProperties(dbItem, poItem);
            loadAggregationRefs((ComkerUser)dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDTO get(String id) {
        ComkerUser dbItem = getNotNull(id);
        ComkerUserDTO poItem = new ComkerUserDTO();

        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);

        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDTO getByUsername(String username) {
        ComkerUser dbItem = getNotNullByUsername(username);
        ComkerUserDTO poItem = new ComkerUserDTO();

        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);

        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDTO getByEmail(String email) {
        ComkerUser dbItem = getNotNullByEmail(email);
        ComkerUserDTO poItem = new ComkerUserDTO();

        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);

        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getGlobalAuthorities(String id) {
        Set<String> result = new HashSet<String>();
        collectGlobalAuthorities(result, id);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void collectGlobalAuthorities(Set<String> result, String id) {
        ComkerUser user = getNotNull(id);

        Set<ComkerCrew> crewSet = new HashSet<ComkerCrew>();
        getUserDao().collectCrew(crewSet, user);

        for(ComkerCrew crew:crewSet) {
            Set<ComkerRole> roleSet = new HashSet<ComkerRole>();
            getCrewDao().collectGlobalRole(roleSet, crew);

            Set<ComkerPermission> permissionSet = new HashSet<ComkerPermission>();
            for(ComkerRole role:roleSet) {
                getRoleDao().collectPermission(permissionSet, role);
            }

            for(ComkerPermission permission:permissionSet) {
                result.add(permission.getAuthority());
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String,Set<String>> getSpotCodeWithAuthorities(String id) {
        Map<String,Set<String>> result = new HashMap<String,Set<String>>();
        collectSpotCodeWithAuthorities(result, id);
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void collectSpotCodeWithAuthorities(Map<String,Set<String>> result, String id) {
        ComkerUser user = getNotNull(id);

        Set<ComkerCrew> crewSet = new HashSet<ComkerCrew>();
        getUserDao().collectCrew(crewSet, user);

        for(ComkerCrew crew:crewSet) {
            Map<ComkerSpot,Set<ComkerRole>> bag = new HashMap<ComkerSpot,Set<ComkerRole>>();
            getCrewDao().collectSpotWithRole(bag, crew);

            for(Map.Entry<ComkerSpot,Set<ComkerRole>> entry:bag.entrySet()) {
                String spotCode = entry.getKey().getCode();
                Set<String> permissionCodeSet = result.get(spotCode);
                if (permissionCodeSet == null) {
                    permissionCodeSet = new HashSet<String>();
                    result.put(spotCode, permissionCodeSet);
                }
                Set<ComkerRole> roleSet = entry.getValue();
                for(ComkerRole role:roleSet) {
                    Set<ComkerPermission> permissionSet = new HashSet<ComkerPermission>();
                    getRoleDao().collectPermission(permissionSet, role);
                    for(ComkerPermission permission:permissionSet) {
                        permissionCodeSet.add(permission.getAuthority());
                    }
                }
            }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerUserDTO create(ComkerUserDTO item) {
        ComkerUser dbItem = new ComkerUser();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = getUserDao().create(dbItem);
        
        ComkerUserDTO poItem = new ComkerUserDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerUserDTO item) {
        ComkerUser dbItem = getNotNull(item.getId());
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        getUserDao().update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerUser dbItem = getNotNull(id);
        getUserDao().delete(dbItem);
    }

    //--------------------------------------------------------------------------
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerUser dbItem, ComkerUserDTO poItem) {
        if (dbItem == null || poItem == null) return;
        List<String> idsOfCrewList = new ArrayList<String>();
        List<ComkerUserJoinCrew> list = dbItem.getUserJoinCrewList();
        for(ComkerUserJoinCrew item:list) {
            idsOfCrewList.add(item.getCrew().getId());
        }
        poItem.setCrewIds(idsOfCrewList.toArray(new String[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerUserDTO poItem, ComkerUser dbItem) {
        if (dbItem == null || poItem == null) return;

        if (poItem.getCrewIds() == null) return;
        
        List<String> newIds = new ArrayList<String>(Arrays.asList(poItem.getCrewIds()));

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of updated crews: {0}",
                    new Object[] {newIds.size()}));
        }

        List<ComkerUserJoinCrew> oldList = dbItem.getUserJoinCrewList();

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of old crews: {0}",
                    new Object[] {oldList.size()}));
        }

        List<ComkerUserJoinCrew> newList = new ArrayList<ComkerUserJoinCrew>();

        for(ComkerUserJoinCrew item:oldList) {
            String oldId = item.getCrew().getId();
            if (newIds.contains(oldId)) {
                newList.add(item);
                newIds.remove(oldId);
            }
        }
        for(String newId:newIds) {
            ComkerCrew crew = getCrewDao().get(newId);
            if (crew == null) continue;
            newList.add(new ComkerUserJoinCrew(dbItem, crew));
        }
        dbItem.getUserJoinCrewList().clear();
        dbItem.getUserJoinCrewList().addAll(newList);

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("Number of new crews: {0}",
                    new Object[] {newList.size()}));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getNotNull(String id) {
        ComkerUser dbItem = getUserDao().get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    MessageFormat.format("User object with id:{0} not found", new Object[] {id}),
                    new ComkerExceptionExtension("user_with_id_not_found", new Object[] {id}));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getNotNullByUsername(String username) {
        ComkerUser dbItem = getUserDao().getByUsername(username);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    MessageFormat.format("User object with username:{0} not found", new Object[] {username}),
                    new ComkerExceptionExtension("user_with_username_not_found", new Object[] {username}));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getNotNullByEmail(String email) {
        ComkerUser dbItem = getUserDao().getByEmail(email);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    MessageFormat.format("User object with email:{0} not found", new Object[] {email}),
                    new ComkerExceptionExtension("user_with_email_not_found", new Object[] {email}));
        }
        return dbItem;
    }
}
