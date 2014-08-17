 package net.cokkee.comker.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerCrewResourceImpl;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerCrewStorage;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.structure.ComkerKeyAndValueSet;
import net.cokkee.comker.util.ComkerDataUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/api/ComkerCrewResourceUnitTest.xml"})
public class ComkerCrewResourceUnitTest {

    @Autowired
    protected ComkerCrewResource crewClient;

    @Autowired
    @InjectMocks
    private ComkerCrewResourceImpl crewServer;

    @Mock
    private ComkerSessionService sessionService;

    @Mock
    private ComkerCrewStorage crewStorage;

    private List<String> crewIdx = new ArrayList<String>();

    private Map<String,ComkerCrewDTO> crewMap = new HashMap<String,ComkerCrewDTO>();

    private List<String> globalRoleIdx = new ArrayList<String>();

    private static List<String> spotIdx = new ArrayList<String>();

    private static List<String> roleIdx = new ArrayList<String>();

    private static ComkerKeyAndValueSet[] keyValues = null;

    @BeforeClass
    public static void initClass() {
        for(int i=0; i<5; i++) {
            spotIdx.add(UUID.randomUUID().toString());
        }

        for(int i=0; i<30; i++) {
            roleIdx.add(UUID.randomUUID().toString());
        }

        keyValues = new ComkerKeyAndValueSet[5];
        for(int j=0; j<5; j++) {
            String[] values = new String[6];
            for(int k=0; k<6; k++) {
                values[k] = roleIdx.get(j*6 + k);
            }
            keyValues[j] = new ComkerKeyAndValueSet(spotIdx.get(j), values);
        }
    }

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<5; i++) {
            globalRoleIdx.add(UUID.randomUUID().toString());
        }

        for(int i=0; i<10; i++) {
            ComkerCrewDTO crew = new ComkerCrewDTO("Crew " + i, "Description of crew#" + i);
            crew.setId(UUID.randomUUID().toString());

            if (i % 3 == 0) {
                crew.setGlobalRoleIds(globalRoleIdx.toArray(new String[0]));
                crew.setScopedRoleIds(keyValues);
            }

            crewMap.put(crew.getId(), crew);
            crewIdx.add(crew.getId());
        }

        Mockito.when(crewStorage.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return crewMap.size();
            }
        });

        Mockito.when(crewStorage.findAll(Mockito.any(ComkerPager.class)))
                .thenAnswer(new Answer<List<ComkerCrewDTO>>() {
            @Override
            public List<ComkerCrewDTO> answer(InvocationOnMock invocation) throws Throwable {
                ComkerPager p = (ComkerPager) invocation.getArguments()[0];
                List<ComkerCrewDTO> result = new ArrayList<ComkerCrewDTO>(crewMap.values());

                if (p != null) {
                    int k = (p.getStart() != null) ? p.getStart().intValue() : 0;

                    for(int i = 0; i<k; i++) {
                        result.remove(0);
                    }

                    int t = (p.getLimit() != null) ? p.getLimit().intValue() : Integer.MAX_VALUE;

                    for(int i = result.size() - 1; i>=t; i--) {
                        result.remove(i);
                    }
                }

                return result;
            }
        });

        Mockito.when(crewStorage.get(Mockito.anyString()))
                .thenAnswer(new Answer<ComkerCrewDTO>() {
            @Override
            public ComkerCrewDTO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                if (crewMap.containsKey(id)) {
                    return crewMap.get(id);
                }
                throw new ComkerObjectNotFoundException("crew_not_found");
            }
        });

        Mockito.doAnswer(new Answer<ComkerCrewDTO>() {
            @Override
            public ComkerCrewDTO answer(InvocationOnMock invocation) throws Throwable {
                ComkerCrewDTO crew = (ComkerCrewDTO) invocation.getArguments()[0];
                crew.setId(UUID.randomUUID().toString());
                crewMap.put(crew.getId(), crew);
                crewIdx.add(crew.getId());
                return crew;
            }
        }).when(crewStorage).create(Mockito.any(ComkerCrewDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerCrewDTO crew = (ComkerCrewDTO) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(crew, crewMap.get(crew.getId()));
                return null;
            }
        }).when(crewStorage).update(Mockito.any(ComkerCrewDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                crewMap.remove(id);
                crewIdx.remove(id);
                return null;
            }
        }).when(crewStorage).delete(Mockito.anyString());

        Mockito.when(sessionService.getPager(Mockito.any(Class.class),
                    Mockito.any(Integer.class), Mockito.any(Integer.class)))
                .thenAnswer(new Answer<ComkerPager>() {
            @Override
            public ComkerPager answer(InvocationOnMock invocation) throws Throwable {
                Integer start = (Integer) invocation.getArguments()[1];
                Integer limit = (Integer) invocation.getArguments()[2];
                ComkerPager pager = new ComkerPager();
                if (start != null) pager.setStart(start);
                if (limit != null) pager.setLimit(limit);
                return pager;
            }
        });
    }

    @Test
    public void test_find_all() {
        Response resp = crewClient.getCrewList(null, null, null);
        Mockito.verify(sessionService).getPager(ComkerCrewDTO.class, null, null);
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerCrewDTO.Pack pack = resp.readEntity(ComkerCrewDTO.Pack.class);
        Assert.assertTrue(pack.getTotal() == crewMap.size());
        Assert.assertTrue(pack.getCollection().size() <= crewMap.size());
        Assert.assertTrue(pack.getCollection().size() <= ComkerPager.DEFAULT_LIMIT);

        List<String> resultIds = new ArrayList<String>();
        List<ComkerCrewDTO> collection = pack.getCollection();
        for(ComkerCrewDTO item:collection) {
            resultIds.add(item.getId());
            Assert.assertEquals(item.getName(), crewMap.get(item.getId()).getName());
        }

        List<String> sourceIds = new ArrayList<String>(crewIdx);
        Collections.sort(sourceIds);
        Collections.sort(resultIds);

        Assert.assertThat(sourceIds, CoreMatchers.is(resultIds));
    }
    
    @Test
    public void test_get_crew_item() {
        int position = 3;
        Response resp = crewClient.getCrewItem(crewIdx.get(position));
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerCrewDTO crew = resp.readEntity(ComkerCrewDTO.class);
        Assert.assertEquals("Crew " + position, crew.getName());
        Assert.assertEquals("Description of crew#" + position, crew.getDescription());

        Assert.assertThat(Arrays.asList(crew.getGlobalRoleIds()), CoreMatchers.is(globalRoleIdx));

        for(int i=0; i<5; i++) {
            Assert.assertEquals(crew.getScopedRoleIds()[i].getKey(), spotIdx.get(i));
            for(int j=0; j<6; j++) {
                Assert.assertEquals(crew.getScopedRoleIds()[i].getValues()[j], roleIdx.get(i*6+j));
            }
        }
    }

    @Test
    public void test_get_crew_item_with_invalid_id() {
        Response resp = crewClient.getCrewItem("ID_INVALID");
        Assert.assertTrue(resp.getStatus() == ComkerObjectNotFoundException.CODE);
    }

    @Test
    public void test_create_crew() {
        int position = crewMap.size();
        ComkerCrewDTO source = new ComkerCrewDTO(
                "Crew " + position,
                "Description of crew " + position);

        Response resp = crewClient.createCrewItem(source);
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerCrewDTO result = resp.readEntity(ComkerCrewDTO.class);
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());
    }

    @Test
    public void test_update_crew() {
        ComkerCrewDTO source = crewMap.get(crewIdx.get(5));

        ComkerCrewDTO actual = new ComkerCrewDTO(
                source.getName() + " - updated",
                source.getDescription());
        actual.setId(source.getId());
        actual.setGlobalRoleIds(new String[] {
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()});

        Response resp = crewClient.updateCrewItem(source.getId(), actual);
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerCrewDTO result = resp.readEntity(ComkerCrewDTO.class);

        assertEquals(actual, result);
    }

    @Test
    public void test_delete_crew() {
        ComkerCrewDTO source = crewMap.get(crewIdx.get(5));

        Response resp = crewClient.deleteCrewItem(source.getId());
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerCrewDTO result = resp.readEntity(ComkerCrewDTO.class);

        assertEquals(source, result);
    }

    private void assertEquals(ComkerCrewDTO source, ComkerCrewDTO result) {
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());

        Assert.assertThat(source.getGlobalRoleIds(), CoreMatchers.is(result.getGlobalRoleIds()));
    }
}
