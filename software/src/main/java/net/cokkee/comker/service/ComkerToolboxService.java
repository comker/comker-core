package net.cokkee.comker.service;

import java.util.Date;

/**
 *
 * @author drupalex
 */
public interface ComkerToolboxService {

    String convertEntityToJson(Object entity);

    Date getTime();

    long getTimeInMillis();
}
