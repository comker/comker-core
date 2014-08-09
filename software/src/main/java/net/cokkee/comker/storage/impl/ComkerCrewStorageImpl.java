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
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRole;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.service.ComkerToolboxService;
import net.cokkee.comker.structure.ComkerKeyAndValueSet;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drupalex
 */
public class ComkerCrewStorageImpl implements ComkerCrewStorage {

    private static Logger log = LoggerFactory.getLogger(ComkerCrewStorageImpl.class);

    private ComkerCrewDao crewDao = null;

    public ComkerCrewDao getCrewDao() {
        return crewDao;
    }

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    private ComkerSpotDao spotDao = null;

    public ComkerSpotDao getSpotDao() {
        return spotDao;
    }

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
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
        return getCrewDao().count();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<ComkerCrewDTO> findAll(ComkerPager filter) {
        List<ComkerCrewDTO> poList = new ArrayList<ComkerCrewDTO>();
        List dbList = getCrewDao().findAll(filter);
        for(Object dbItem:dbList) {
            ComkerCrewDTO poItem = new ComkerCrewDTO();
            ComkerDataUtil.copyProperties(dbItem, poItem);
            loadAggregationRefs((ComkerCrew)dbItem, poItem);
            poList.add(poItem);
        }
        return poList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrewDTO get(String id) {
        ComkerCrew dbItem = getNotNull(id);

        ComkerCrewDTO poItem = new ComkerCrewDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);

        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getGlobalRoleCodes(String id) {
        ComkerCrew crew = getNotNull(id);
        Set<String> result = new HashSet<String>();

        Set<ComkerRole> roles = new HashSet<ComkerRole>();
        getCrewDao().collectGlobalRole(roles, crew);

        for(ComkerRole role:roles) {
            result.add(role.getCode());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getGlobalAuthorities(String id) {
        ComkerCrew crew = getNotNull(id);
        Set<String> result = new HashSet<String>();

        Set<ComkerRole> roleSet = new HashSet<ComkerRole>();
        getCrewDao().collectGlobalRole(roleSet, crew);

        for(ComkerRole role:roleSet) {
            Set<ComkerPermission> permissionSet = new HashSet<ComkerPermission>();
            getRoleDao().collectPermission(permissionSet, role);
            for(ComkerPermission permission:permissionSet) {
                result.add(permission.getAuthority());
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String,Set<String>> getSpotCodeWithRoleCodes(String id) {
        ComkerCrew crew = getNotNull(id);
        Map<String,Set<String>> result = new HashMap<String,Set<String>>();

        Map<ComkerSpot,Set<ComkerRole>> bag = new HashMap<ComkerSpot,Set<ComkerRole>>();
        getCrewDao().collectSpotWithRole(bag, crew);
        
        for(Map.Entry<ComkerSpot,Set<ComkerRole>> entry:bag.entrySet()) {
            String spotCode = entry.getKey().getCode();
            Set<String> roleCodeSet = result.get(spotCode);
            if (roleCodeSet == null) {
                roleCodeSet = new HashSet<String>();
                result.put(spotCode, roleCodeSet);
            }
            Set<ComkerRole> roleSet = entry.getValue();
            for(ComkerRole role:roleSet) {
                roleCodeSet.add(role.getCode());
            }
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String,Set<String>> getSpotCodeWithAuthorities(String id) {
        ComkerCrew crew = getNotNull(id);
        Map<String,Set<String>> result = new HashMap<String,Set<String>>();

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
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerCrewDTO create(ComkerCrewDTO item) {
        ComkerCrew dbItem = new ComkerCrew();
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        dbItem = getCrewDao().create(dbItem);
        
        ComkerCrewDTO poItem = new ComkerCrewDTO();
        ComkerDataUtil.copyProperties(dbItem, poItem);
        loadAggregationRefs(dbItem, poItem);
        return poItem;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerCrewDTO item) {
        ComkerCrew dbItem = getNotNull(item.getId());
        ComkerDataUtil.copyProperties(item, dbItem);
        saveAggregationRefs(item, dbItem);
        getCrewDao().update(dbItem);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        ComkerCrew dbItem = getNotNull(id);
        getCrewDao().delete(dbItem);
    }

    //--------------------------------------------------------------------------

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void loadAggregationRefs(ComkerCrew crew, ComkerCrewDTO poItem) {
        if (crew == null || poItem == null) return;

        List<String> idsOfGlobalRoleList = new ArrayList<String>();
        List<ComkerCrewJoinGlobalRole> list = crew.getCrewJoinGlobalRoleList();
        for(ComkerCrewJoinGlobalRole item:list) {
            idsOfGlobalRoleList.add(item.getRole().getId());
        }
        poItem.setGlobalRoleIds(idsOfGlobalRoleList.toArray(new String[0]));

        Map<String,HashSet<String>> bag = new HashMap<String, HashSet<String>>();
        List<ComkerCrewJoinRoleWithSpot> joinRoleWithSpot = crew.getCrewJoinRoleWithSpotList();
        for(ComkerCrewJoinRoleWithSpot item:joinRoleWithSpot) {
            ComkerSpot spot = item.getSpot();
            HashSet<String> roleSet = bag.get(spot.getId());
            if (roleSet == null) {
                roleSet = new HashSet<String>();
                bag.put(spot.getId(), roleSet);
            }
            roleSet.add(item.getRole().getId());
        }

        Set<ComkerKeyAndValueSet> result = new HashSet<ComkerKeyAndValueSet>();
        for(String key: bag.keySet()) {
            result.add(new ComkerKeyAndValueSet(key, bag.get(key)));
        }
        poItem.setLimitedSpotRoleIds(result.toArray(new ComkerKeyAndValueSet[0]));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void saveAggregationRefs(ComkerCrewDTO poItem, ComkerCrew dbItem) {
        if (dbItem == null || poItem == null) return;

        if (poItem.getGlobalRoleIds() != null) {

            List<String> newIds = new ArrayList<String>(Arrays.asList(poItem.getGlobalRoleIds()));

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of updated CrewJoinGlobalRoles: {0}",
                        new Object[] {newIds.size()}));
            }

            List<ComkerCrewJoinGlobalRole> oldList = dbItem.getCrewJoinGlobalRoleList();

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of old CrewJoinGlobalRoles: {0}",
                        new Object[] {oldList.size()}));
            }

            List<ComkerCrewJoinGlobalRole> newList = new ArrayList<ComkerCrewJoinGlobalRole>();

            for(ComkerCrewJoinGlobalRole item:oldList) {
                String oldId = item.getRole().getId();
                if (newIds.contains(oldId)) {
                    newList.add(item);
                    newIds.remove(oldId);
                }
            }
            for(String newId:newIds) {
                ComkerRole newRole = getRoleDao().get(newId);
                if (newRole == null) continue;
                newList.add(new ComkerCrewJoinGlobalRole(dbItem, newRole));
            }
            dbItem.getCrewJoinGlobalRoleList().clear();
            dbItem.getCrewJoinGlobalRoleList().addAll(newList);

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of new CrewJoinGlobalRoles: {0}",
                        new Object[] {newList.size()}));
            }
        }

        if (poItem.getLimitedSpotRoleIds() != null) {
            Map<String,Set<String>> newIds = new HashMap<String, Set<String>>();
            for(ComkerKeyAndValueSet keyAndValueSet:poItem.getLimitedSpotRoleIds()) {
                Set<String> valueSet = newIds.get(keyAndValueSet.getKey());
                if (valueSet == null) {
                    valueSet = new HashSet<String>();
                    newIds.put(keyAndValueSet.getKey(), valueSet);
                }
                valueSet.addAll(keyAndValueSet.getValueSet());
            }

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of updated KeyAndValueSet: {0}",
                        new Object[] {newIds.size()}));
            }

            List<ComkerCrewJoinRoleWithSpot> oldList = dbItem.getCrewJoinRoleWithSpotList();

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of old CrewJoinRoleWithSpot objects: {0}",
                        new Object[] {oldList.size()}));
            }

            List<ComkerCrewJoinRoleWithSpot> newList = new ArrayList<ComkerCrewJoinRoleWithSpot>();

            for(ComkerCrewJoinRoleWithSpot item:oldList) {
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
                    ComkerSpot newSpot = getSpotDao().get(newId.getKey());
                    if (newSpot == null) continue;
                    ComkerRole newRole = getRoleDao().get(newRoleId);
                    if (newRole == null) continue;
                    newList.add(new ComkerCrewJoinRoleWithSpot(dbItem, newRole, newSpot));
                }
            }

            if (log.isDebugEnabled()) {
                log.debug(MessageFormat.format("Number of new CrewJoinRoleWithSpot objects: {0}",
                        new Object[] {newList.size()}));
            }

            dbItem.getCrewJoinRoleWithSpotList().clear();
            dbItem.getCrewJoinRoleWithSpotList().addAll(newList);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerCrew getNotNull(String id) {
        ComkerCrew dbItem = getCrewDao().get(id);
        if (dbItem == null) {
            throw new ComkerObjectNotFoundException("crew_not_found");
        }
        return dbItem;
    }
}
