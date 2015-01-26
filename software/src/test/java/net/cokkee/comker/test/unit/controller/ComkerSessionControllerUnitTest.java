package net.cokkee.comker.test.unit.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;

import net.cokkee.comker.controller.ComkerSessionController;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityContextReader;
import net.cokkee.comker.service.ComkerSessionService;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
        
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
            ComkerSessionControllerUnitTest.ServletConfig.class
        }
)
public class ComkerSessionControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerSessionController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerSecurityContextReader securityContextReader;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getInformation_on_logined_session() throws Exception {
 
        long time1 = Calendar.getInstance().getTime().getTime();

        Mockito.when(securityContextReader.getUserDetails()).thenAnswer(new Answer<ComkerUserDetails>() {

            @Override
            public ComkerUserDetails answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDetails userDetails = new ComkerUserDetails(
                        "username",
                        "password",
                        "SPOT_01",
                        new HashSet<String>(Arrays.asList(new String[]{"PERMISSION_01", "PERMISSION_02"})));
                return userDetails;
            }
        });
        
        ResultActions result = mockMvc.perform(get("/comker/session/information"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        long time2 = Calendar.getInstance().getTime().getTime();

        result.andExpect(jsonPath("$.timestamp", Matchers.greaterThan(time1)))
                .andExpect(jsonPath("$.timestamp", Matchers.lessThan(time2)))
                .andExpect(jsonPath("$.permissions", Matchers.hasSize(2)));
        //result.andDo(print());
 
        Mockito.verify(securityContextReader, Mockito.times(1)).getUserDetails();
        Mockito.verifyNoMoreInteractions(securityContextReader);
    }
    
    //--------------------------------------------------------------------------
    
    @Configuration
    @EnableWebMvc
    @ComponentScan(
            basePackageClasses = { 
                ComkerSessionController.class
            }
    )
    public static class ServletConfig extends WebMvcConfigurerAdapter {

        private static final Logger logger = LoggerFactory.getLogger(ServletConfig.class);

        public ServletConfig() {
            if (logger.isDebugEnabled()) {
                logger.debug("==@ " + ServletConfig.class.getSimpleName() + " is invoked");
            }
        }
    }
}
