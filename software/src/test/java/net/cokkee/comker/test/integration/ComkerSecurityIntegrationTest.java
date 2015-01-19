package net.cokkee.comker.test.integration;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.cokkee.comker.model.ComkerUserDetails;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test/integration/ComkerSecurityIntegrationTest.xml")
@WebAppConfiguration
public class ComkerSecurityIntegrationTest {

    private static String SEC_CONTEXT_ATTR = HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    MockServletContext servletContext;

    @Autowired
    MockHttpSession session;

    @Autowired
    MockHttpServletRequest request;

    @Autowired
    MockHttpServletResponse response;

    @Autowired
    FilterChainProxy springSecurityFilterChain;

    @Autowired
    CharacterEncodingFilter characterEncodingFilter;

    @Autowired
    SessionFactory comkerSessionFactory;

    protected MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain, "/*")
                .addFilter(characterEncodingFilter, "/*")
                .build();
    }

    @Test
    public void login_with_invalid_username() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "wrong_username")
                .param("password", "xxxxxx"))
                .andExpect(redirectedUrl("/comker/#auth/loginFailure"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        HttpSession session = mvcResult.getRequest().getSession();
                        SecurityContext sc = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                        Assert.assertNull(sc);
                    }
                })
                .andReturn();
    }

    @Test
    public void login_with_valid_username() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                .param("username", "admin").param("password", "dobietday"))
                .andExpect(redirectedUrl("/comker/"))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        HttpSession session = mvcResult.getRequest().getSession();
                        SecurityContext sc = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                        Assert.assertEquals("admin", sc.getAuthentication().getName());

                        ComkerUserDetails ud = (ComkerUserDetails) sc.getAuthentication().getPrincipal();

                        for (String perm : ud.getPermissions()) {
                            System.out.println("===============" + perm);
                        }

                        
                        SecurityContext sc2 = SecurityContextHolder.getContext();
                        Assert.assertNotNull(sc2);
                        System.out.println("-=========" + sc2.getClass().getName());
                    }
                })
                .andReturn();
    }

    //@Test
    public void login_with_user_admin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login")
                //.header("X-Requested-With", "ComkerAjaxRequest")
                .param("username", "admin")
                .param("password", "dobietday"))
                //.andExpect(status().isOk())
                //                .andExpect(jsonPath("$.collection", Matchers.is(roleObject.getCode())))
                //                .andExpect(jsonPath("$.name", Matchers.is(roleObject.getName())))
                //                .andExpect(jsonPath("$.description", Matchers.is(roleObject.getDescription())))
                .andExpect(new ResultMatcher() {
                    @Override
                    public void match(MvcResult mvcResult) throws Exception {
                        HttpSession session = mvcResult.getRequest().getSession();
                        SecurityContext sc = (SecurityContext) session.getAttribute(SEC_CONTEXT_ATTR);
                        Assert.assertEquals("admin", sc.getAuthentication().getName());
                    }
                })
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession();

        result = mockMvc.perform(get("/comker/session/information").session(session))
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$.total", Matchers.is(4)))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getRole_Found_ShouldReturnFoundRole() throws Exception {

        MockHttpSession session = (MockHttpSession) mockMvc.perform(post("/login")
                .param("username", "admin")
                .param("password", "dobietday"))
                .andReturn().getRequest().getSession();
        
        MvcResult result = mockMvc.perform(get("/comker/adm/role/").session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", Matchers.is(4)))
                //                .andExpect(jsonPath("$.collection", Matchers.is(roleObject.getCode())))
                //                .andExpect(jsonPath("$.name", Matchers.is(roleObject.getName())))
                //                .andExpect(jsonPath("$.description", Matchers.is(roleObject.getDescription())))
                .andDo(print())
                .andReturn();
    }
}
