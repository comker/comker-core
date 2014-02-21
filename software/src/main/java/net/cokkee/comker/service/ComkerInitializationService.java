package net.cokkee.comker.service;

import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;

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
}
