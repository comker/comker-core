package net.cokkee.comker.service;

import net.cokkee.comker.model.ComkerUserDetails;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author drupalex
 */
public interface ComkerSecurityService {
    
    ComkerUserDetails getUserDetails();

    ComkerUserDetails loadUserDetails(String username, String spotCode)
            throws UsernameNotFoundException, DataAccessException;

    void reloadUserDetails(String spotCode);

    void changePassword(String password, String encodedPassword);
}
