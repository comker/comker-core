package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.*;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerSettingKeyDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
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
        ComkerSpotDPO spotUnknown = getUnknownSpot();
        ComkerSpotDPO spotDefault = getDefaultSpot();

        // init seed users
        ComkerUserDPO userUnknown = getUnknownUser();
        ComkerUserDPO userDefault = getDefaultUser();

        // init settings
        ComkerSettingKeyDPO sKey;

        // -- init Application settings
        sKey = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_APPLICATION_TITLE, ComkerSettingKeyDPO.TYPE_STRING, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "Application Title");

        sKey = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_APPLICATION_MESSAGE, ComkerSettingKeyDPO.TYPE_STRING, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "Welcome to Application");

        sKey = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_APPLICATION_PAGER_SIZE, ComkerSettingKeyDPO.TYPE_LIST, "");
        settingDao.define(spotUnknown, userUnknown, sKey, "10,50,100");

        // -- init Default spot settings
        sKey = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_SPOT_THEME, ComkerSettingKeyDPO.TYPE_STRING, "");
        settingDao.define(spotDefault, userUnknown, sKey, "cokkee");

        // -- init Default spot/user settings
        sKey = getOrCreateSettingKey(ComkerSettingKeyDPO.CODE_USER_LANGUAGE, ComkerSettingKeyDPO.TYPE_STRING, "");
        settingDao.define(spotDefault, userDefault, sKey, "vi");

        // init permissions
        ComkerPermissionDPO permAllSysa = getOrCreatePermission("PERM_COMKER_SYSA");
        ComkerPermissionDPO permAllUser = getOrCreatePermission("PERM_COMKER_USER");
        ComkerPermissionDPO permAllTest = getOrCreatePermission("PERM_COMKER_TEST");

        // init roles
        ComkerRoleDPO roleAdm = getAdministratorRole();

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

        ComkerRoleDPO roleMgr = getManagerRole();

        ComkerRoleDPO roleMbr = getMemberRole();

        ComkerRoleDPO roleGst = getGuestRole();

        // init crews
        ComkerCrewDPO crewAdmins = getOrCreateCrew(roleAdm, "Administrators", "Administrator Group");
        ComkerCrewDPO crewGuests = getOrCreateCrew(roleGst, "Guests", "Guest Group");

        // init users
        ComkerUserDPO userAdmin = getOrCreateUser(
                "admin@cokkee.net",
                "admin",
                passwordEncoder.encode("dobietday"),
                "Administrator");
        userDao.addCrew(userAdmin, crewAdmins);

        ComkerUserDPO userGuest = getOrCreateUser(
                "guest@cokkee.net",
                "guest",
                passwordEncoder.encode("nopassword"),
                "Guest");
        userDao.addCrew(userGuest, crewGuests);

        //this.initComkerNavbars();
    }
}