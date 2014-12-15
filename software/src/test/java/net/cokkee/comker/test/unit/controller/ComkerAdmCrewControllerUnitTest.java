package net.cokkee.comker.test.unit.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.controller.ComkerAdmCrewController;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.model.struct.ComkerKeyAndValueSet;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerCrewStorage;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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
@ContextConfiguration("classpath:/test/unit/controller/ComkerAdmCrewControllerUnitTest.xml")
public class ComkerAdmCrewControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmCrewController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerCrewStorage crewStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getCrewList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerCrewDTO.class)).thenReturn(pager);
        
        List<ComkerCrewDTO> list = new ArrayList<ComkerCrewDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerCrewDTO target = new ComkerCrewDTO(
                    "Crew name #" + i,
                    "Crew Description #" + i);
            target.setId(UUID.randomUUID().toString());
            switch(i) {
                case 0:
                    target.setGlobalRoleIds(null);
                    break;
                case 1:
                case 2:
                    target.setGlobalRoleIds(new String[] {
                        "ROLE_1", "ROLE_2", "ROLE_3", "ROLE_4"
                    });
                    break;
                case 3:
                    target.setGlobalRoleIds(new String[] {});
                    break;
            }
            list.add(target);
        }
                
        Mockito.when(crewStorage.count()).thenReturn(list.size());
        Mockito.when(crewStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/crew"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerCrewDTO t = list.get(i);
            result
                .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                .andExpect(jsonPath("$.collection[" + i + "].name", Matchers.is(t.getName())))
                .andExpect(jsonPath("$.collection[" + i + "].description", Matchers.is(t.getDescription())));
        }
        
        result
            .andExpect(jsonPath("$.collection[1].globalRoleIds", Matchers.hasSize(4)))
            .andExpect(jsonPath("$.collection[1].globalRoleIds[0]", Matchers.is(list.get(1).getGlobalRoleIds()[0])))
            .andExpect(jsonPath("$.collection[1].globalRoleIds[3]", Matchers.is(list.get(1).getGlobalRoleIds()[3])));
        
        result.andExpect(jsonPath("$.collection[3].globalRoleIds", Matchers.hasSize(0)));
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerCrewDTO.class);
        Mockito.verify(crewStorage, Mockito.times(1)).count();
        Mockito.verify(crewStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    
    @Test
    public void getCrewList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerCrewDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerCrewDTO.class)).thenReturn(sieve);
        
        List<ComkerCrewDTO> list = new ArrayList<ComkerCrewDTO>();
        
        Mockito.when(crewStorage.count()).thenReturn(list.size());
        Mockito.when(crewStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/crew")
                .param("start", "1")
                .param("limit", "10")
                .param("q", "Crew"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_NAME"), "Crew");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerCrewDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerCrewDTO.class);
        Mockito.verify(crewStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(crewStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    @Test
    public void getCrew_Found_ShouldReturnFoundCrew() throws Exception {
 
        ComkerCrewDTO target = new ComkerCrewDTO("Crew name", "Crew Description");
        target.setId(UUID.randomUUID().toString());
        target.setGlobalRoleIds(new String[] {
            "ROLE_1", "ROLE_2", "ROLE_3", "ROLE_4"
        });
        target.setScopedRoleIds(new ComkerKeyAndValueSet[] {
            new ComkerKeyAndValueSet("SPOT_1", new String[] {}), 
            new ComkerKeyAndValueSet("SPOT_2", new String[] {"ROLE_11", "ROLE_12", "ROLE_13"})
        });
        
        Mockito.when(crewStorage.get(target.getId())).thenReturn(target);
 
        mockMvc.perform(get("/comker/adm/crew/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                .andExpect(jsonPath("$.globalRoleIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.globalRoleIds[0]", Matchers.is(target.getGlobalRoleIds()[0])))
                .andExpect(jsonPath("$.globalRoleIds[3]", Matchers.is(target.getGlobalRoleIds()[3])))
                .andExpect(jsonPath("$.scopedRoleIds", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.scopedRoleIds[0].key", Matchers.is(target.getScopedRoleIds()[0].getKey())))
                .andExpect(jsonPath("$.scopedRoleIds[0].values", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.scopedRoleIds[1].key", Matchers.is(target.getScopedRoleIds()[1].getKey())))
                .andExpect(jsonPath("$.scopedRoleIds[1].values", Matchers.hasSize(3)))
                .andReturn();
       
        Mockito.verify(crewStorage, Mockito.times(1)).get(target.getId());
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    @Test
    public void getCrew_NotFound_ShouldReturnErrorInfo() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(crewStorage.get(id)).thenThrow(
                new ComkerObjectNotFoundException(
                    "crew_with__id__not_found",
                    new ComkerExceptionExtension("error.crew_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Crew object with id:{0} not found", 
                                    new Object[] {id}))));
        
        mockMvc.perform(get("/comker/adm/crew/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerObjectNotFoundException")))
                .andExpect(jsonPath("$.label", Matchers.is("crew_with__id__not_found")))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(crewStorage, Mockito.times(1)).get(id);
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    @Test
    public void createCrew_ok() throws Exception {
        final ComkerCrewDTO source = new ComkerCrewDTO(
                "Crew 1",
                "Description Crew 1");
        
        final String targetId = UUID.randomUUID().toString();
        
        Mockito.when(crewStorage.create(Mockito.any(ComkerCrewDTO.class)))
                .thenAnswer(new Answer<ComkerCrewDTO>() {
                    @Override
                    public ComkerCrewDTO answer(InvocationOnMock invocation) throws Throwable {
                        ComkerCrewDTO input = (ComkerCrewDTO) invocation.getArguments()[0];
                        ComkerCrewDTO target = new ComkerCrewDTO(
                                input.getName(),
                                input.getDescription());
                        target.setId(targetId);
                        return target;
                    }
                });
        
        mockMvc.perform(post("/comker/adm/crew")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(targetId)))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(crewStorage, Mockito.times(1)).create(Mockito.any(ComkerCrewDTO.class));
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    @Test
    public void updateCrew_ok() throws Exception {
        final ComkerCrewDTO source = new ComkerCrewDTO(
                "Crew 1",
                "Description Crew 1");
        source.setId(UUID.randomUUID().toString());
        
        final ComkerCrewDTO target = new ComkerCrewDTO(
                                source.getName(),
                                source.getDescription());
        target.setId(source.getId());
 
        Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        ComkerCrewDTO input = (ComkerCrewDTO) invocation.getArguments()[0];
                        Assert.assertEquals(source.getId(), input.getId());
                        Assert.assertEquals(source.getName(), input.getName());
                        Assert.assertEquals(source.getDescription(), input.getDescription());
                        return null;
                    }
                }).when(crewStorage).update(Mockito.any(ComkerCrewDTO.class));
        
        Mockito.when(crewStorage.get(source.getId())).thenReturn(target);
        
        mockMvc.perform(put("/comker/adm/crew/{id}", source.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(source.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(crewStorage, Mockito.times(1)).update(Mockito.any(ComkerCrewDTO.class));
        Mockito.verify(crewStorage, Mockito.times(1)).get(source.getId());
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
    
    @Test
    public void deleteCrew_ok() throws Exception {
 
        final ComkerCrewDTO target = new ComkerCrewDTO(
                "Crew name", 
                "Crew Description");
        target.setId(UUID.randomUUID().toString());
        
        Mockito.when(crewStorage.get(target.getId())).thenReturn(target);
        Mockito.doNothing().when(crewStorage).delete(target.getId());
 
        mockMvc.perform(delete("/comker/adm/crew/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                //.andDo(print())
                .andReturn();
 
        Mockito.verify(crewStorage, Mockito.times(1)).get(target.getId());
        Mockito.verify(crewStorage, Mockito.times(1)).delete(target.getId());
        Mockito.verifyNoMoreInteractions(crewStorage);
    }
}
