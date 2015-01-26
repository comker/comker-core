package net.cokkee.comker.test.unit.service;

import java.util.Arrays;
import java.util.HashSet;

import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityContextHolder;
import net.cokkee.comker.service.impl.ComkerSecurityContextReaderImpl;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerSecurityContextReaderUnitTest {
    
    @InjectMocks
    private ComkerSecurityContextReaderImpl securityContextReader;

    @Mock
    private ComkerSecurityContextHolder securityContextHolder;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getUsername_ok() {
        Mockito.when(securityContextHolder.getContext()).thenAnswer(new Answer<SecurityContext>() {
            @Override
            public SecurityContext answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDetails user = new ComkerUserDetails(
                        "pnhung177",
                        "nopassword",
                        "PERSONAL",
                        new HashSet<String>(Arrays.asList(new String[] {}))
                );
                SecurityContext ctx = new SecurityContextImpl();
                ctx.setAuthentication(new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), user.getAuthorities()));
                return ctx;
            }
        });

        String username = securityContextReader.getUsername();
        Assert.assertEquals("pnhung177", username);
    }
    
    @Test
    public void getUsername_with_null_SecurityContext() {
        Mockito.when(securityContextHolder.getContext()).thenReturn(null);
        String username = securityContextReader.getUsername();
        Assert.assertNull(username);
    }
    
    @Test
    public void getUsername_with_null_Authentication() {
        Mockito.when(securityContextHolder.getContext()).thenAnswer(new Answer<SecurityContext>() {
            @Override
            public SecurityContext answer(InvocationOnMock invocation) throws Throwable {
                SecurityContext ctx = new SecurityContextImpl();
                ctx.setAuthentication(null);
                return ctx;
            }
        });

        String username = securityContextReader.getUsername();
        Assert.assertNull(username);
    }
    
    @Test
    public void getUserDetails_ok() {
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

        ComkerUserDetails userDetails = securityContextReader.getUserDetails();
        Assert.assertNotNull(userDetails);
        Assert.assertEquals("username", userDetails.getUsername());
        Assert.assertEquals("password", userDetails.getPassword());
    }

    @Test
    public void getUserDetails_but_not_found() {
        Mockito.when(securityContextHolder.getContext()).thenAnswer(new Answer<SecurityContext>() {
            @Override
            public SecurityContext answer(InvocationOnMock invocation) throws Throwable {
                SecurityContext ctx = new SecurityContextImpl();
                ctx.setAuthentication(null);
                return ctx;
            }
        });

        ComkerUserDetails userDetails = securityContextReader.getUserDetails();
        Assert.assertNull(userDetails);
    }
}
