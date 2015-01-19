package net.cokkee.comker.storage.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.model.struct.ComkerKeyAndValueSet;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerCrewValidator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerCrewStorageImpl extends ComkerAbstractStorageImpl
        implements ComkerCrewStorage {

    private static final Logger log = LoggerFactory.getLogger(ComkerCrewStorageImpl.class);

    private ComkerCrewValidator crewValidator = null;

    public void setCrewValidator(ComkerCrewValidator crewValidator) {
        this.crewValidator = crewValidator;
    }

    private ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    private ComkerSpotDao spotDao = null;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
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
    public List<ComkerCrewDTO> findAll(ComkerQueryPager pager) {
        return findAll(null, pager);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        return crewDao.count(sieve);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerCrewDTO> findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        List<ComkerCrewDTO> poList = new ArrayList<ComkerCrewDTO>();
        List dbList = crewDao.findAll(sieve, pager);
        for(Object dbItem:dbList) {
            ComkerCrewDTO poItem = new ComkerCrewDTO();
            ComkerDataUtil.copyProperties(dbItem, poItem);
            loadAggregationRefs((ComkerCrewDPO)dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDTO get(String id) {
        ComkerCrewDPO dpoItem = getNotNull(id);

        ComkerCrewDTO dtoItem = new ComkerCrewDTO();
        ComkerDataUtil.copyProperties(dpoItem, dtoItem);
        loadAggregationRefs(dpoItem, dtoItem);

        return dtoItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getGlobalRoleCodes(String id) {
        ComkerCrewDPO crew = getNotNull(id);
        Set<String> result = new LinkedHashSet<String>();

        Set<ComkerRoleDPO> roles = new LinkedHashSet<ComkerRoleDPO>();
        crewDao.collectGlobalRole(roles, crew);

        for(ComkerRoleDPO role:roles) {
            result.add(role.getCode());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getGlobalAuthorities(String id) {
        ComkerCrewDPO crew = getNotNull(id);
        Set<String> result = new LinkedHashSet<String>();

        Set<ComkerRoleDPO> roleSet = new LinkedHashSet<ComkerRoleDPO>();
        crewDao.collectGlobalRole(roleSet, crew);

        for(ComkerRoleDPO role:roleSet) {
            Set<ComkerPermissionDPO> permissionSet = new LinkedHashSet<ComkerPermissionDPO>();
            roleDao.collectPermission(permissionSet, role);
            for(ComkerPermissionDPO permission:permissionSet) {
                result.add(permission.getAuthority());
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String,Set<String>> getSpotCodeWithRoleCodes(String id) {
        ComkerCrewDPO crew = getNotNull(id);
        Map<String,Set<String>> result = new LinkedHashMap<String,Set<String>>();

        Map<ComkerSpotDPO,Set<ComkerRoleDPO>> bag = new LinkedHashMap<ComkerSpotDPO,Set<ComkerRoleDPO>>();
        crewDao.collectSpotWithRole(bag, crew);
        
        for(Map.Entry<ComkerSpotDPO,Set<ComkerRoleDPO>> entry:bag.entrySet()) {
            String spotCode = entry.getKey().getCode();
            Set<String> roleCodeSet = result.get(spotCode);
            if (roleCodeSet == null) {
                roleCodeSet = new LinkedHashSet<String>();
                result.put(spotCode, roleCodeSet);
            }
            Set<ComkerRoleDPO> roleSet = entry.getValue();
            for(ComkerRoleDPO role:roleSet) {
                roleCodeSet.add(role.getCode());
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String,Set<String>> getSpotCodeWithAuthorities(String id) {
        ComkerCrewDPO crew = getNotNull(id);
        Map<String,Set<String>> result = new LinkedHashMap<String,Set<String>>();

        Map<ComkerSpotDPO,Set<ComkerRoleDPO>> bag = new LinkedHashMap<ComkerSpotDPO,Set<ComkerRoleDPO>>();
        crewDao.collectSpotWithRole(bag, crew);

        for(Map.Entry<ComkerSpotDPO,Set<ComkerRoleDPO>> entry:bag.entrySet()) {
            String spotCode = entry.getKey().getCode();
            Set<String> permissionCodeSet = result.get(spotCode);
            if (permissionCodeSet == null) {
                permissionCodeSet = new LinkedHashSet<String>();
                result.put(spotCode, permissionCodeSet);
            }
            Set<ComkerRoleDPO> roleSet = entry.getValue();
            for(ComkerRoleDPO role:roleSet) {
                Set<ComkerPermissionDPO> permissionSet = new LinkedHashSet<ComkerPermissionDPO>();
                roleDao.collectPermission(permissionSet, role);
                for(ComkerPermissionDPO permission:permissionSet) {
                    permissionCodeSet.add(permission.getAuthority());
                }
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerCrewDTO create(ComkerCrewDTO item) {
        invokeValidator(crewValidator, item);

        ComkerCrewDPO dbItem = new ComkerCrewDPO();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = crewDao.create(dbItem);
        
        ComkerCrewDTO poItem = new ComkerCrewDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerCrewDTO item) {
        ComkerCrewDPO dbItem = getNotNull(item.getId());

        invokeValidator(crewValidator, item);
        
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        crewDao.update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerCrewDPO dbItem = getNotNull(id);
        crewDao.delete(dbItem);
    }

    //--------------------------------------------------------------------------

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerCrewDPO crew, ComkerCrewDTO poItem) {
        if (crew == null || poItem == null) return;

        Set<String> idsOfGlobalRoleList = new LinkedHashSet<String>();
        List<ComkerCrewJoinGlobalRoleDPO> list = crew.getCrewJoinGlobalRoleList();
        for(ComkerCrewJoinGlobalRoleDPO item:list) {
            idsOfGlobalRoleList.add(item.getRole().getId());
        }
        poItem.setGlobalRoleIds(idsOfGlobalRoleList.toArray(new String[0]));

        Map<String,Set<String>> bag = new LinkedHashMap<String, Set<String>>();
        List<ComkerCrewJoinRoleWithSpotDPO> joinRoleWithSpot = crew.getCrewJoinRoleWithSpotList();
        for(ComkerCrewJoinRoleWithSpotDPO item:joinRoleWithSpot) {
            ComkerSpotDPO spot = item.getSpot();
            Set<String> roleSet = bag.get(spot.getId());
            if (roleSet == null) {
                roleSet = new LinkedHashSet<String>();
                bag.put(spot.getId(), roleSet);
            }
            roleSet.add(item.getRole().getId());
        }

        Set<ComkerKeyAndValueSet> result = new LinkedHashSet<ComkerKeyAndValueSet>();
        for(String key: bag.keySet()) {
            result.add(new ComkerKeyAndValueSet(key, bag.get(key).toArray(new String[0])));
        }
        poItem.setScopedRoleIds(result.toArray(new ComkerKeyAndValueSet[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerCrewDTO dtoItem, ComkerCrewDPO dpoItem) {
        if (dpoItem == null || dtoItem == null) return;

        if (dtoItem.getGlobalRoleIds() != null) {

            List<String> newIds = new ArrayList<String>(Arrays.asList(dtoItem.getGlobalRoleIds()));

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of updated CrewJoinGlobalRoles: {0}",
                        new Object[] {newIds.size()}));
            }

            List<ComkerCrewJoinGlobalRoleDPO> oldList = dpoItem.getCrewJoinGlobalRoleList();

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of old CrewJoinGlobalRoles: {0}",
                        new Object[] {oldList.size()}));
            }

            List<ComkerCrewJoinGlobalRoleDPO> newList = new ArrayList<ComkerCrewJoinGlobalRoleDPO>();

            for(ComkerCrewJoinGlobalRoleDPO item:oldList) {
                String oldId = item.getRole().getId();
                if (newIds.contains(oldId)) {
                    newList.add(item);
                    newIds.remove(oldId);
                }
            }
            for(String newId:newIds) {
                ComkerRoleDPO newRole = roleDao.get(newId);
                if (newRole == null) continue;
                newList.add(new ComkerCrewJoinGlobalRoleDPO(dpoItem, newRole));
            }
            dpoItem.getCrewJoinGlobalRoleList().clear();
            dpoItem.getCrewJoinGlobalRoleList().addAll(newList);

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of new CrewJoinGlobalRoles: {0}",
                        new Object[] {newList.size()}));
            }
        }

        if (dtoItem.getScopedRoleIds() != null) {
            Map<String,Set<String>> newIds = new LinkedHashMap<String, Set<String>>();
            for(ComkerKeyAndValueSet keyAndValues:dtoItem.getScopedRoleIds()) {
                Set<String> valueSet = newIds.get(keyAndValues.getKey());
                if (valueSet == null) {
                    valueSet = new LinkedHashSet<String>();
                    newIds.put(keyAndValues.getKey(), valueSet);
                }
                valueSet.addAll(Arrays.asList(keyAndValues.getValues()));
            }

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of updated KeyAndValueSet: {0}",
                        new Object[] {newIds.size()}));
            }

            List<ComkerCrewJoinRoleWithSpotDPO> oldList = dpoItem.getCrewJoinRoleWithSpotList();

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of old CrewJoinRoleWithSpot objects: {0}",
                        new Object[] {oldList.size()}));
            }

            List<ComkerCrewJoinRoleWithSpotDPO> newList = new ArrayList<ComkerCrewJoinRoleWithSpotDPO>();

            for(ComkerCrewJoinRoleWithSpotDPO item:oldList) {
                String oldSpotId = item.getSpot().getId();
                String oldRoleId = item.getRole().getId();
                if (newIds.containsKey(oldSpotId)) {
                    Set<String> newRoleIds = newIds.get(oldSpotId);
                    if (newRoleIds.contains(oldRoleId)) {
                        newList.add(item);
                        newRoleIds.remove(oldRoleId);
                    }
                }
            }
            for(Map.Entry<String,Set<String>> newId:newIds.entrySet()) {
                for(String newRoleId:newId.getValue()) {
                    ComkerSpotDPO newSpot = spotDao.get(newId.getKey());
                    if (newSpot == null) continue;
                    ComkerRoleDPO newRole = roleDao.get(newRoleId);
                    if (newRole == null) continue;
                    newList.add(new ComkerCrewJoinRoleWithSpotDPO(dpoItem, newRole, newSpot));
                }
            }

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of new CrewJoinRoleWithSpot objects: {0}",
                        new Object[] {newList.size()}));
            }

            dpoItem.getCrewJoinRoleWithSpotList().clear();
            dpoItem.getCrewJoinRoleWithSpotList().addAll(newList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDPO getNotNull(String id) {
        ComkerCrewDPO dpoItem = crewDao.get(id);
        if (dpoItem == null) {
            throw new ComkerObjectNotFoundException(
                    "crew_with__id__not_found",
                    new ComkerExceptionExtension("error.crew_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Crew object with id:{0} not found", 
                                    new Object[] {id})));
        }
        return dpoItem;
    }
}
