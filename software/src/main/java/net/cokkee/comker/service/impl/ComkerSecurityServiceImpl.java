package net.cokkee.comker.service.impl;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.exception.ComkerForbiddenAccessException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSecurityContextHolder;
import net.cokkee.comker.service.ComkerSecurityContextReader;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.storage.ComkerUserStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerSecurityServiceImpl implements ComkerSecurityService {

    private final Logger log = LoggerFactory.getLogger(ComkerSecurityServiceImpl.class);

    private ComkerUserStorage userStorage = null;

    public void setUserStorage(ComkerUserStorage userStorage) {
        this.userStorage = userStorage;
    }
    
    private ComkerSecurityContextHolder securityContextHolder = null;

    public void setsecurityContextHolder(ComkerSecurityContextHolder securityContextHolder) {
        this.securityContextHolder = securityContextHolder;
    }
    
    private ComkerSecurityContextReader securityContextReader = null;

    public void setsecurityContextReader(ComkerSecurityContextReader securityContextReader) {
        this.securityContextReader = securityContextReader;
    }

    private UserCache userCache = null;

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    private PasswordEncoder passwordEncoder = null;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    //--------------------------------------------------------------------------

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDetails loadUserDetails(String username)
            throws UsernameNotFoundException, DataAccessException {
        return loadUserDetails(username, null);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDetails loadUserDetails(String username, String spotCode)
            throws UsernameNotFoundException, DataAccessException {

        if (log.isDebugEnabled()) {
            log.debug("loadUserByUsername(" + username + ")");
        }

        ComkerUserDTO user = null;
        if (username != null) {
            user = userStorage.getByUsername(username);
        }

        if (user == null) {
            if (log.isErrorEnabled()) {
                log.error("loadUserByUsername(" + username + ") is NULL");
            }
            throw new UsernameNotFoundException("User " + username + " not found.");
        }

        Set<String> authoritySet = new HashSet<String>();

        authoritySet.addAll(userStorage.getGlobalAuthorities(user.getId()));

        if (spotCode != null) {
            Map<String,Set<String>> tree = userStorage.getSpotCodeWithAuthorities(user.getId());
            if (tree.containsKey(spotCode)) {
                authoritySet.addAll(tree.get(spotCode));
            }
        }

        if (log.isDebugEnabled()) {
            log.debug(MessageFormat.format("authorities of user {0}:", new Object[] {username}));
            for(String authority:authoritySet) {
                log.debug(MessageFormat.format(" + {0}", new Object[] {authority}));
            }
        }

        ComkerUserDetails userDetails = new ComkerUserDetails(
                user.getUsername(), user.getPassword(), spotCode, authoritySet);

        return userDetails;
    }

    @Override
    public void reloadUserDetails(String spotCode) {
        ComkerUserDetails oldUserDetails = securityContextReader.getUserDetails();
        if (oldUserDetails == null) return;

        ComkerUserDetails userDetails = loadUserDetails(oldUserDetails.getUsername(), spotCode);

        if (userDetails != null) {
            UsernamePasswordAuthenticationToken newToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
            securityContextHolder.getContext().setAuthentication(newToken);
        }

        // refresh oldUserDetails details (remove from userCache)
        if (userCache != null) {
            userCache.removeUserFromCache(oldUserDetails.getUsername());
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        ComkerUserDetails oldUserDetails = securityContextReader.getUserDetails();
        if (oldUserDetails == null) return;

        String oldEncodedPassword = passwordEncoder.encode(oldPassword);

        if (!oldEncodedPassword.equals(oldUserDetails.getPassword())) {
            throw new ComkerForbiddenAccessException(
                    "new_password_does_not_match_old_password",
                    new ComkerExceptionExtension("error.new_password_does_not_match_old_password", 
                            null, "The new password does not match the old password."));
        }

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        ComkerUserDetails newUserDetails = new ComkerUserDetails(oldUserDetails, newEncodedPassword);

        UsernamePasswordAuthenticationToken newToken =
                new UsernamePasswordAuthenticationToken(newUserDetails, newPassword);
        securityContextHolder.getContext().setAuthentication(newToken);
    }
}
