package net.cokkee.comker.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author drupalex
 */
public class ComkerUserDetails  implements UserDetails {

    static final GrantedAuthority NO_ROLE = new SimpleGrantedAuthority("NO_ROLE");

    public ComkerUserDetails(String username, String password, String spotCode, Set<String> permissions) {
        this.username = username;
        this.password = password;
        this.spotCode = spotCode;
        this.permissions = permissions;

        this.authorities = null;
    }

    public ComkerUserDetails(ComkerUserDetails userdetails, String password) {
        this.username = userdetails.getUsername();
        this.password = password;
        this.spotCode = userdetails.getSpotCode();
        this.permissions = userdetails.getPermissions();
        this.authorities = userdetails.getAuthorities();
    }

    public ComkerUserDetails(ComkerUserDetails userdetails, String spotCode, Set<String> permissions) {
        this.username = userdetails.getUsername();
        this.password = userdetails.getPassword();
        this.spotCode = spotCode;
        this.permissions = permissions;
        this.authorities = null;
    }

    private String username;
    private String password;
    private String spotCode;
    private Set<String> permissions = null;

    private Collection<GrantedAuthority> authorities = null;

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public String getSpotCode() {
        return spotCode;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        if (authorities == null) {
            authorities = new HashSet<GrantedAuthority>();
            if (permissions != null && permissions.size() > 0) {
                for (String permission:permissions) {
                    authorities.add(new SimpleGrantedAuthority(permission));
                }
            } else {
                authorities.add(NO_ROLE);
            }
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
