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

        ComkerSpotDPO spotBnho = getOrCreateSpot("SAMPLE_SPOT_1", "Sample Spot/Site #1", "");
        initDefaultSpot(spotBnho);

        ComkerUserDPO userBnho0 = getOrCreateUser(
                "manager@site1.cokkee.net",
                "SPOT100000",
                passwordEncoder.encode("dobietday"),
                "Sample One Manager");
        initDefaultUser(userBnho0, spotBnho, roleMgr);

        ComkerUserDPO userBnho1 = getOrCreateUser(
                "member1@site1.cokkee.net",
                "SPOT100001",
                passwordEncoder.encode("nopassword"),
                "Sample One Member One");
        initDefaultUser(userBnho1, spotBnho, roleMbr);

        ComkerUserDPO userBnho2 = getOrCreateUser(
                "member2@site1.cokkee.net",
                "SPOT100002",
                passwordEncoder.encode("nopassword"),
                "Sample One Member Two");
        initDefaultUser(userBnho2, spotBnho, roleMbr);

        ComkerSpotDPO spotPctu = getOrCreateSpot("SAMPLE_SPOT_2", "Sample Spot/Site #2", "");
        initDefaultSpot(spotPctu);

        ComkerUserDPO userPctu0 = getOrCreateUser(
                "manager@site2.cokkee.net",
                "SPOT200000",
                passwordEncoder.encode("dobietday"),
                "Sample Two Manager");
        initDefaultUser(userPctu0, spotPctu, roleMgr);

        ComkerUserDPO userPctu1 = getOrCreateUser(
                "member1@site2.cokkee.net",
                "SPOT200001",
                passwordEncoder.encode("nopassword"),
                "Sample Two Member One");
        initDefaultUser(userPctu1, spotPctu, roleMbr);

        ComkerUserDPO userPctu2 = getOrCreateUser(
                "member2@site2.cokkee.net",
                "SPOT200002",
                passwordEncoder.encode("nopassword"),
                "Sample Two Member Two");
        initDefaultUser(userPctu2, spotPctu, roleMbr);
    }
}