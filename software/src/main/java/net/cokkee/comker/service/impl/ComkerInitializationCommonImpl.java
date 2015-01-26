package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.*;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 *
 * @author drupalex
 */
public abstract class ComkerInitializationCommonImpl
        implements ComkerInitializationService, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(ComkerInitializationCommonImpl.class);

    protected ComkerUserDao userDao = null;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }

    protected ComkerSpotDao spotDao = null;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }

    protected ComkerSettingDao settingDao = null;

    public void setSettingDao(ComkerSettingDao settingDao) {
        this.settingDao = settingDao;
    }

    protected ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    protected ComkerRoleDao roleDao = null;

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    protected ComkerPermissionDao permissionDao = null;

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }


    protected ComkerNavbarDao navbarDao = null;

    public void setNavbarDao(ComkerNavbarDao navbarDao) {
        this.navbarDao = navbarDao;
    }


    protected PasswordEncoder passwordEncoder = null;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //----------------------------------------------------------------------

    public ComkerInitializationCommonImpl() {
    }

    //----------------------------------------------------------------------
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDao, "userDao must be specified");
        Assert.notNull(spotDao, "spotDao must be specified");
        Assert.notNull(crewDao, "crewDao must be specified");
        Assert.notNull(roleDao, "roleDao must be specified");
        Assert.notNull(permissionDao, "permissionDao must be specified");
        Assert.notNull(navbarDao, "navbarDao must be specified");
        Assert.notNull(passwordEncoder, "passwordEncoder must be specified");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void initDefaultSpot(ComkerSpotDPO spot) {

        if (spot == null) return;

        ComkerSpotDPO spotDefault = getDefaultSpot();
        ComkerUserDPO userUnknown = getUnknownUser();
        ComkerUserDPO userDefault = getDefaultUser();

        // -- init spot settings
        ComkerSettingKeyDPO key = null;
        String value = null;

        key = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_SPOT_THEME, ComkerSettingKeyDPO.TYPE_STRING, "");
        value = settingDao.getValue(spotDefault, userUnknown, key);
        settingDao.define(spot, userUnknown, key, value);

        key = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_USER_LANGUAGE, ComkerSettingKeyDPO.TYPE_STRING, "");
        value = settingDao.getValue(spotDefault, userDefault, key);
        settingDao.define(spot, userDefault, key, value);

        // -- init crews (groups)
        ComkerRoleDPO roleMgr = getManagerRole();
        ComkerCrewDPO crewMgrs = getOrCreateCrew(spot, roleMgr);

        ComkerRoleDPO roleMbr = getMemberRole();
        ComkerCrewDPO crewMbrs = getOrCreateCrew(spot, roleMbr);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void initDefaultUser(ComkerUserDPO user, ComkerSpotDPO spot, ComkerRoleDPO role) {

        if (user == null) return;
        ComkerUserDPO userDefault = getDefaultUser();

        if (spot == null) {
            spot = getDefaultSpot();
        }

        if (role == null) {
            role = getMemberRole();
        }

        // init user settings
        ComkerSettingKeyDPO key = null;
        String value = null;

        key = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_USER_LANGUAGE, ComkerSettingKeyDPO.TYPE_STRING, "");
        value = settingDao.getValue(spot, userDefault, key);
        settingDao.define(spot, user, key, value);

        // init user group
        ComkerCrewDPO crew = getOrCreateCrew(spot, role);
        userDao.addCrew(user, crew);
    }

    //==========================================================================
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpotDPO getOrCreateSpot(String code, String name, String description) {
        ComkerSpotDPO item = spotDao.getByCode(code);
        if (item == null) {
            item = new ComkerSpotDPO(code, name, description);
            spotDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpotDPO getUnknownSpot() {
        return getOrCreateSpot(ComkerSpotDPO.UNKNOWN, "UNKNOWN SPOT", "");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpotDPO getDefaultSpot() {
        return getOrCreateSpot(ComkerSpotDPO.DEFAULT, "DEFAULT SPOT", "");
    }

    //==========================================================================
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUserDPO getOrCreateUser(String email, String username, String password, String fullname){
        ComkerUserDPO item = userDao.getByUsername(username);
        if (item == null) {
            item = new ComkerUserDPO(email, username, password, fullname);
            userDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUserDPO getUnknownUser(){
        return getOrCreateUser("unknown@cokkee.net", ComkerUserDPO.UNKNOWN, "ff808181442e048701442e04aa710008", "UNKNOWN USER");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUserDPO getDefaultUser(){
        return getOrCreateUser("default@cokkee.net", ComkerUserDPO.DEFAULT, "ff808181442e048701442e04aa610006", "DEFAULT USER");
    }

    //==========================================================================

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSettingKeyDPO getOrCreateSettingKey(String code, String type, String range) {
        ComkerSettingKeyDPO item = settingDao.getByCode(code);
        if (item == null) {
            item = new ComkerSettingKeyDPO(code, type, range);
            settingDao.create(item);
        }
        return item;
    }

    //==========================================================================

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerCrewDPO getOrCreateCrew(ComkerRoleDPO globalRole, String name, String description) {
        ComkerCrewDPO item = crewDao.getByName(name);
        if (item == null) {
            item = new ComkerCrewDPO(name, description);
            crewDao.create(item);
            crewDao.addGlobalRole(item, globalRole);
        } 
        return item;
    }
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerCrewDPO getOrCreateCrew(ComkerSpotDPO spot, ComkerRoleDPO role, String name, String description) {
        ComkerCrewDPO item = crewDao.getByName(name);
        if (item == null) {
            item = new ComkerCrewDPO(name, description);
            crewDao.update(item);
            crewDao.addRoleWithSpot(item, role, spot);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerCrewDPO getOrCreateCrew(ComkerSpotDPO spot, ComkerRoleDPO role) {
        Assert.notNull(spot, "Spot should be not null");
        Assert.notNull(role, "Role should be not null");

        return getOrCreateCrew(spot, role, role.getCode() + " group of [" + spot.getName() + "]", "");
    }

    //==========================================================================

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRoleDPO getOrCreateRole(String code, String name, String description, Boolean global) {
        ComkerRoleDPO item = roleDao.getByCode(code);
        if (item == null) {
            item = new ComkerRoleDPO(code, name, description, global);
            roleDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRoleDPO getAdministratorRole() {
        return getOrCreateRole("Administrator", "Quản trị hệ thống", "Quản quản trị hệ thống", Boolean.TRUE);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRoleDPO getManagerRole() {
        return getOrCreateRole("Manager", "Quản lý Spot", "Quản lý vùng hoạt động", Boolean.FALSE);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRoleDPO getMemberRole() {
        return getOrCreateRole("Member", "Thành viên Spot", "Thành viên vùng hoạt động", Boolean.FALSE);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRoleDPO getGuestRole() {
        return getOrCreateRole("Guest", "Thành viên tự do", "Thành viên không thuộc vùng hoạt động", Boolean.TRUE);
    }

    //==========================================================================

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerPermissionDPO getOrCreatePermission(String authority) {
        ComkerPermissionDPO item = permissionDao.getByAuthority(authority);
        if (item == null) {
            item = new ComkerPermissionDPO(authority);
            permissionDao.save(item);
        }
        return item;
    }

    //==========================================================================
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected void initComkerNavbars() {
        ComkerNavbarNodeDPO nodeRoot = new ComkerNavbarNodeDPO("__NAVBAR_ROOT__", null, 
                new String[] {"PERM_COMKER_GUEST"});
        nodeRoot = navbarDao.create(nodeRoot);

        ComkerNavbarNodeDPO parent = nodeRoot;
        ComkerNavbarNodeDPO node = new ComkerNavbarNodeDPO("home", "#", 
                new String[] {"PERM_COMKER_GUEST"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        // Administration sub-menu
        parent = nodeRoot;
        node = new ComkerNavbarNodeDPO("administration", "#administration",
                new String[] {
                    "PERM_COMKER_ADM_USER",
                    "PERM_COMKER_ADM_CREW",
                    "PERM_COMKER_ADM_SPOT"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        parent = node;

        node = new ComkerNavbarNodeDPO("administration.userList", "#user/list",
                new String[] {
                    "PERM_COMKER_MOD_USER",
                    "PERM_COMKER_ADM_USER"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        node = new ComkerNavbarNodeDPO("administration.crewList", "#crew/list",
                new String[] {
                    "PERM_COMKER_MOD_CREW",
                    "PERM_COMKER_ADM_CREW"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        // Tools sub-menu
    }
}