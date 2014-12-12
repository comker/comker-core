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
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerUserValidator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerUserStorageImpl extends ComkerAbstractStorageImpl
        implements ComkerUserStorage {

    private static Logger log = LoggerFactory.getLogger(ComkerUserStorageImpl.class);

    private ComkerUserValidator userValidator = null;

    public void setUserValidator(ComkerUserValidator userValidator) {
        this.userValidator = userValidator;
    }
    
    private ComkerUserDao userDao = null;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }
    
    private ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }
    
    private ComkerRoleDao roleDao = null;

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
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
    public List<ComkerUserDTO> findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        return userDao.count(sieve);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerUserDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerUserDTO> poList = new ArrayList<ComkerUserDTO>();
        List dbList = userDao.findAll(sieve, pager);
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
        userDao.collectCrew(crewSet, user);

        for(ComkerCrew crew:crewSet) {
            Set<ComkerRole> roleSet = new HashSet<ComkerRole>();
            crewDao.collectGlobalRole(roleSet, crew);

            Set<ComkerPermission> permissionSet = new HashSet<ComkerPermission>();
            for(ComkerRole role:roleSet) {
                roleDao.collectPermission(permissionSet, role);
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
        userDao.collectCrew(crewSet, user);

        for(ComkerCrew crew:crewSet) {
            Map<ComkerSpot,Set<ComkerRole>> bag = new HashMap<ComkerSpot,Set<ComkerRole>>();
            crewDao.collectSpotWithRole(bag, crew);

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
                    roleDao.collectPermission(permissionSet, role);
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
        invokeValidator(userValidator, item);

        ComkerUser dbItem = new ComkerUser();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = userDao.create(dbItem);
        
        ComkerUserDTO poItem = new ComkerUserDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerUserDTO item) {
        ComkerUser dbItem = getNotNull(item.getId());

        invokeValidator(userValidator, item);

        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        userDao.update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerUser dbItem = getNotNull(id);
        userDao.delete(dbItem);
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
            ComkerCrew crew = crewDao.get(newId);
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
        ComkerUser dbItem = userDao.get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "user_with__id__not_found",
                    new ComkerExceptionExtension("error.user_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("User object with id:{0} not found", 
                                    new Object[] {id})));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getNotNullByUsername(String username) {
        ComkerUser dbItem = userDao.getByUsername(username);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "user_with__username__not_found",
                    new ComkerExceptionExtension("error.user_with__username__not_found", 
                            new Object[] {username}, 
                            MessageFormat.format("User object with username:{0} not found", 
                                    new Object[] {username})));
        }
        return dbItem;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private ComkerUser getNotNullByEmail(String email) {
        ComkerUser dbItem = userDao.getByEmail(email);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException(
                    "user_with__email__not_found",
                    new ComkerExceptionExtension("error.user_with__email__not_found", 
                            new Object[] {email}, 
                            MessageFormat.format("User object with email:{0} not found", 
                                    new Object[] {email})));
        }
        return dbItem;
    }
}
