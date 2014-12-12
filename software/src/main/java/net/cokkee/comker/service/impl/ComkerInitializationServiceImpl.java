package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.*;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSettingKey;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerInitializationServiceImpl extends ComkerInitializationCommonImpl
        implements ComkerInitializationService {

    private final Logger log = LoggerFactory.getLogger(ComkerInitializationServiceImpl.class);

    //----------------------------------------------------------------------

    public ComkerInitializationServiceImpl() {
        super();
        if (log.isDebugEnabled()) {
            log.debug("ComkerInitializationServiceImpl.constructor() - create Comker* objects");
        }
    }

    //----------------------------------------------------------------------

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("ComkerInitializationService.init() - start");
        }

        // init seed spots
        ComkerSpot spotUnknown = getUnknownSpot();
        ComkerSpot spotDefault = getDefaultSpot();

        // init seed users
        ComkerUser userUnknown = getUnknownUser();
        ComkerUser userDefault = getDefaultUser();

        // init settings
        ComkerSettingKey sKey;

        // -- init Application settings
        sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_TITLE, ComkerSettingKey.TYPE_STRING, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "Application Title");

        sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_MESSAGE, ComkerSettingKey.TYPE_STRING, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "Welcome to Application");

        sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_APPLICATION_PAGER_SIZE, ComkerSettingKey.TYPE_LIST, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "10,50,100");

        // -- init Default spot settings
        sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_SPOT_THEME, ComkerSettingKey.TYPE_STRING, "");
        settingDao.define(spotDefault, userUnknown, sKey, "cokkee");

        // -- init Default spot/user settings
        sKey = getOrCreateSettingKey(ComkerSettingKey.CODE_USER_LANGUAGE, ComkerSettingKey.TYPE_STRING, "");
        settingDao.define(spotDefault, userDefault, sKey, "vi");

        // init permissions
        ComkerPermission permAllSysa = getOrCreatePermission("PERM_COMKER_SYSA");
        ComkerPermission permAllUser = getOrCreatePermission("PERM_COMKER_USER");
        ComkerPermission permAllTest = getOrCreatePermission("PERM_COMKER_TEST");

        // init roles
        ComkerRole roleAdm = getAdministratorRole();

        String[] permStrs = new String[] {
            "PERM_COMKER_MODULE_L",
            "PERM_COMKER_MODULE_R",
            
            "PERM_COMKER_SPOT_L",
            "PERM_COMKER_SPOT_R",
            "PERM_COMKER_SPOT_W",
            "PERM_COMKER_SPOT_W",
            "PERM_COMKER_SPOT_D",
            
            "PERM_COMKER_PERMISSION_L",
            "PERM_COMKER_PERMISSION_R",
            
            "PERM_COMKER_ROLE_L",
            "PERM_COMKER_ROLE_R",
            "PERM_COMKER_ROLE_W",
            "PERM_COMKER_ROLE_W",
            "PERM_COMKER_ROLE_D",
            
            "PERM_COMKER_CREW_L",
            "PERM_COMKER_CREW_R",
            "PERM_COMKER_CREW_W",
            "PERM_COMKER_CREW_W",
            "PERM_COMKER_CREW_D",
            
            "PERM_COMKER_USER_L",
            "PERM_COMKER_USER_R",
            "PERM_COMKER_USER_W",
            "PERM_COMKER_USER_W",
            "PERM_COMKER_USER_D",
            
            "PERM_COMKER_WATCHDOG_L",
            "PERM_COMKER_WATCHDOG_R",
        };
        
        for(String permStr: permStrs) {
            roleDao.addPermission(roleAdm, getOrCreatePermission(permStr));
        }

        ComkerRole roleMgr = getManagerRole();

        ComkerRole roleMbr = getMemberRole();

        ComkerRole roleGst = getGuestRole();

        // init crews
        ComkerCrew crewAdmins = getOrCreateCrew(roleAdm, "Administrators", "Administrator Group");
        ComkerCrew crewGuests = getOrCreateCrew(roleGst, "Guests", "Guest Group");

        // init users
        ComkerUser userAdmin = getOrCreateUser(
                "admin@cokkee.net",
                "admin",
                passwordEncoder.encodePassword("dobietday", null),
                "Administrator");
        userDao.addCrew(userAdmin, crewAdmins);

        ComkerUser userGuest = getOrCreateUser(
                "guest@cokkee.net",
                "guest",
                passwordEncoder.encodePassword("nopassword", null),
                "Guest");
        userDao.addCrew(userGuest, crewGuests);

        //this.initComkerNavbars();
    }
}