package net.cokkee.comker.test.unit.controller;

import java.util.ArrayList;
import java.util.List;

import net.cokkee.comker.controller.ComkerAdmNavbarController;
import net.cokkee.comker.model.dto.ComkerNavNodeViewDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerNavbarStorage;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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
@ContextConfiguration("classpath:/test/unit/controller/ComkerAdmNavbarControllerUnitTest.xml")
public class ComkerAdmNavbarControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmNavbarController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerNavbarStorage navbarStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getTreeFromRoot_ok() throws Exception {
        ComkerNavNodeViewDTO root = generateNavbarTree();
        
        Mockito.when(navbarStorage.getTree()).thenReturn(root);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/navbar/tree"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result.andExpect(jsonPath("$.code", Matchers.is(root.getCode())))
                .andExpect(jsonPath("$.children[0].code", Matchers.is(root.getChildren().get(0).getCode())))
                .andExpect(jsonPath("$.children[1].code", Matchers.is(root.getChildren().get(1).getCode())));
        
        //result.andDo(print());
        
        Mockito.verify(navbarStorage, Mockito.times(1)).getTree();
        Mockito.verifyNoMoreInteractions(navbarStorage);
    }
    
    private ComkerNavNodeViewDTO generateNavbarTree() {
        ComkerNavNodeViewDTO navbarRoot = new ComkerNavNodeViewDTO(
                "NAVBAR_ROOT",
                "http://comker.net/",
                new String[] {"PERMISSION_01", "PERMISSION_02"});

        ComkerNavNodeViewDTO parent = null;
        ComkerNavNodeViewDTO item = null;

        parent = navbarRoot;
        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH_01",
                "http://comker.net/branch01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        parent = item;

        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH01_SUB01",
                "http://comker.net/branch01/sub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH01_SUB02",
                "http://comker.net/branch01/sub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);

        parent = navbarRoot;
        item = new ComkerNavNodeViewDTO(
                "NAVBAR_BRANCH_02",
                "http://comker.net/branch02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        parent.getChildren().add(item);
        
        return navbarRoot;
    }
}
