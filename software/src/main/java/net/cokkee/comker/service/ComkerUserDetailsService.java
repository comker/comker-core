package net.cokkee.comker.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author drupalex
 */
public class ComkerUserDetailsService implements UserDetailsService {

    private static Logger log = LoggerFactory.getLogger(ComkerUserDetailsService.class);
    
    private ComkerSecurityService securityService = null;

    public ComkerSecurityService getSecurityService() {
        return securityService;
    }

    public void setSecurityService(ComkerSecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        return getSecurityService().loadUserDetails(username, null);
    }
}
