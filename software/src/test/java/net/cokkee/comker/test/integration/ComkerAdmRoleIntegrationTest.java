 package net.cokkee.comker.test.integration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/test/integration/ComkerAdmRoleIntegrationTest.xml")
@WebAppConfiguration
public class ComkerAdmRoleIntegrationTest {

    @Autowired WebApplicationContext wac;
    
    @Autowired MockServletContext servletContext;

    @Autowired MockHttpSession session;

    @Autowired MockHttpServletRequest request;

    @Autowired MockHttpServletResponse response;
    
    @Autowired CharacterEncodingFilter characterEncodingFilter;
    
    @Autowired SessionFactory comkerSessionFactory;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .addFilter(characterEncodingFilter, "/*")
                .build();
    }
    
    //@Test
    public void getRole_Found_ShouldReturnFoundRole() throws Exception {
 
        MvcResult result = mockMvc.perform(get("/adm/role/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total", Matchers.is(4)))
//                .andExpect(jsonPath("$.collection", Matchers.is(roleObject.getCode())))
//                .andExpect(jsonPath("$.name", Matchers.is(roleObject.getName())))
//                .andExpect(jsonPath("$.description", Matchers.is(roleObject.getDescription())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
 
        String content = result.getResponse().getContentAsString();
        System.out.println(content);
    }
    
    
    @Test
    public void getRole_Found_ShouldReturnFoundRole_notFound() throws Exception {
 
        MvcResult result = mockMvc.perform(get("/adm/role/abcdef-unknown"))
                //.andExpect(status().isOk())
                //.andExpect(jsonPath("$.total", Matchers.is(4)))
//                .andExpect(jsonPath("$.collection", Matchers.is(roleObject.getCode())))
//                .andExpect(jsonPath("$.name", Matchers.is(roleObject.getName())))
//                .andExpect(jsonPath("$.description", Matchers.is(roleObject.getDescription())))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
