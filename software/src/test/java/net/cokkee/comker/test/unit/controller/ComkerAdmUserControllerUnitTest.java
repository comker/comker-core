package net.cokkee.comker.test.unit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.cokkee.comker.base.ComkerBaseConstant;
import net.cokkee.comker.controller.ComkerAdmUserController;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.util.ComkerDataUtil;

import org.junit.runner.RunWith;
import org.junit.Assert;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(
        classes = {
            ComkerAdmUserControllerUnitTest.ServletConfig.class
        }
)
public class ComkerAdmUserControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmUserController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerUserStorage userStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getUserList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerUserDTO.class)).thenReturn(pager);
        
        List<ComkerUserDTO> list = new ArrayList<ComkerUserDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerUserDTO target = new ComkerUserDTO(
                    "user" + i + "@comker.io",
                    "username_" + i,
                    "password" + i,
                    "Full Name");
            target.setId(UUID.randomUUID().toString());
            switch(i) {
                case 0:
                    target.setCrewIds(null);
                    break;
                case 1:
                case 2:
                    target.setCrewIds(new String[] {
                        "CREW_1", "CREW_2", "CREW_3", "CREW_4"
                    });
                    break;
                case 3:
                    target.setCrewIds(new String[] {});
                    break;
            }
            list.add(target);
        }
                
        Mockito.when(userStorage.count()).thenReturn(list.size());
        Mockito.when(userStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/user"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerUserDTO t = list.get(i);
            result
                    .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                    .andExpect(jsonPath("$.collection[" + i + "].email", Matchers.is(t.getEmail())))
                    .andExpect(jsonPath("$.collection[" + i + "].username", Matchers.is(t.getUsername())))
                    //.andExpect(jsonPath("$.collection[" + i + "].password", Matchers.isEmptyOrNullString()))
                    .andExpect(jsonPath("$.collection[" + i + "].fullname", Matchers.is(t.getFullname())));
        }
        
        result
            .andExpect(jsonPath("$.collection[1].crewIds", Matchers.hasSize(4)))
            .andExpect(jsonPath("$.collection[1].crewIds[0]", Matchers.is(list.get(1).getCrewIds()[0])))
            .andExpect(jsonPath("$.collection[1].crewIds[3]", Matchers.is(list.get(1).getCrewIds()[3])));
        
        result.andExpect(jsonPath("$.collection[3].crewIds", Matchers.hasSize(0)));
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerUserDTO.class);
        Mockito.verify(userStorage, Mockito.times(1)).count();
        Mockito.verify(userStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    
    @Test
    public void getUserList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerUserDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerUserDTO.class)).thenReturn(sieve);
        
        List<ComkerUserDTO> list = new ArrayList<ComkerUserDTO>();
        
        Mockito.when(userStorage.count()).thenReturn(list.size());
        Mockito.when(userStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/user")
                .param(ComkerBaseConstant.PAGER_START, "1")
                .param(ComkerBaseConstant.PAGER_LIMIT, "10")
                .param(ComkerBaseConstant.QUERY_STRING, "User"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_EMAIL"), "User");
        Assert.assertEquals(sieve.getCriterion("OR_USERNAME"), "User");
        Assert.assertEquals(sieve.getCriterion("OR_FULLNAME"), "User");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerUserDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerUserDTO.class);
        Mockito.verify(userStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(userStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    
    @Test
    public void getUser_Found_ShouldReturnFoundUser() throws Exception {
 
        ComkerUserDTO target = new ComkerUserDTO(
                "user@comker.com",
                "username", 
                "password", 
                "User Fullname");
        target.setId(UUID.randomUUID().toString());
        target.setCrewIds(new String[] {
            "CREW_1", "CREW_2", "CREW_3", "CREW_4"
        });
        
        Mockito.when(userStorage.get(target.getId())).thenReturn(target);
 
        mockMvc.perform(get("/comker/adm/user/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.email", Matchers.is(target.getEmail())))
                .andExpect(jsonPath("$.username", Matchers.is(target.getUsername())))
                //.andExpect(jsonPath("$.password", Matchers.isEmptyOrNullString()))
                .andExpect(jsonPath("$.fullname", Matchers.is(target.getFullname())))
                .andExpect(jsonPath("$.crewIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.crewIds[0]", Matchers.is(target.getCrewIds()[0])))
                .andExpect(jsonPath("$.crewIds[3]", Matchers.is(target.getCrewIds()[3])))
                .andReturn();
         
        Mockito.verify(userStorage, Mockito.times(1)).get(target.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    @Test
    public void createUser_ok() throws Exception {
        final ComkerUserDTO source = new ComkerUserDTO(
                "user@comker.com",
                "username", 
                "password", 
                "User Fullname");
        source.setId(UUID.randomUUID().toString());
        source.setCrewIds(new String[] {
            "CREW_1", "CREW_2", "CREW_3", "CREW_4"
        });
        
        final ComkerUserDTO target = new ComkerUserDTO(
                source.getEmail(),
                source.getUsername(),
                source.getPassword(),
                source.getFullname());
        target.setId(UUID.randomUUID().toString());
        target.setCrewIds(source.getCrewIds());
        
        Mockito.when(userStorage.create(Mockito.any(ComkerUserDTO.class)))
                .thenAnswer(new Answer<String>() {
                    @Override
                    public String answer(InvocationOnMock invocation) throws Throwable {
                        ComkerUserDTO input = (ComkerUserDTO) invocation.getArguments()[0];
                        
                        Assert.assertEquals(source.getEmail(), input.getEmail());
                        Assert.assertEquals(source.getUsername(), input.getUsername());
                        Assert.assertEquals(source.getFullname(), input.getFullname());
                        
                        return target.getId();
                    }
                });
        
        Mockito.when(userStorage.get(target.getId())).thenReturn(target);
        
        mockMvc.perform(post("/comker/adm/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.email", Matchers.is(source.getEmail())))
                .andExpect(jsonPath("$.username", Matchers.is(source.getUsername())))
                .andExpect(jsonPath("$.password", Matchers.is(source.getPassword())))
                .andExpect(jsonPath("$.fullname", Matchers.is(source.getFullname())))
                .andExpect(jsonPath("$.crewIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.crewIds[0]", Matchers.is(source.getCrewIds()[0])))
                .andExpect(jsonPath("$.crewIds[3]", Matchers.is(source.getCrewIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(userStorage, Mockito.times(1)).create(Mockito.any(ComkerUserDTO.class));
        Mockito.verify(userStorage, Mockito.times(1)).get(target.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    @Test
    public void updateUser_ok() throws Exception {
        final ComkerUserDTO source = new ComkerUserDTO(
                "user@comker.com",
                "username", 
                "password", 
                "User Fullname");
        source.setId(UUID.randomUUID().toString());
        source.setCrewIds(new String[] {
            "CREW_1", "CREW_2", "CREW_3", "CREW_4"
        });
        
        final ComkerUserDTO target = new ComkerUserDTO(
                                source.getEmail(),
                                source.getUsername(),
                                source.getPassword(),
                                source.getFullname());
        target.setId(source.getId());
        target.setCrewIds(new String[] {
            "CREW_1", "CREW_2", "CREW_3", "CREW_4"
        });
 
        Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        ComkerUserDTO input = (ComkerUserDTO) invocation.getArguments()[0];
                        Assert.assertEquals(source.getId(), input.getId());
                        Assert.assertEquals(source.getEmail(), input.getEmail());
                        Assert.assertEquals(source.getUsername(), input.getUsername());
                        Assert.assertEquals(source.getFullname(), input.getFullname());
                        Assert.assertArrayEquals(source.getCrewIds(), input.getCrewIds());
                        return null;
                    }
                }).when(userStorage).update(Mockito.any(ComkerUserDTO.class));
        
        Mockito.when(userStorage.get(source.getId())).thenReturn(target);
        
        mockMvc.perform(put("/comker/adm/user/{id}", source.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(source.getId())))
                .andExpect(jsonPath("$.email", Matchers.is(source.getEmail())))
                .andExpect(jsonPath("$.username", Matchers.is(source.getUsername())))
                .andExpect(jsonPath("$.password", Matchers.is(source.getPassword())))
                .andExpect(jsonPath("$.fullname", Matchers.is(source.getFullname())))
                .andExpect(jsonPath("$.crewIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.crewIds[0]", Matchers.is(source.getCrewIds()[0])))
                .andExpect(jsonPath("$.crewIds[3]", Matchers.is(source.getCrewIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(userStorage, Mockito.times(1)).update(Mockito.any(ComkerUserDTO.class));
        Mockito.verify(userStorage, Mockito.times(1)).get(source.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    @Test
    public void deleteUser_ok() throws Exception {
 
        final ComkerUserDTO target = new ComkerUserDTO(
                "user@comker.com",
                "username", 
                "password", 
                "User Fullname");
        target.setId(UUID.randomUUID().toString());
        target.setCrewIds(new String[] {
            "CREW_1"
        });
        
        Mockito.when(userStorage.get(target.getId())).thenReturn(target);
        Mockito.doNothing().when(userStorage).delete(target.getId());
 
        mockMvc.perform(delete("/comker/adm/user/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.email", Matchers.is(target.getEmail())))
                .andExpect(jsonPath("$.username", Matchers.is(target.getUsername())))
                .andExpect(jsonPath("$.password", Matchers.is(target.getPassword())))
                .andExpect(jsonPath("$.fullname", Matchers.is(target.getFullname())))
                .andExpect(jsonPath("$.crewIds", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.crewIds[0]", Matchers.is(target.getCrewIds()[0])))
                //.andDo(print())
                .andReturn();
 
        Mockito.verify(userStorage, Mockito.times(1)).get(target.getId());
        Mockito.verify(userStorage, Mockito.times(1)).delete(target.getId());
        Mockito.verifyNoMoreInteractions(userStorage);
    }
    
    //--------------------------------------------------------------------------
    
    @Configuration
    @EnableWebMvc
    @ComponentScan(
            basePackageClasses = { 
                ComkerAdmUserController.class
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
