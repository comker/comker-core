package net.cokkee.comker.service;

import net.cokkee.comker.model.ComkerUserDetails;

/**
 *
 * @author drupalex
 */
public interface ComkerSecurityContextReader {

    String getUsername();
    
    ComkerUserDetails getUserDetails();
}
