package net.cokkee.comker.test.unit.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.cokkee.comker.controller.ComkerAdmWatchdogController;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerWatchdogDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerWatchdogStorage;

import org.junit.runner.RunWith;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
        
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
            ComkerAdmWatchdogControllerUnitTest.ServletConfig.class
        }
)
public class ComkerAdmWatchdogControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmWatchdogController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerWatchdogStorage watchdogStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getWatchdogList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerWatchdogDTO.class)).thenReturn(pager);
        
        List<ComkerWatchdogDTO> list = new ArrayList<ComkerWatchdogDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerWatchdogDTO target = new ComkerWatchdogDTO();
            target.setUsername("username_" + i);
            target.setMethodName("methodname_" + i);
            target.setHitTime(Calendar.getInstance().getTime());
            target.setId(UUID.randomUUID().toString());
            list.add(target);
        }
                
        Mockito.when(watchdogStorage.count()).thenReturn(list.size());
        Mockito.when(watchdogStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/watchdog"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerWatchdogDTO t = list.get(i);
            result
                    .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                    .andExpect(jsonPath("$.collection[" + i + "].username", Matchers.is(t.getUsername())))
                    .andExpect(jsonPath("$.collection[" + i + "].methodName", Matchers.is(t.getMethodName())))
                    .andExpect(jsonPath("$.collection[" + i + "].hitTime", Matchers.is(t.getHitTime().getTime())));
            //result.andDo(print());
        }
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerWatchdogDTO.class);
        Mockito.verify(watchdogStorage, Mockito.times(1)).count();
        Mockito.verify(watchdogStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(watchdogStorage);
    }
    
    
    @Test
    public void getWatchdogList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerWatchdogDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerWatchdogDTO.class)).thenReturn(sieve);
        
        List<ComkerWatchdogDTO> list = new ArrayList<ComkerWatchdogDTO>();
        
        Mockito.when(watchdogStorage.count()).thenReturn(list.size());
        Mockito.when(watchdogStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/watchdog")
                .param("start", "1")
                .param("limit", "10")
                .param("q", "Watchdog"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_USERNAME"), "Watchdog");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerWatchdogDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerWatchdogDTO.class);
        Mockito.verify(watchdogStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(watchdogStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(watchdogStorage);
    }
    
    //--------------------------------------------------------------------------
    
    @Configuration
    @EnableWebMvc
    @ComponentScan(
            basePackageClasses = { 
                ComkerAdmWatchdogController.class
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
