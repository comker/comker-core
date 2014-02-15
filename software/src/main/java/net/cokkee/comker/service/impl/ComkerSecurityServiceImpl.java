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
import org.springframework.security.core.userdetails.UserDetails;
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
    public UserDetails getUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) return null;
        Authentication authentication = context.getAuthentication();
        if (authentication == null) return null;
        Object user = authentication.getPrincipal();
        if (user instanceof UserDetails) return (UserDetails) user;
        return null;
    }
    
    @Override
    public UserDetails loadUserDetails(String username, String spotCode)
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
                user.getUsername(), user.getPassword(),
                createGrantedAuthorities(authoritySet));

        return userDetails;
    }

    private static Collection<GrantedAuthority> createGrantedAuthorities(Collection<String> authorities) {
        Collection<GrantedAuthority> ga = new HashSet<GrantedAuthority>();
        for(String authority: authorities) {
            ga.add(new SimpleGrantedAuthority(authority));
        }
        return ga;
    }

    @Override
    public void reloadUserDetails(String spotCode) {
        UserDetails oldUserDetails = getUserDetails();
        if (oldUserDetails == null) return;

        UserDetails userDetails = loadUserDetails(oldUserDetails.getUsername(), spotCode);

        if (userDetails != null) {
            UsernamePasswordAuthenticationToken newToken =
                    new UsernamePasswordAuthenticationToken(userDetails,
                userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newToken);
        }

        // refresh user details (remove from userCache)
        if (getUserCache() != null) {
            getUserCache().removeUserFromCache(oldUserDetails.getUsername());
        }
    }

    @Override
    public void changePassword(String password, String encodedPassword) {
        UserDetails user = getUserDetails();
        if (user == null) return;

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.addAll(user.getAuthorities());

        ComkerUserDetails userDetails = new ComkerUserDetails(
                user.getUsername(), encodedPassword, authorities);

        UsernamePasswordAuthenticationToken newToken =
                new UsernamePasswordAuthenticationToken(userDetails, password);
        SecurityContextHolder.getContext().setAuthentication(newToken);
    }
}
