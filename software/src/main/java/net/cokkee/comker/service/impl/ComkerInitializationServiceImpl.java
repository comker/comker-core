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

        ComkerPermission permAdmUser = getOrCreatePermission("PERM_COMKER_ADM_USER");
        ComkerPermission permAdmSpot = getOrCreatePermission("PERM_COMKER_ADM_SPOT");
        ComkerPermission permAdmCrew = getOrCreatePermission("PERM_COMKER_ADM_CREW");
        ComkerPermission permAdmRole = getOrCreatePermission("PERM_COMKER_ADM_ROLE");
        ComkerPermission permAdmPermission = getOrCreatePermission("PERM_COMKER_ADM_PERMISSION");
        ComkerPermission permAdmSetting = getOrCreatePermission("PERM_COMKER_ADM_SETTING");

        // init roles
        ComkerRole roleAdm = getAdministratorRole();
        roleDao.addPermission(roleAdm, permAdmUser);
        roleDao.addPermission(roleAdm, permAdmSpot);
        roleDao.addPermission(roleAdm, permAdmCrew);
        roleDao.addPermission(roleAdm, permAdmRole);
        roleDao.addPermission(roleAdm, permAdmPermission);
        roleDao.addPermission(roleAdm, permAdmSetting);

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