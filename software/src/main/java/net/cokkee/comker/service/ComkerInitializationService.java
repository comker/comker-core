package net.cokkee.comker.service;

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
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 *
 * @author drupalex
 */
public interface ComkerInitializationService {

    void initComkerApplication();

    void initDefaultSpot(ComkerSpot spot);

    void initDefaultUser(ComkerUser user, ComkerSpot spot, ComkerRole role);
    
    void initSampleApplication();

    void initDemonstrationData();

    public static class Impl implements ComkerInitializationService {

        private final Logger log = LoggerFactory.getLogger(Impl.class);

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

        private ComkerSettingDao settingDao = null;

        public ComkerSettingDao getSettingDao() {
            return settingDao;
        }

        public void setSettingDao(ComkerSettingDao settingDao) {
            this.settingDao = settingDao;
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

        
        private ComkerNavbarDao navbarDao = null;

        public ComkerNavbarDao getNavbarDao() {
            return navbarDao;
        }

        public void setNavbarDao(ComkerNavbarDao navbarDao) {
            this.navbarDao = navbarDao;
        }

        
        private MessageDigestPasswordEncoder passwordEncoder = null;

        public MessageDigestPasswordEncoder getPasswordEncoder() {
            return passwordEncoder;
        }

        public void setPasswordEncoder(MessageDigestPasswordEncoder passwordEncoder) {
            this.passwordEncoder = passwordEncoder;
        }
        
        //----------------------------------------------------------------------

        public Impl() {
            if (log.isDebugEnabled()) {
                log.debug("ComkerInitializationService.constructor() - create Comker* objects");
            }
        }

        //----------------------------------------------------------------------

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void initComkerApplication() {
            if (log.isDebugEnabled()) {
                log.debug("ComkerInitializationService.initComkerApplication() - start");
            }

            // init seed spots
            ComkerSpot spotUnknown = getUnknownSpot();
            ComkerSpot spotDefault = getDefaultSpot();

            // init seed users
            ComkerUser userUnknown = getUnknownUser();
            ComkerUser userDefault = getDefaultUser();

            // init settings
            ComkerSettingKey sKey = null;

            // -- init Application settings
            sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_TITLE, ComkerSettingKey.TYPE_STRING, "");
            getSettingDao().define(spotUnknown, userUnknown, sKey, "Application Title");

            sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_MESSAGE, ComkerSettingKey.TYPE_STRING, "");
            getSettingDao().define(spotUnknown, userUnknown, sKey, "Welcome to Application");

            sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_PAGER_SIZE, ComkerSettingKey.TYPE_LIST, "");
            getSettingDao().define(spotUnknown, userUnknown, sKey, "10,50,100");

            // -- init Default spot settings
            sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_SPOT_THEME, ComkerSettingKey.TYPE_STRING, "");
            getSettingDao().define(spotDefault, userUnknown, sKey, "cokkee");

            // -- init Default spot/user settings
            sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_USER_LANGUAGE, ComkerSettingKey.TYPE_STRING, "");
            getSettingDao().define(spotDefault, userDefault, sKey, "vi");

            // init permissions
            ComkerPermission permAllSysa = getOrCreatePermission("PERM_COMKER_SYSA");
            ComkerPermission permAllUser = getOrCreatePermission("PERM_COMKER_USER");
            ComkerPermission permAllTest = getOrCreatePermission("PERM_COMKER_TEST");

            ComkerPermission permAdmUser = getOrCreatePermission("PERM_COMKER_ADM_USER");
            ComkerPermission permAdmSpot = getOrCreatePermission("PERM_COMKER_ADM_SPOT");
            ComkerPermission permAdmCrew = getOrCreatePermission("PERM_COMKER_ADM_CREW");
            ComkerPermission permAdmRole = getOrCreatePermission("PERM_COMKER_ADM_ROLE");
            ComkerPermission permAdmPermission = getOrCreatePermission("PERM_COMKER_ADM_PERMISSION");
            ComkerPermission permAdmSetting = getOrCreatePermission("PERM_COMKER_ADM_SETTING");

            // init roles
            ComkerRole roleAdm = getAdministratorRole();
            getRoleDao().addPermission(roleAdm, permAdmUser);
            getRoleDao().addPermission(roleAdm, permAdmSpot);
            getRoleDao().addPermission(roleAdm, permAdmCrew);
            getRoleDao().addPermission(roleAdm, permAdmRole);
            getRoleDao().addPermission(roleAdm, permAdmPermission);
            getRoleDao().addPermission(roleAdm, permAdmSetting);
            
            ComkerRole roleMgr = getManagerRole();
            
            ComkerRole roleMbr = getMemberRole();

            ComkerRole roleGst = getGuestRole();

            // init crews
            ComkerCrew crewAdmins = getOrCreateCrew(spotUnknown, roleAdm, "Administrators", "Administrator Group");
            ComkerCrew crewGuests = getOrCreateCrew(spotUnknown, roleGst, "Guests", "Guest Group");

            // init users
            ComkerUser userAdmin = getOrCreateUser(
                    "admin@cokkee.net",
                    "admin",
                    getPasswordEncoder().encodePassword("dobietday", null),
                    "Administrator");
            getUserDao().addCrew(userAdmin, crewAdmins);

            ComkerUser userGuest = getOrCreateUser(
                    "guest@cokkee.net",
                    "guest",
                    getPasswordEncoder().encodePassword("nopassword", null),
                    "Guest");
            getUserDao().addCrew(userGuest, crewGuests);
        }

        @Override
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
            value = getSettingDao().getValue(spotDefault, userUnknown, key);
            getSettingDao().define(spot, userUnknown, key, value);

            key = getOrCreateSettingKey(ComkerSettingKey.CODE_USER_LANGUAGE, ComkerSettingKey.TYPE_STRING, "");
            value = getSettingDao().getValue(spotDefault, userDefault, key);
            getSettingDao().define(spot, userDefault, key, value);

            // -- init crews (groups)
            ComkerRole roleMgr = getManagerRole();
            ComkerCrew crewMgrs = getOrCreateCrew(spot, roleMgr);
            
            ComkerRole roleMbr = getMemberRole();
            ComkerCrew crewMbrs = getOrCreateCrew(spot, roleMbr);
        }

        @Override
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
            value = getSettingDao().getValue(spot, userDefault, key);
            getSettingDao().define(spot, user, key, value);

            // init user group
            ComkerCrew crew = getOrCreateCrew(spot, role);
            getUserDao().addCrew(user, crew);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void initSampleApplication() {
            if (log.isDebugEnabled()) {
                log.debug("ComkerInitializationService.initSampleApplication() - start");
            }

            // appends extended permissions
            ComkerPermission permSmpPlanOwner = getOrCreatePermission("PERM_SAMPLE_PLAN_OWNER");
            ComkerPermission permSmpPlanShared = getOrCreatePermission("PERM_SAMPLE_PLAN_SHARED");

            // appends extended roles
            ComkerRole roleMgr = getManagerRole();

            ComkerRole roleMbr = getMemberRole();
            getRoleDao().addPermission(roleMbr, permSmpPlanOwner);
            getRoleDao().addPermission(roleMbr, permSmpPlanShared);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void initDemonstrationData() {
            if (log.isDebugEnabled()) {
                log.debug("ComkerInitializationService.initDemonstrationData() - start");
            }

            ComkerRole roleMgr = getManagerRole();
            ComkerRole roleMbr = getMemberRole();

            ComkerSpot spotBnho = getOrCreateSpot("buocnho.com", "Buocnho Training & Technology", "");
            initDefaultSpot(spotBnho);

            ComkerUser userBnho0 = getOrCreateUser(
                    "manager@buocnho.com",
                    "BNA00000",
                    getPasswordEncoder().encodePassword("dobietday", null),
                    "Buocnho Manager");
            initDefaultUser(userBnho0, spotBnho, roleMgr);

            ComkerUser userBnho1 = getOrCreateUser(
                    "member1@buocnho.com",
                    "BNA00001",
                    getPasswordEncoder().encodePassword("nopassword", null),
                    "Buocnho Member One");
            initDefaultUser(userBnho1, spotBnho, roleMbr);

            ComkerUser userBnho2 = getOrCreateUser(
                    "member2@buocnho.com",
                    "BNA00002",
                    getPasswordEncoder().encodePassword("nopassword", null),
                    "Buocnho Member Two");
            initDefaultUser(userBnho2, spotBnho, roleMbr);

            ComkerSpot spotPctu = getOrCreateSpot("pctu.edu.vn", "Đại học Phan Châu Trinh - Hội An", "");
            initDefaultSpot(spotPctu);

            ComkerUser userPctu0 = getOrCreateUser(
                    "manager@pctu.edu.vn",
                    "PCT00000",
                    getPasswordEncoder().encodePassword("dobietday", null),
                    "Pctu Manager");
            initDefaultUser(userPctu0, spotPctu, roleMgr);

            ComkerUser userPctu1 = getOrCreateUser(
                    "member1@pctu.edu.vn",
                    "PCT00001",
                    getPasswordEncoder().encodePassword("nopassword", null),
                    "Pctu Member One");
            initDefaultUser(userPctu1, spotPctu, roleMbr);

            ComkerUser userPctu2 = getOrCreateUser(
                    "member2@pctu.edu.vn",
                    "PCT00002",
                    getPasswordEncoder().encodePassword("nopassword", null),
                    "Pctu Member Two");
            initDefaultUser(userPctu2, spotPctu, roleMbr);
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerSpot getOrCreateSpot(String code, String name, String description) {
            ComkerSpot item = getSpotDao().getByCode(code);
            if (item == null) {
                item = new ComkerSpot(code, name, description);
                getSpotDao().create(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerSpot getUnknownSpot() {
            return getOrCreateSpot(ComkerSpot.UNKNOWN, "UNKNOWN SPOT", "");
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerSpot getDefaultSpot() {
            return getOrCreateSpot(ComkerSpot.DEFAULT, "DEFAULT SPOT", "");
        }

        
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerUser getOrCreateUser(String email, String username, String password, String fullname){
            ComkerUser item = getUserDao().getByUsername(username);
            if (item == null) {
                item = new ComkerUser(email, username, password, fullname);
                getUserDao().create(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerUser getUnknownUser(){
            return getOrCreateUser("unknown@cokkee.net", ComkerUser.UNKNOWN, "ff808181442e048701442e04aa710008", "UNKNOWN USER");
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerUser getDefaultUser(){
            return getOrCreateUser("default@cokkee.net", ComkerUser.DEFAULT, "ff808181442e048701442e04aa610006", "DEFAULT USER");
        }


        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerSettingKey getOrCreateSettingKey(String code, String type, String range) {
            ComkerSettingKey item = getSettingDao().getByCode(code);
            if (item == null) {
                item = new ComkerSettingKey(code, type, range);
                getSettingDao().create(item);
            }
            return item;
        }


        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerCrew getOrCreateCrew(ComkerSpot spot, ComkerRole role, String name, String description) {
            ComkerCrew item = getCrewDao().getBySpotWithRole(spot, role);
            if (item == null) {
                item = new ComkerCrew(name, description);
                getCrewDao().save(item);
                getCrewDao().addRoleWithSpot(item, role, spot);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerCrew getOrCreateCrew(ComkerSpot spot, ComkerRole role) {
            Assert.notNull(spot, "Spot should be not null");
            Assert.notNull(role, "Role should be not null");

            ComkerCrew item = getCrewDao().getBySpotWithRole(spot, role);
            if (item == null) {
                item = new ComkerCrew(role.getCode() + " group of [" + spot.getName() + "]", "");
                getCrewDao().save(item);
                getCrewDao().addRoleWithSpot(item, role, spot);
            }
            return item;
        }

        
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getOrCreateRole(String code, String name, String description) {
            ComkerRole item = getRoleDao().getByCode(code);
            if (item == null) {
                item = new ComkerRole(code, name, description);
                getRoleDao().create(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getAdministratorRole() {
            return getOrCreateRole("Administrator", "Quản trị hệ thống", "Quản quản trị hệ thống");
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getManagerRole() {
            return getOrCreateRole("Manager", "Quản lý Spot", "Quản lý vùng hoạt động");
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getMemberRole() {
            return getOrCreateRole("Member", "Thành viên Spot", "Thành viên vùng hoạt động");
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getGuestRole() {
            return getOrCreateRole("Guest", "Thành viên tự do", "Thành viên không thuộc vùng hoạt động");
        }

        
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerPermission getOrCreatePermission(String authority) {
            ComkerPermission item = getPermissionDao().getByAuthority(authority);
            if (item == null) {
                item = new ComkerPermission(authority);
                getPermissionDao().save(item);
            }
            return item;
        }

        
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private void initOriginNavbars() {
            ComkerNavbarNode nodeRoot = new ComkerNavbarNode("__NAVBAR_ROOT__", null, null, null);
            getNavbarDao().create(nodeRoot);

            ComkerNavbarNode parent = nodeRoot;
            ComkerNavbarNode node = new ComkerNavbarNode("home", "#", null, null);
            node.changeParent(parent);
            getNavbarDao().create(node);

            // Administration sub-menu
            parent = nodeRoot;
            node = new ComkerNavbarNode("administration", "#administration", null, null);
            node.changeParent(parent);
            getNavbarDao().create(node);

            parent = node;

            node = new ComkerNavbarNode("administration.userList", "#user/list", null, null);
            node.changeParent(parent);
            getNavbarDao().create(node);

            node = new ComkerNavbarNode("administration.crewList", "#crew/list", null, null);
            node.changeParent(parent);
            getNavbarDao().create(node);

            // Tools sub-menu
        }
    }
}
