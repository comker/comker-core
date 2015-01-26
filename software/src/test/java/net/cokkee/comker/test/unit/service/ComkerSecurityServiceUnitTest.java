package net.cokkee.comker.test.unit.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSecurityContextHolder;
import net.cokkee.comker.service.impl.ComkerSecurityServiceImpl;
import net.cokkee.comker.storage.ComkerUserStorage;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComkerSecurityServiceUnitTest {
    
    @InjectMocks
    private ComkerSecurityServiceImpl securityService;
    
    @Mock
    private ComkerUserStorage userStorage;

    @Mock
    private UserCache userCache;

    @Mock
    private ComkerSecurityContextHolder securityContextHolder;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void loadUserDetails_with_valid_username() {
        ComkerUserDTO user = new ComkerUserDTO("pnhung177@gmail.com", 
                "pnhung177", "nopassword", "Pham Ngoc Hung");
        user.setId(UUID.randomUUID().toString());
        
        HashSet<String> authorities = new HashSet<String>(Arrays.asList(new String[] {
            "PERM_1", "PERM_2", "PERM_3", "PERM_4", "PERM_5", "PERM_6"
        }));
        
        Mockito.when(userStorage.getByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(userStorage.getGlobalAuthorities(user.getId())).thenReturn(authorities);
        
        ComkerUserDetails ud = securityService.loadUserDetails(user.getUsername());
        
        Mockito.verify(userStorage, Mockito.times(1)).getByUsername(user.getUsername());
        Mockito.verify(userStorage, Mockito.times(1)).getGlobalAuthorities(user.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
        
        Assert.assertEquals(user.getUsername(), ud.getUsername());
        Assert.assertEquals(user.getPassword(), ud.getPassword());
        Assert.assertNull(ud.getSpotCode());
        Assert.assertTrue(ud.isEnabled());
        Assert.assertTrue(ud.isAccountNonExpired());
        Assert.assertTrue(ud.isAccountNonLocked());
        Assert.assertTrue(ud.isCredentialsNonExpired());
        Assert.assertTrue(ud.getPermissions().size() == authorities.size());
        Assert.assertTrue(ud.getAuthorities().size() == authorities.size());
    }
    
    @Test
    public void loadUserDetails_with_valid_username_and_valid_spotCode() {
        String spotCode = "MY_SPOT_CODE";
        
        ComkerUserDTO user = new ComkerUserDTO("pnhung177@gmail.com", 
                "pnhung177", "nopassword", "Pham Ngoc Hung");
        user.setId(UUID.randomUUID().toString());
        
        HashSet<String> authorities = new HashSet<String>(Arrays.asList(new String[] {
            "PERM_1", "PERM_2", "PERM_3", "PERM_4", "PERM_5", "PERM_6"
        }));
        
        Map<String,Set<String>> spotAuthors = new HashMap<String,Set<String>>();
        spotAuthors.put(spotCode, new HashSet<String>(Arrays.asList(new String[] {
            "PERM_5", "PERM_6", "PERM_7", "PERM_8", "PERM_9"
        })));
        spotAuthors.put("OTHER1", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_10", "PERM_11", "PERM_12"
        })));
        spotAuthors.put("OTHER2", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_17", "PERM_18", "PERM_19"
        })));
        
        Mockito.when(userStorage.getByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(userStorage.getGlobalAuthorities(user.getId())).thenReturn(authorities);
        Mockito.when(userStorage.getSpotCodeWithAuthorities(user.getId())).thenReturn(spotAuthors);
        
        ComkerUserDetails ud = securityService.loadUserDetails(user.getUsername(), spotCode);
        
        Mockito.verify(userStorage, Mockito.times(1)).getByUsername(user.getUsername());
        Mockito.verify(userStorage, Mockito.times(1)).getGlobalAuthorities(user.getId());
        Mockito.verify(userStorage, Mockito.times(1)).getSpotCodeWithAuthorities(user.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
        
        Assert.assertEquals(user.getUsername(), ud.getUsername());
        Assert.assertEquals(user.getPassword(), ud.getPassword());
        Assert.assertEquals(spotCode, ud.getSpotCode());
        Assert.assertTrue(ud.isEnabled());
        Assert.assertTrue(ud.isAccountNonExpired());
        Assert.assertTrue(ud.isAccountNonLocked());
        Assert.assertTrue(ud.isCredentialsNonExpired());
        Assert.assertTrue(ud.getPermissions().size() == 9);
        Assert.assertTrue(ud.getAuthorities().size() == 9);
    }
    
    @Test
    public void loadUserDetails_with_valid_username_and_invalid_spotCode() {
        String spotCode = "INVALID_SPOT_CODE";
        
        ComkerUserDTO user = new ComkerUserDTO("pnhung177@gmail.com", 
                "pnhung177", "nopassword", "Pham Ngoc Hung");
        user.setId(UUID.randomUUID().toString());
        
        HashSet<String> authorities = new HashSet<String>(Arrays.asList(new String[] {
            "PERM_1", "PERM_2", "PERM_3", "PERM_4", "PERM_5", "PERM_6"
        }));
        
        Map<String,Set<String>> spotAuthors = new HashMap<String,Set<String>>();
        spotAuthors.put("MY_SPOT_CODE", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_5", "PERM_6", "PERM_7", "PERM_8", "PERM_9"
        })));
        spotAuthors.put("OTHER1", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_10", "PERM_11", "PERM_12"
        })));
        spotAuthors.put("OTHER2", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_17", "PERM_18", "PERM_19"
        })));
        
        Mockito.when(userStorage.getByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(userStorage.getGlobalAuthorities(user.getId())).thenReturn(authorities);
        Mockito.when(userStorage.getSpotCodeWithAuthorities(user.getId())).thenReturn(spotAuthors);
        
        ComkerUserDetails ud = securityService.loadUserDetails(user.getUsername(), spotCode);
        
        Mockito.verify(userStorage, Mockito.times(1)).getByUsername(user.getUsername());
        Mockito.verify(userStorage, Mockito.times(1)).getGlobalAuthorities(user.getId());
        Mockito.verify(userStorage, Mockito.times(1)).getSpotCodeWithAuthorities(user.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
        
        Assert.assertEquals(spotCode, ud.getSpotCode());
        Assert.assertTrue(ud.getPermissions().size() == authorities.size());
        Assert.assertTrue(ud.getAuthorities().size() == authorities.size());
    }
    
    @Test
    public void loadUserDetails_with_valid_invalid_username() {
        Mockito.when(userStorage.getByUsername("usernamenotfound")).thenReturn(null);
        try {
            securityService.loadUserDetails("usernamenotfound");
            Assert.assertTrue("Couldn't reach to here!", false);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof UsernameNotFoundException);
        }
        Mockito.verify(userStorage, Mockito.times(1)).getByUsername("usernamenotfound");
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    @Test
    public void reloadUserDetails_ok() {
        ComkerUserDetails userDetails = new ComkerUserDetails(
                "pnhung177",
                "nopassword",
                "OLD_SPOT_CODE",
                new HashSet<String>(Arrays.asList(new String[]{
                    "PERM_1", "PERM_2", "PERM_3", "PERM_4", "PERM_5"
                }))
        );
        SecurityContext ctx = new SecurityContextImpl();
        ctx.setAuthentication(new UsernamePasswordAuthenticationToken(
                userDetails, userDetails.getPassword(), userDetails.getAuthorities()));

        Mockito.when(securityContextHolder.getContext()).thenReturn(ctx);
        
        String spotCode = "NEW_SPOT_CODE";
        
        ComkerUserDTO user = new ComkerUserDTO("pnhung177@gmail.com", 
                "pnhung177", "nopassword", "Pham Ngoc Hung");
        user.setId(UUID.randomUUID().toString());
        
        HashSet<String> authorities = new HashSet<String>(Arrays.asList(new String[] {
            "PERM_1", "PERM_2"
        }));
        
        Map<String,Set<String>> spotAuthors = new HashMap<String,Set<String>>();
        spotAuthors.put("OLD_SPOT_CODE", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_1", "PERM_2", "PERM_3", "PERM_4", "PERM_5"
        })));
        spotAuthors.put("NEW_SPOT_CODE", new HashSet<String>(Arrays.asList(new String[] {
            "PERM_2", "PERM_6", "PERM_7", "PERM_8", "PERM_9"
        })));
        
        Mockito.when(userStorage.getByUsername(user.getUsername())).thenReturn(user);
        Mockito.when(userStorage.getGlobalAuthorities(user.getId())).thenReturn(authorities);
        Mockito.when(userStorage.getSpotCodeWithAuthorities(user.getId())).thenReturn(spotAuthors);
        
        securityService.reloadUserDetails(spotCode);
        
        Mockito.verify(securityContextHolder, Mockito.times(2)).getContext();
        Mockito.verify(userStorage, Mockito.times(1)).getByUsername(user.getUsername());
        Mockito.verify(userStorage, Mockito.times(1)).getGlobalAuthorities(user.getId());
        Mockito.verify(userStorage, Mockito.times(1)).getSpotCodeWithAuthorities(user.getId());
        Mockito.verify(userCache, Mockito.times(1)).removeUserFromCache("pnhung177");
        Mockito.verifyNoMoreInteractions(userCache);
        Mockito.verifyNoMoreInteractions(userStorage);
        Mockito.verifyNoMoreInteractions(securityContextHolder);
        
        Authentication auth = ctx.getAuthentication();
        Assert.assertTrue(auth.getAuthorities().size() == 6);
    }
    
    @Test
    public void reloadUserDetails_with_null_authentication() {
        SecurityContext ctx = new SecurityContextImpl();
        ctx.setAuthentication(null);
        Mockito.when(securityContextHolder.getContext()).thenReturn(ctx);
        
        securityService.reloadUserDetails("NEW_SPOT_CODE");
        
        Mockito.verify(securityContextHolder, Mockito.times(1)).getContext();
        
        // nothing to change
        Mockito.verifyNoMoreInteractions(userCache);
        Mockito.verifyNoMoreInteractions(userStorage);
        
        Assert.assertNull(ctx.getAuthentication());
    }
}
