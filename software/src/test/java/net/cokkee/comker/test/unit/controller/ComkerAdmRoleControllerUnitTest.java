package net.cokkee.comker.test.unit.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.base.ComkerBaseConstant;

import net.cokkee.comker.controller.ComkerAdmRoleController;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerRoleStorage;
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
            ComkerAdmRoleControllerUnitTest.ServletConfig.class
        }
)
public class ComkerAdmRoleControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmRoleController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerRoleStorage roleStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getRoleList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerRoleDTO.class)).thenReturn(pager);
        
        List<ComkerRoleDTO> list = new ArrayList<ComkerRoleDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerRoleDTO target = new ComkerRoleDTO(
                    UUID.randomUUID().toString(),
                    "ROLE_CODE_" + i,
                    "Role name #" + i,
                    "Role Description #" + i);
            switch(i) {
                case 0:
                    target.setPermissionIds(null);
                    break;
                case 1:
                case 2:
                    target.setPermissionIds(new String[] {
                        "PERMISSION_1", "PERMISSION_2", "PERMISSION_3", "PERMISSION_4"
                    });
                    break;
                case 3:
                    target.setPermissionIds(new String[] {});
                    break;
            }
            list.add(target);
        }
                
        Mockito.when(roleStorage.count()).thenReturn(list.size());
        Mockito.when(roleStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/role"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerRoleDTO t = list.get(i);
            result
                .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                .andExpect(jsonPath("$.collection[" + i + "].code", Matchers.is(t.getCode())))
                .andExpect(jsonPath("$.collection[" + i + "].name", Matchers.is(t.getName())))
                .andExpect(jsonPath("$.collection[" + i + "].description", Matchers.is(t.getDescription())));
        }
        
        result
            .andExpect(jsonPath("$.collection[1].permissionIds", Matchers.hasSize(4)))
            .andExpect(jsonPath("$.collection[1].permissionIds[0]", Matchers.is(list.get(1).getPermissionIds()[0])))
            .andExpect(jsonPath("$.collection[1].permissionIds[3]", Matchers.is(list.get(1).getPermissionIds()[3])));
        
        result.andExpect(jsonPath("$.collection[3].permissionIds", Matchers.hasSize(0)));
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerRoleDTO.class);
        Mockito.verify(roleStorage, Mockito.times(1)).count();
        Mockito.verify(roleStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    
    @Test
    public void getRoleList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerRoleDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerRoleDTO.class)).thenReturn(sieve);
        
        List<ComkerRoleDTO> list = new ArrayList<ComkerRoleDTO>();
        
        Mockito.when(roleStorage.count()).thenReturn(list.size());
        Mockito.when(roleStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/role")
                .param(ComkerBaseConstant.PAGER_START, "1")
                .param(ComkerBaseConstant.PAGER_LIMIT, "10")
                .param(ComkerBaseConstant.QUERY_STRING, "Role"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_CODE"), "Role");
        Assert.assertEquals(sieve.getCriterion("OR_NAME"), "Role");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerRoleDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerRoleDTO.class);
        Mockito.verify(roleStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(roleStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    
    @Test
    public void getRole_Found_ShouldReturnFoundRole() throws Exception {
 
        ComkerRoleDTO target = new ComkerRoleDTO(
                UUID.randomUUID().toString(),
                "ROLE_CODE", 
                "Role name", 
                "Role Description");
        target.setPermissionIds(new String[] {
            "PERMISSION_1", "PERMISSION_2", "PERMISSION_3", "PERMISSION_4"
        });
        
        Mockito.when(roleStorage.get(target.getId())).thenReturn(target);
 
        mockMvc.perform(get("/comker/adm/role/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(target.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                .andExpect(jsonPath("$.permissionIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.permissionIds[0]", Matchers.is(target.getPermissionIds()[0])))
                .andExpect(jsonPath("$.permissionIds[3]", Matchers.is(target.getPermissionIds()[3])))
                .andReturn();
         
        Mockito.verify(roleStorage, Mockito.times(1)).get(target.getId());
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    //@Test
    public void getRole_NotFound_ShouldReturnErrorInfo() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(roleStorage.get(id)).thenThrow(
                new ComkerObjectNotFoundException(
                    "role_with__id__not_found",
                    new ComkerExceptionExtension("error.role_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Role object with id:{0} not found", 
                                    new Object[] {id}))));
        
        mockMvc.perform(get("/comker/adm/role/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerObjectNotFoundException")))
                .andExpect(jsonPath("$.label", Matchers.is("role_with__id__not_found")))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(roleStorage, Mockito.times(1)).get(id);
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    @Test
    public void createRole_ok() throws Exception {
        final ComkerRoleDTO source = new ComkerRoleDTO(
                "ROLE_1",
                "Role 1",
                "Description Role 1");
        source.setPermissionIds(new String[] {
            "PERMISSION_1", "PERMISSION_2", "PERMISSION_3", "PERMISSION_4"
        });
        
        final String targetId = UUID.randomUUID().toString();
        
        Mockito.when(roleStorage.create(Mockito.any(ComkerRoleDTO.class)))
                .thenAnswer(new Answer<ComkerRoleDTO>() {
                    @Override
                    public ComkerRoleDTO answer(InvocationOnMock invocation) throws Throwable {
                        ComkerRoleDTO input = (ComkerRoleDTO) invocation.getArguments()[0];
                        ComkerRoleDTO target = new ComkerRoleDTO(
                                targetId,
                                input.getCode(),
                                input.getName(),
                                input.getDescription());
                        target.setPermissionIds(input.getPermissionIds());
                        return target;
                    }
                });
        
        mockMvc.perform(post("/comker/adm/role")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(targetId)))
                .andExpect(jsonPath("$.code", Matchers.is(source.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                .andExpect(jsonPath("$.permissionIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.permissionIds[0]", Matchers.is(source.getPermissionIds()[0])))
                .andExpect(jsonPath("$.permissionIds[3]", Matchers.is(source.getPermissionIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(roleStorage, Mockito.times(1)).create(Mockito.any(ComkerRoleDTO.class));
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    @Test
    public void updateRole_ok() throws Exception {
        final ComkerRoleDTO source = new ComkerRoleDTO(
                UUID.randomUUID().toString(),
                "ROLE_1",
                "Role 1",
                "Description Role 1");
        source.setPermissionIds(new String[] {
            "PERMISSION_1", "PERMISSION_2", "PERMISSION_3", "PERMISSION_4"
        });
        
        final ComkerRoleDTO target = new ComkerRoleDTO(
                source.getId(),
                source.getCode(),
                source.getName(),
                source.getDescription());
        target.setPermissionIds(new String[] {
            "PERMISSION_1", "PERMISSION_2", "PERMISSION_3", "PERMISSION_4"
        });
 
        Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        ComkerRoleDTO input = (ComkerRoleDTO) invocation.getArguments()[0];
                        Assert.assertEquals(source.getId(), input.getId());
                        Assert.assertEquals(source.getCode(), input.getCode());
                        Assert.assertEquals(source.getName(), input.getName());
                        Assert.assertEquals(source.getDescription(), input.getDescription());
                        Assert.assertArrayEquals(source.getPermissionIds(), input.getPermissionIds());
                        return null;
                    }
                }).when(roleStorage).update(Mockito.any(ComkerRoleDTO.class));
        
        Mockito.when(roleStorage.get(source.getId())).thenReturn(target);
        
        mockMvc.perform(put("/comker/adm/role/{id}", source.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(source.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(source.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                .andExpect(jsonPath("$.permissionIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.permissionIds[0]", Matchers.is(source.getPermissionIds()[0])))
                .andExpect(jsonPath("$.permissionIds[3]", Matchers.is(source.getPermissionIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(roleStorage, Mockito.times(1)).update(Mockito.any(ComkerRoleDTO.class));
        Mockito.verify(roleStorage, Mockito.times(1)).get(source.getId());
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    @Test
    public void deleteRole_ok() throws Exception {
 
        final ComkerRoleDTO target = new ComkerRoleDTO(
                UUID.randomUUID().toString(),
                "ROLE_CODE", 
                "Role name", 
                "Role Description");
        target.setPermissionIds(new String[] {
            "PERMISSION_1"
        });
        
        Mockito.when(roleStorage.get(target.getId())).thenReturn(target);
        Mockito.doNothing().when(roleStorage).delete(target.getId());
 
        mockMvc.perform(delete("/comker/adm/role/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(target.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                .andExpect(jsonPath("$.permissionIds", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.permissionIds[0]", Matchers.is(target.getPermissionIds()[0])))
                //.andDo(print())
                .andReturn();
 
        Mockito.verify(roleStorage, Mockito.times(1)).get(target.getId());
        Mockito.verify(roleStorage, Mockito.times(1)).delete(target.getId());
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    //--------------------------------------------------------------------------
    
    @Configuration
    @EnableWebMvc
    @ComponentScan(
            basePackageClasses = { 
                ComkerAdmRoleController.class
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
