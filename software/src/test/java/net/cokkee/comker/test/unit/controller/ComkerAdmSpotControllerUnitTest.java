package net.cokkee.comker.test.unit.controller;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.cokkee.comker.controller.ComkerAdmSpotController;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.storage.ComkerSpotStorage;
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
@ContextConfiguration("classpath:/test/unit/controller/ComkerAdmSpotControllerUnitTest.xml")
public class ComkerAdmSpotControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerAdmSpotController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerSessionService sessionService;
    
    @Mock
    private ComkerSpotStorage spotStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void getSpotList_default() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        
        Mockito.when(sessionService.getPager(ComkerSpotDTO.class)).thenReturn(pager);
        
        List<ComkerSpotDTO> list = new ArrayList<ComkerSpotDTO>();
        
        for(int i=0; i<4; i++) {
            ComkerSpotDTO target = new ComkerSpotDTO(
                    "SPOT_CODE_" + i,
                    "Spot name #" + i,
                    "Spot Description #" + i);
            target.setId(UUID.randomUUID().toString());
            switch(i) {
                case 0:
                    target.setModuleIds(null);
                    break;
                case 1:
                case 2:
                    target.setModuleIds(new String[] {
                        "MODULE_1", "MODULE_2", "MODULE_3", "MODULE_4"
                    });
                    break;
                case 3:
                    target.setModuleIds(new String[] {});
                    break;
            }
            list.add(target);
        }
                
        Mockito.when(spotStorage.count()).thenReturn(list.size());
        Mockito.when(spotStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/spot"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        for(int i=0; i<4; i++) {
            ComkerSpotDTO t = list.get(i);
            result
                .andExpect(jsonPath("$.collection[" + i + "].id", Matchers.is(t.getId())))
                .andExpect(jsonPath("$.collection[" + i + "].code", Matchers.is(t.getCode())))
                .andExpect(jsonPath("$.collection[" + i + "].name", Matchers.is(t.getName())))
                .andExpect(jsonPath("$.collection[" + i + "].description", Matchers.is(t.getDescription())));
        }
        
        result
            .andExpect(jsonPath("$.collection[1].moduleIds", Matchers.hasSize(4)))
            .andExpect(jsonPath("$.collection[1].moduleIds[0]", Matchers.is(list.get(1).getModuleIds()[0])))
            .andExpect(jsonPath("$.collection[1].moduleIds[3]", Matchers.is(list.get(1).getModuleIds()[3])));
        
        result.andExpect(jsonPath("$.collection[3].moduleIds", Matchers.hasSize(0)));
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerSpotDTO.class);
        Mockito.verify(spotStorage, Mockito.times(1)).count();
        Mockito.verify(spotStorage, Mockito.times(1)).findAll(pager);
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    
    @Test
    public void getSpotList_with_sieve_and_pager() throws Exception {
 
        ComkerQueryPager pager = new ComkerQueryPager();
        ComkerQuerySieve sieve = new ComkerQuerySieve();
        
        Mockito.when(sessionService.getPager(ComkerSpotDTO.class)).thenReturn(pager);
        Mockito.when(sessionService.getSieve(ComkerSpotDTO.class)).thenReturn(sieve);
        
        List<ComkerSpotDTO> list = new ArrayList<ComkerSpotDTO>();
        
        Mockito.when(spotStorage.count()).thenReturn(list.size());
        Mockito.when(spotStorage.findAll(pager)).thenReturn(list);
 
        ResultActions result = mockMvc.perform(get("/comker/adm/spot")
                .param("start", "1")
                .param("limit", "10")
                .param("q", "Spot"));
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result
                .andExpect(jsonPath("$.collection", Matchers.hasSize(0)))
                .andExpect(jsonPath("$.total", Matchers.is(0)));
        
        Assert.assertEquals(pager.getStart(), new Integer(1));
        Assert.assertEquals(pager.getLimit(), new Integer(10));
        Assert.assertEquals(sieve.getCriterion("OR_CODE"), "Spot");
        Assert.assertEquals(sieve.getCriterion("OR_NAME"), "Spot");
        
        Mockito.verify(sessionService, Mockito.times(1)).getPager(ComkerSpotDTO.class);
        Mockito.verify(sessionService, Mockito.times(1)).getSieve(ComkerSpotDTO.class);
        Mockito.verify(spotStorage, Mockito.times(1)).count(sieve);
        Mockito.verify(spotStorage, Mockito.times(1)).findAll(sieve, pager);
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    
    @Test
    public void getSpot_Found_ShouldReturnFoundSpot() throws Exception {
 
        ComkerSpotDTO target = new ComkerSpotDTO(
                "SPOT_CODE", 
                "Spot name", 
                "Spot Description");
        target.setId(UUID.randomUUID().toString());
        target.setModuleIds(new String[] {
            "MODULE_1", "MODULE_2", "MODULE_3", "MODULE_4"
        });
        
        Mockito.when(spotStorage.get(target.getId())).thenReturn(target);
 
        mockMvc.perform(get("/comker/adm/spot/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(target.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                .andExpect(jsonPath("$.moduleIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.moduleIds[0]", Matchers.is(target.getModuleIds()[0])))
                .andExpect(jsonPath("$.moduleIds[3]", Matchers.is(target.getModuleIds()[3])))
                .andReturn();
         
        Mockito.verify(spotStorage, Mockito.times(1)).get(target.getId());
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    @Test
    public void getSpot_NotFound_ShouldReturnErrorInfo() throws Exception {
        String id = UUID.randomUUID().toString();
        Mockito.when(spotStorage.get(id)).thenThrow(
                new ComkerObjectNotFoundException(
                    "spot_with__id__not_found",
                    new ComkerExceptionExtension("error.spot_with__id__not_found", 
                            new Object[] {id}, 
                            MessageFormat.format("Spot object with id:{0} not found", 
                                    new Object[] {id}))));
        
        mockMvc.perform(get("/comker/adm/spot/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerObjectNotFoundException")))
                .andExpect(jsonPath("$.label", Matchers.is("spot_with__id__not_found")))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(spotStorage, Mockito.times(1)).get(id);
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    @Test
    public void createSpot_ok() throws Exception {
        final ComkerSpotDTO source = new ComkerSpotDTO(
                "SPOT_1",
                "Spot 1",
                "Description Spot 1");
        source.setModuleIds(new String[] {
            "MODULE_1", "MODULE_2", "MODULE_3", "MODULE_4"
        });
        
        final String targetId = UUID.randomUUID().toString();
        
        Mockito.when(spotStorage.create(Mockito.any(ComkerSpotDTO.class)))
                .thenAnswer(new Answer<ComkerSpotDTO>() {
                    @Override
                    public ComkerSpotDTO answer(InvocationOnMock invocation) throws Throwable {
                        ComkerSpotDTO input = (ComkerSpotDTO) invocation.getArguments()[0];
                        ComkerSpotDTO target = new ComkerSpotDTO(
                                input.getCode(),
                                input.getName(),
                                input.getDescription());
                        target.setId(targetId);
                        target.setModuleIds(input.getModuleIds());
                        return target;
                    }
                });
        
        mockMvc.perform(post("/comker/adm/spot")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(targetId)))
                .andExpect(jsonPath("$.code", Matchers.is(source.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                .andExpect(jsonPath("$.moduleIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.moduleIds[0]", Matchers.is(source.getModuleIds()[0])))
                .andExpect(jsonPath("$.moduleIds[3]", Matchers.is(source.getModuleIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(spotStorage, Mockito.times(1)).create(Mockito.any(ComkerSpotDTO.class));
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    @Test
    public void updateSpot_ok() throws Exception {
        final ComkerSpotDTO source = new ComkerSpotDTO(
                "SPOT_1",
                "Spot 1",
                "Description Spot 1");
        source.setId(UUID.randomUUID().toString());
        source.setModuleIds(new String[] {
            "MODULE_1", "MODULE_2", "MODULE_3", "MODULE_4"
        });
        
        final ComkerSpotDTO target = new ComkerSpotDTO(
                                source.getCode(),
                                source.getName(),
                                source.getDescription());
        target.setId(source.getId());
        target.setModuleIds(new String[] {
            "MODULE_1", "MODULE_2", "MODULE_3", "MODULE_4"
        });
 
        Mockito.doAnswer(new Answer() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        ComkerSpotDTO input = (ComkerSpotDTO) invocation.getArguments()[0];
                        Assert.assertEquals(source.getId(), input.getId());
                        Assert.assertEquals(source.getCode(), input.getCode());
                        Assert.assertEquals(source.getName(), input.getName());
                        Assert.assertEquals(source.getDescription(), input.getDescription());
                        Assert.assertArrayEquals(source.getModuleIds(), input.getModuleIds());
                        return null;
                    }
                }).when(spotStorage).update(Mockito.any(ComkerSpotDTO.class));
        
        Mockito.when(spotStorage.get(source.getId())).thenReturn(target);
        
        mockMvc.perform(put("/comker/adm/spot/{id}", source.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(ComkerDataUtil.convertObjectToJson(source)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(source.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(source.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(source.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(source.getDescription())))
                .andExpect(jsonPath("$.moduleIds", Matchers.hasSize(4)))
                .andExpect(jsonPath("$.moduleIds[0]", Matchers.is(source.getModuleIds()[0])))
                .andExpect(jsonPath("$.moduleIds[3]", Matchers.is(source.getModuleIds()[3])))
                //.andDo(print())
                .andReturn();
        
        Mockito.verify(spotStorage, Mockito.times(1)).update(Mockito.any(ComkerSpotDTO.class));
        Mockito.verify(spotStorage, Mockito.times(1)).get(source.getId());
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
    
    @Test
    public void deleteSpot_ok() throws Exception {
 
        final ComkerSpotDTO target = new ComkerSpotDTO(
                "SPOT_CODE", 
                "Spot name", 
                "Spot Description");
        target.setId(UUID.randomUUID().toString());
        target.setModuleIds(new String[] {
            "MODULE_1"
        });
        
        Mockito.when(spotStorage.get(target.getId())).thenReturn(target);
        Mockito.doNothing().when(spotStorage).delete(target.getId());
 
        mockMvc.perform(delete("/comker/adm/spot/{id}", target.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(target.getId())))
                .andExpect(jsonPath("$.code", Matchers.is(target.getCode())))
                .andExpect(jsonPath("$.name", Matchers.is(target.getName())))
                .andExpect(jsonPath("$.description", Matchers.is(target.getDescription())))
                .andExpect(jsonPath("$.moduleIds", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.moduleIds[0]", Matchers.is(target.getModuleIds()[0])))
                //.andDo(print())
                .andReturn();
 
        Mockito.verify(spotStorage, Mockito.times(1)).get(target.getId());
        Mockito.verify(spotStorage, Mockito.times(1)).delete(target.getId());
        Mockito.verifyNoMoreInteractions(spotStorage);
    }
}
