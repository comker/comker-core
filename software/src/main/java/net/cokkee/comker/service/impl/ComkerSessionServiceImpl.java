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

    private ComkerPager spotListPager = new ComkerPager();

    @Override
    public ComkerPager getSpotListPager() {
        return spotListPager;
    }

    //----------------------------------------------------------------------

    private Map<String,Object> roleListCriteria = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getRoleListCriteria() {
        return roleListCriteria;
    }

    private ComkerPager roleListPager = new ComkerPager();

    @Override
    public ComkerPager getRoleListPager() {
        return roleListPager;
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
