package net.cokkee.comker.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author drupalex
 */
public interface ComkerSecurityService {
    
    UserDetails getUserDetails();

    UserDetails loadUserDetails(String username, String spotCode)
            throws UsernameNotFoundException, DataAccessException;

    void reloadUserDetails(String spotCode);

    void changePassword(String password, String encodedPassword);
}
