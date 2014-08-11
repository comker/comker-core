package net.cokkee.comker.service.impl;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.service.ComkerUserDetailsService;

/**
 *
 * @author drupalex
 */
public class ComkerSessionServiceImpl implements ComkerSessionService {

    private ComkerUserDetailsService userDetailsService;

    public ComkerUserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(ComkerUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //----------------------------------------------------------------------

    private Map<Class,ComkerPager> pagers = new HashMap<Class, ComkerPager>();

    @Override
    public ComkerPager getPager(Class clazz) {
        if (clazz == null) return null;

        ComkerPager pager = pagers.get(clazz);
        if (pager == null) {
            pager = new ComkerPager();
            pagers.put(clazz, pager);
        }

        return pager;
    }

    @Override
    public ComkerPager getPager(Class clazz, Integer start, Integer limit) {
        ComkerPager pager = getPager(clazz);
        if (start != null) pager.setStart(start);
        if (limit != null) pager.setLimit(limit);
        return pager;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> userListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getUserListCriteria() {
        return userListCriteria;
    }

    private ComkerPager userListPager = new ComkerPager();

    @Override
    public ComkerPager getUserListPager() {
        return userListPager;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> crewListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getCrewListCriteria() {
        return crewListCriteria;
    }

    private ComkerPager crewListPager = new ComkerPager();

    @Override
    public ComkerPager getCrewListPager() {
        return crewListPager;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> spotListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getSpotListCriteria() {
        return spotListCriteria;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> roleListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getRoleListCriteria() {
        return roleListCriteria;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> permissionListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getPermissionListCriteria() {
        return permissionListCriteria;
    }

    private ComkerPager permissionListPager = new ComkerPager();

    @Override
    public ComkerPager getPermissionListPager() {
        return permissionListPager;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> watchdogListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getWatchdogListCriteria() {
        return watchdogListCriteria;
    }

    private ComkerPager watchdogListPager = new ComkerPager();

    @Override
    public ComkerPager getWatchdogListPager() {
        return watchdogListPager;
    }
}
