package net.cokkee.comker.service;

import net.cokkee.comker.model.msg.ComkerMailAddress;
import net.cokkee.comker.model.msg.ComkerMailMessage;

/**
 *
 * @author drupalex
 */
public interface ComkerMailingService {
    void sendMail(ComkerMailAddress address, ComkerMailMessage message);
}
