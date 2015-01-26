package net.cokkee.comker.service.impl;

import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityContextHolder;
import net.cokkee.comker.service.ComkerSecurityContextReader;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

/**
 *
 * @author drupalex
 */
public class ComkerSecurityContextReaderImpl implements ComkerSecurityContextReader {
    
    private ComkerSecurityContextHolder securityContextHolder;

    public void setsecurityContextHolder(ComkerSecurityContextHolder securityContextHolder) {
        this.securityContextHolder = securityContextHolder;
    }
    
    @Override
    public String getUsername() {
        ComkerUserDetails userDetails = getUserDetails();
        if (userDetails == null) return null;
        return userDetails.getUsername();
    }

    @Override
    public ComkerUserDetails getUserDetails() {
        SecurityContext context = securityContextHolder.getContext();
        if (context == null) return null;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return null;
        Object user = authentication.getPrincipal();
        if (user instanceof ComkerUserDetails) return (ComkerUserDetails) user;
        return null;
    }
}
