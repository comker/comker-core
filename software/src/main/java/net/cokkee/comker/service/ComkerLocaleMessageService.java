package net.cokkee.comker.service;

/**
 *
 * @author drupalex
 */
public interface ComkerLocaleMessageService {

    String getMessage (String code, String defaultMessage);

    String getMessage (String code, Object[] args, String defaultMessage);
}
