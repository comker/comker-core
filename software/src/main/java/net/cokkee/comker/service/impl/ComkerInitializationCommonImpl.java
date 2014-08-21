package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.*;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSettingDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerNavbarNode;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
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


    protected MessageDigestPasswordEncoder passwordEncoder = null;

    public void setPasswordEncoder(MessageDigestPasswordEncoder passwordEncoder) {
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
    public void initDefaultSpot(ComkerSpot spot) {

        if (spot == null) return;

        ComkerSpot spotDefault = getDefaultSpot();
        ComkerUser userUnknown = getUnknownUser();
        ComkerUser userDefault = getDefaultUser();

        // -- init spot settings
        ComkerSettingKey key = null;
        String value = null;

        key = getOrCreateSettingKey(ComkerSettingKey.CODE_SPOT_THEME, ComkerSettingKey.TYPE_STRING, "");
        value = settingDao.getValue(spotDefault, userUnknown, key);
        settingDao.define(spot, userUnknown, key, value);

        key = getOrCreateSettingKey(ComkerSettingKey.CODE_USER_LANGUAGE, ComkerSettingKey.TYPE_STRING, "");
        value = settingDao.getValue(spotDefault, userDefault, key);
        settingDao.define(spot, userDefault, key, value);

        // -- init crews (groups)
        ComkerRole roleMgr = getManagerRole();
        ComkerCrew crewMgrs = getOrCreateCrew(spot, roleMgr);

        ComkerRole roleMbr = getMemberRole();
        ComkerCrew crewMbrs = getOrCreateCrew(spot, roleMbr);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void initDefaultUser(ComkerUser user, ComkerSpot spot, ComkerRole role) {

        if (user == null) return;
        ComkerUser userDefault = getDefaultUser();

        if (spot == null) {
            spot = getDefaultSpot();
        }

        if (role == null) {
            role = getMemberRole();
        }

        // init user settings
        ComkerSettingKey key = null;
        String value = null;

        key = getOrCreateSettingKey(ComkerSettingKey.CODE_USER_LANGUAGE, ComkerSettingKey.TYPE_STRING, "");
        value = settingDao.getValue(spot, userDefault, key);
        settingDao.define(spot, user, key, value);

        // init user group
        ComkerCrew crew = getOrCreateCrew(spot, role);
        userDao.addCrew(user, crew);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpot getOrCreateSpot(String code, String name, String description) {
        ComkerSpot item = spotDao.getByCode(code);
        if (item == null) {
            item = new ComkerSpot(code, name, description);
            spotDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpot getUnknownSpot() {
        return getOrCreateSpot(ComkerSpot.UNKNOWN, "UNKNOWN SPOT", "");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSpot getDefaultSpot() {
        return getOrCreateSpot(ComkerSpot.DEFAULT, "DEFAULT SPOT", "");
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUser getOrCreateUser(String email, String username, String password, String fullname){
        ComkerUser item = userDao.getByUsername(username);
        if (item == null) {
            item = new ComkerUser(email, username, password, fullname);
            userDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUser getUnknownUser(){
        return getOrCreateUser("unknown@cokkee.net", ComkerUser.UNKNOWN, "ff808181442e048701442e04aa710008", "UNKNOWN USER");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerUser getDefaultUser(){
        return getOrCreateUser("default@cokkee.net", ComkerUser.DEFAULT, "ff808181442e048701442e04aa610006", "DEFAULT USER");
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerSettingKey getOrCreateSettingKey(String code, String type, String range) {
        ComkerSettingKey item = settingDao.getByCode(code);
        if (item == null) {
            item = new ComkerSettingKey(code, type, range);
            settingDao.create(item);
        }
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerCrew getOrCreateCrew(ComkerSpot spot, ComkerRole role, String name, String description) {
        ComkerCrew item = crewDao.getBySpotWithRole(spot, role);
        if (item == null) {
            item = new ComkerCrew(name, description);
            crewDao.save(item);
            crewDao.addRoleWithSpot(item, role, spot);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerCrew getOrCreateCrew(ComkerSpot spot, ComkerRole role) {
        Assert.notNull(spot, "Spot should be not null");
        Assert.notNull(role, "Role should be not null");

        ComkerCrew item = crewDao.getBySpotWithRole(spot, role);
        if (item == null) {
            item = new ComkerCrew(role.getCode() + " group of [" + spot.getName() + "]", "");
            crewDao.save(item);
            crewDao.addRoleWithSpot(item, role, spot);
        }
        return item;
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRole getOrCreateRole(String code, String name, String description) {
        ComkerRole item = roleDao.getByCode(code);
        if (item == null) {
            item = new ComkerRole(code, name, description);
            roleDao.create(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRole getAdministratorRole() {
        return getOrCreateRole("Administrator", "Quản trị hệ thống", "Quản quản trị hệ thống");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRole getManagerRole() {
        return getOrCreateRole("Manager", "Quản lý Spot", "Quản lý vùng hoạt động");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRole getMemberRole() {
        return getOrCreateRole("Member", "Thành viên Spot", "Thành viên vùng hoạt động");
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerRole getGuestRole() {
        return getOrCreateRole("Guest", "Thành viên tự do", "Thành viên không thuộc vùng hoạt động");
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected ComkerPermission getOrCreatePermission(String authority) {
        ComkerPermission item = permissionDao.getByAuthority(authority);
        if (item == null) {
            item = new ComkerPermission(authority);
            permissionDao.save(item);
        }
        return item;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    protected void initComkerNavbars() {
        ComkerNavbarNode nodeRoot = new ComkerNavbarNode("__NAVBAR_ROOT__", null, 
                new String[] {"PERM_COMKER_GUEST"});
        nodeRoot = navbarDao.create(nodeRoot);

        ComkerNavbarNode parent = nodeRoot;
        ComkerNavbarNode node = new ComkerNavbarNode("home", "#", 
                new String[] {"PERM_COMKER_GUEST"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        // Administration sub-menu
        parent = nodeRoot;
        node = new ComkerNavbarNode("administration", "#administration",
                new String[] {
                    "PERM_COMKER_ADM_USER",
                    "PERM_COMKER_ADM_CREW",
                    "PERM_COMKER_ADM_SPOT"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        parent = node;

        node = new ComkerNavbarNode("administration.userList", "#user/list",
                new String[] {
                    "PERM_COMKER_MOD_USER",
                    "PERM_COMKER_ADM_USER"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        node = new ComkerNavbarNode("administration.crewList", "#crew/list",
                new String[] {
                    "PERM_COMKER_MOD_CREW",
                    "PERM_COMKER_ADM_CREW"});
        parent.getChildren().add(node);
        node.setParent(parent);
        node = navbarDao.create(node);

        // Tools sub-menu
    }
}