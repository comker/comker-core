package net.cokkee.comker.storage;

import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.model.msg.ComkerInformationResponse;

/**
 *
 * @author drupalex
 */
public interface ComkerRegistrationStorage {

    ComkerInformationResponse register(ComkerRegistrationDTO form);

    int confirm(String code);
}
