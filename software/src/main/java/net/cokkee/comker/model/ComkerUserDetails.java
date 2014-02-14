package net.cokkee.comker.model;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author drupalex
 */
public class ComkerUserDetails  implements UserDetails {

    static final GrantedAuthority NO_ROLE = new SimpleGrantedAuthority("NO_ROLE");

    public ComkerUserDetails(String username, String password, Collection<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public ComkerUserDetails(String username, String password, String[] authorityArray) {
        this.username = username;
        this.password = password;
        this.authorities = new ArrayList<GrantedAuthority>();

        if (authorityArray != null && authorityArray.length > 0) {
            for (int i=0; i<authorityArray.length; i++) {
                authorities.add(new SimpleGrantedAuthority(authorityArray[i]));
            }
        } else {
            authorities.add(NO_ROLE);
        }
    }

    private String username;
    private String password;
    private Collection<GrantedAuthority> authorities = null;

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
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
