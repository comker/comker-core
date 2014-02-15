package net.cokkee.comker.service.impl;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.service.ComkerSecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author drupalex
 */
public class ComkerSecurityServiceImpl implements ComkerSecurityService {

    private final Logger log = LoggerFactory.getLogger(ComkerSecurityServiceImpl.class);


    private ComkerUserDao userDao = null;

    public ComkerUserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }

    private UserCache userCache = null;

    public UserCache getUserCache() {
        return userCache;
    }

    public void setUserCache(UserCache userCache) {
        this.userCache = userCache;
    }

    //--------------------------------------------------------------------------

    @Override
    public ComkerUserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return null;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return null;
        Object user = authentication.getPrincipal();
        if (user instanceof ComkerUserDetails) return (ComkerUserDetails) user;
        return null;
    }
    
    @Override
    public ComkerUserDetails loadUserDetails(String username, String spotCode)
            throws UsernameNotFoundException, DataAccessException {

        if (log.isDebugEnabled()) {
            log.debug("loadUserByUsername(" + username + ")");
        }

        ComkerUser user = null;
        if (username != null) {
            user = getUserDao().getByUsername(username);
        }

        if (user == null) {
            if (log.isErrorEnabled()) {
                log.error("loadUserByUsername(" + username + ") is NULL");
            }
            throw new UsernameNotFoundException("User " + username + " not found.");
        }

        Set<String> authoritySet = new HashSet<String>();

        authoritySet.addAll(getUserDao().getCodeOfGlobalPermission(user));

        if (spotCode != null) {
            Map<String,Set<String>> tree = getUserDao().getCodeOfSpotWithPermission(user);
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
        ComkerUserDetails oldUserDetails = getUserDetails();
        if (oldUserDetails == null) return;

        ComkerUserDetails userDetails = loadUserDetails(oldUserDetails.getUsername(), spotCode);

        if (userDetails != null) {
            UsernamePasswordAuthenticationToken newToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newToken);
        }

        // refresh oldUserDetails details (remove from userCache)
        if (getUserCache() != null) {
            getUserCache().removeUserFromCache(oldUserDetails.getUsername());
        }
    }

    @Override
    public void changePassword(String password, String encodedPassword) {
        ComkerUserDetails oldUserDetails = getUserDetails();
        if (oldUserDetails == null) return;

        ComkerUserDetails newUserDetails = new ComkerUserDetails(oldUserDetails, encodedPassword);

        UsernamePasswordAuthenticationToken newToken =
                new UsernamePasswordAuthenticationToken(newUserDetails, password);
        SecurityContextHolder.getContext().setAuthentication(newToken);
    }
}
