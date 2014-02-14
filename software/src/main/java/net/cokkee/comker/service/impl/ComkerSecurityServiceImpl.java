package net.cokkee.comker.service.impl;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.service.ComkerSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerSecurityServiceImpl implements ComkerSecurityService {

    private final Logger log = LoggerFactory.getLogger(ComkerSecurityServiceImpl.class);


    private ComkerUserDao userDao = null;

    public ComkerUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }


    private ComkerSpotDao spotDao = null;

    public ComkerSpotDao getSpotDao() {
        return spotDao;
    }

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
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


    private ComkerPermissionDao permissionDao = null;

    public ComkerPermissionDao getPermissionDao() {
        return permissionDao;
    }

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    
}
