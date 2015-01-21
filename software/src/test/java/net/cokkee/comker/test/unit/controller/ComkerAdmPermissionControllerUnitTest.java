package net.cokkee.comker.test.unit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.cokkee.comker.controller.ComkerAdmPermissionController;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerPermissionStorage;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
            ComkerAdmPermissionControllerUnitTest.ServletConfig.class
        }
)
public class ComkerAdmPermissionControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmPermissionController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerPermissionStorage permissionStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getPermissionList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerPermissionDTO.class)).thenReturn(pager);
        
        List<ComkerPermissionDTO> list = new ArrayList<ComkerPermissionDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerPermissionDTO target = new ComkerPermissionDTO(
                    "PERMISSION_CODE_" + i);
            target.setId(UUID.randomUUID().toString());
            list.add(target);
        }
                
        Mockito.when(permissionStorage.count()).thenReturn(list.size());
        Mockito.when(permissionStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/permission"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerPermissionDTO t = list.get(i);
            result
                .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                .andExpect(jsonPath("$.collection[" + i + "].authority", Matchers.is(t.getAuthority())));
        }
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerPermissionDTO.class);
        Mockito.verify(permissionStorage, Mockito.times(1)).count();
        Mockito.verify(permissionStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(permissionStorage);
    }
    
    
    @Test
    public void getPermissionList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerPermissionDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerPermissionDTO.class)).thenReturn(sieve);
        
        List<ComkerPermissionDTO> list = new ArrayList<ComkerPermissionDTO>();
        
        Mockito.when(permissionStorage.count()).thenReturn(list.size());
        Mockito.when(permissionStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/permission")
                .param("start", "1")
                .param("limit", "10")
                .param("q", "Permission"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_AUTHORITY"), "Permission");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerPermissionDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerPermissionDTO.class);
        Mockito.verify(permissionStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(permissionStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(permissionStorage);
    }
    
    //--------------------------------------------------------------------------
    
    @Configuration
    @EnableWebMvc
    @ComponentScan(
            basePackageClasses = { 
                ComkerAdmPermissionController.class
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
