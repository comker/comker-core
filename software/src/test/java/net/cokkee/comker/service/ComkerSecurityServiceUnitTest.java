package net.cokkee.comker.service;

import java.util.Arrays;
import java.util.HashSet;

import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.impl.ComkerSecurityServiceImpl;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
import static org.hamcrest.CoreMatchers.hasItems;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserCache;
import static org.mockito.Mockito.*;

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
    private ComkerUserDao userDao;

    @Mock
    private UserCache userCache;

    @Mock
    private ComkerSecurityContextHolder securityContextHolder;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_get_user_details_in_current_session() {
        Mockito.when(securityContextHolder.getContext()).thenAnswer(new Answer<SecurityContext>() {
            @Override
            public SecurityContext answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDetails user = new ComkerUserDetails(
                        "username",
                        "password",
                        "SPOT_01",
                        new HashSet<String>(Arrays.asList(new String[] {}))
                );
                SecurityContext ctx = new SecurityContextImpl();
                ctx.setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), user.getAuthorities()));
                return ctx;
            }
        });

        ComkerUserDetails userDetails = securityService.getUserDetails();
        Assert.assertNotNull(userDetails);
        Assert.assertEquals(userDetails.getUsername(), "username");
        Assert.assertEquals(userDetails.getPassword(), "password");
    }

    @Test
    public void test_get_user_details_but_not_found() {
        Mockito.when(securityContextHolder.getContext()).thenAnswer(new Answer<SecurityContext>() {
            @Override
            public SecurityContext answer(InvocationOnMock invocation) throws Throwable {
                SecurityContext ctx = new SecurityContextImpl();
                ctx.setAuthentication(null);
                return ctx;
            }
        });

        ComkerUserDetails userDetails = securityService.getUserDetails();
        Assert.assertNull(userDetails);
    }
}
