package net.cokkee.comker.service.impl;

import net.cokkee.comker.service.*;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
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
public class ComkerInitializationSampleData extends ComkerInitializationCommonImpl
        implements ComkerInitializationService {

    private final Logger log = LoggerFactory.getLogger(ComkerInitializationSampleData.class);

    //----------------------------------------------------------------------

    public ComkerInitializationSampleData() {
        super();
        if (log.isDebugEnabled()) {
            log.debug("ComkerInitializationSampleData.constructor() - create Comker* objects");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void init() {
        
        if (log.isDebugEnabled()) {
            log.debug("ComkerInitializationSampleData.init() - start");
        }

        ComkerRoleDPO roleMgr = getManagerRole();
        ComkerRoleDPO roleMbr = getMemberRole();

        ComkerSpotDPO spotBnho = getOrCreateSpot("BUOCNHO_COM", "Buocnho Training & Technology", "");
        initDefaultSpot(spotBnho);

        ComkerUserDPO userBnho0 = getOrCreateUser(
                "manager@buocnho.com",
                "BNA00000",
                passwordEncoder.encodePassword("dobietday", null),
                "Buocnho Manager");
        initDefaultUser(userBnho0, spotBnho, roleMgr);

        ComkerUserDPO userBnho1 = getOrCreateUser(
                "member1@buocnho.com",
                "BNA00001",
                passwordEncoder.encodePassword("nopassword", null),
                "Buocnho Member One");
        initDefaultUser(userBnho1, spotBnho, roleMbr);

        ComkerUserDPO userBnho2 = getOrCreateUser(
                "member2@buocnho.com",
                "BNA00002",
                passwordEncoder.encodePassword("nopassword", null),
                "Buocnho Member Two");
        initDefaultUser(userBnho2, spotBnho, roleMbr);

        ComkerSpotDPO spotPctu = getOrCreateSpot("PCTU_EDU_VN", "Đại học Phan Châu Trinh - Hội An", "");
        initDefaultSpot(spotPctu);

        ComkerUserDPO userPctu0 = getOrCreateUser(
                "manager@pctu.edu.vn",
                "PCT00000",
                passwordEncoder.encodePassword("dobietday", null),
                "Pctu Manager");
        initDefaultUser(userPctu0, spotPctu, roleMgr);

        ComkerUserDPO userPctu1 = getOrCreateUser(
                "member1@pctu.edu.vn",
                "PCT00001",
                passwordEncoder.encodePassword("nopassword", null),
                "Pctu Member One");
        initDefaultUser(userPctu1, spotPctu, roleMbr);

        ComkerUserDPO userPctu2 = getOrCreateUser(
                "member2@pctu.edu.vn",
                "PCT00002",
                passwordEncoder.encodePassword("nopassword", null),
                "Pctu Member Two");
        initDefaultUser(userPctu2, spotPctu, roleMbr);
    }
}