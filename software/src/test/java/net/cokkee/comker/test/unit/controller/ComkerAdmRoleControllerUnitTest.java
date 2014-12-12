package net.cokkee.comker.test.unit.controller;

import java.text.MessageFormat;
import java.util.UUID;
import net.cokkee.comker.controller.ComkerAdmRoleController;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerExceptionExtension;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerRoleStorage;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
        
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test/unit/controller/ComkerAdmRoleControllerUnitTest.xml")
public class ComkerAdmRoleControllerUnitTest {
    
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype()         
    );
    
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
    public void getRole_Found_ShouldReturnFoundRole() throws Exception {
 
        ComkerRoleDTO roleObject = new ComkerRoleDTO("ROLE_CODE", "Role name", "Role Description");
        roleObject.setId(UUID.randomUUID().toString());
        
        Mockito.when(roleStorage.get(roleObject.getId())).thenReturn(roleObject);
 
        MvcResult result = mockMvc.perform(get("/comker/adm/role/{id}", roleObject.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", Matchers.is(roleObject.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(roleObject.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(roleObject.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(roleObject.getDescription())))
                .andReturn();
 
        String content = result.getResponse().getContentAsString();
        
        Mockito.verify(roleStorage, Mockito.times(1)).get(roleObject.getId());
        Mockito.verifyNoMoreInteractions(roleStorage);
    }
    
    @Test
    public void getRole_NotFound_ShouldReturnErrorInfo() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(roleStorage.get(id)).thenThrow(
                new ComkerObjectNotFoundException(
                    "role_with__id__not_found",
                    new ComkerExceptionExtension("error.role_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Role object with id:{0} not found", 
                                    new Object[] {id}))));
        
        MvcResult result = mockMvc.perform(get("/comker/adm/role/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerObjectNotFoundException")))
                .andExpect(jsonPath("$.label", Matchers.is("role_with__id__not_found")))
                .andDo(print())
                .andReturn();
    }
    
    
}
