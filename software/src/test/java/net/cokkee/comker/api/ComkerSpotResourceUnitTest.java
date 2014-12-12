 package net.cokkee.comker.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerSpotResourceImpl;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.service.ComkerSessionService;
import net.cokkee.comker.util.ComkerDataUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
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
@ContextConfiguration(locations = {"classpath:/api/ComkerSpotResourceUnitTest.xml"})
public class ComkerSpotResourceUnitTest {

    @Autowired
    protected ComkerSpotResource spotClient;

    @Autowired
    @InjectMocks
    private ComkerSpotResourceImpl spotServer;

    @Mock
    private ComkerSessionService sessionService;

    @Mock
    private ComkerSpotStorage spotStorage;

    private List<String> spotIdx = new ArrayList<String>();

    private Map<String,ComkerSpotDTO> spotMap = new HashMap<String,ComkerSpotDTO>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<10; i++) {
            ComkerSpotDTO spot = new ComkerSpotDTO("SPOT_" + i, "Spot " + i, null);
            spot.setId(UUID.randomUUID().toString());
            spotMap.put(spot.getId(), spot);
            spotIdx.add(spot.getId());
        }

        Mockito.when(spotStorage.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return spotMap.size();
            }
        });

        Mockito.when(spotStorage.findAll(Mockito.any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerSpotDTO>>() {
            @Override
            public List<ComkerSpotDTO> answer(InvocationOnMock invocation) throws Throwable {
                ComkerQueryPager p = (ComkerQueryPager) invocation.getArguments()[0];
                List<ComkerSpotDTO> result = new ArrayList<ComkerSpotDTO>(spotMap.values());

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

        Mockito.when(spotStorage.get(Mockito.anyString()))
                .thenAnswer(new Answer<ComkerSpotDTO>() {
            @Override
            public ComkerSpotDTO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                if (spotMap.containsKey(id)) {
                    return spotMap.get(id);
                }
                throw new ComkerObjectNotFoundException("spot_not_found");
            }
        });

        Mockito.doAnswer(new Answer<ComkerSpotDTO>() {
            @Override
            public ComkerSpotDTO answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpotDTO spot = (ComkerSpotDTO) invocation.getArguments()[0];
                spot.setId(UUID.randomUUID().toString());
                spotMap.put(spot.getId(), spot);
                spotIdx.add(spot.getId());
                return spot;
            }
        }).when(spotStorage).create(Mockito.any(ComkerSpotDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerSpotDTO spot = (ComkerSpotDTO) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(spot, spotMap.get(spot.getId()));
                return null;
            }
        }).when(spotStorage).update(Mockito.any(ComkerSpotDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                spotMap.remove(id);
                spotIdx.remove(id);
                return null;
            }
        }).when(spotStorage).delete(Mockito.anyString());

        Mockito.when(sessionService.getPager(Mockito.any(Class.class),
                    Mockito.any(Integer.class), Mockito.any(Integer.class)))
                .thenAnswer(new Answer<ComkerQueryPager>() {
            @Override
            public ComkerQueryPager answer(InvocationOnMock invocation) throws Throwable {
                Integer start = (Integer) invocation.getArguments()[1];
                Integer limit = (Integer) invocation.getArguments()[2];
                ComkerQueryPager pager = new ComkerQueryPager();
                if (start != null) pager.setStart(start);
                if (limit != null) pager.setLimit(limit);
                return pager;
            }
        });
    }

    @Test
    public void test_find_all() {
        Response resp = spotClient.getSpotList(null, null, null);
        Mockito.verify(sessionService).getPager(ComkerSpotDTO.class, null, null);
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerSpotDTO.Pack pack = resp.readEntity(ComkerSpotDTO.Pack.class);
        Assert.assertTrue(pack.getTotal() == spotMap.size());
        Assert.assertTrue(pack.getCollection().size() <= spotMap.size());
        Assert.assertTrue(pack.getCollection().size() <= ComkerQueryPager.DEFAULT_LIMIT);

        List<String> resultIds = new ArrayList<String>();
        List<ComkerSpotDTO> collection = pack.getCollection();
        for(ComkerSpotDTO item:collection) {
            resultIds.add(item.getId());
            Assert.assertEquals(item.getCode(), spotMap.get(item.getId()).getCode());
        }

        List<String> sourceIds = new ArrayList<String>(spotIdx);
        Collections.sort(sourceIds);
        Collections.sort(resultIds);

        Assert.assertThat(sourceIds, CoreMatchers.is(resultIds));
    }
    
    @Test
    public void test_get_spot_item() {
        int position = 3;
        Response resp = spotClient.getSpotItem(spotIdx.get(position));
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerSpotDTO spot = resp.readEntity(ComkerSpotDTO.class);
        Assert.assertEquals("SPOT_" + position, spot.getCode());
        Assert.assertEquals("Spot " + position, spot.getName());
    }

    @Test
    public void test_get_spot_item_with_invalid_id() {
        Response resp = spotClient.getSpotItem("ID_INVALID");
        Assert.assertTrue(resp.getStatus() == 400);
    }

    @Test
    public void test_create_spot() {
        int position = spotMap.size();
        ComkerSpotDTO source = new ComkerSpotDTO(
                "SPOT_" + position,
                "Spot " + position,
                null);

        Response resp = spotClient.createSpotItem(source);
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerSpotDTO result = resp.readEntity(ComkerSpotDTO.class);
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getCode(), result.getCode());
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());
    }

    @Test
    public void test_update_spot() {
        ComkerSpotDTO source = spotMap.get(spotIdx.get(5));

        ComkerSpotDTO actual = new ComkerSpotDTO(
                source.getCode() + "_UPDATED",
                source.getName() + " - updated",
                source.getDescription());
        actual.setId(source.getId());
        actual.setModuleIds(new String[] {
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()});

        Response resp = spotClient.updateSpotItem(source.getId(), actual);
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerSpotDTO result = resp.readEntity(ComkerSpotDTO.class);

        assertEquals(actual, result);
    }

    @Test
    public void test_delete_spot() {
        ComkerSpotDTO source = spotMap.get(spotIdx.get(5));

        Response resp = spotClient.deleteSpotItem(source.getId());
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerSpotDTO result = resp.readEntity(ComkerSpotDTO.class);

        assertEquals(source, result);
    }

    private void assertEquals(ComkerSpotDTO source, ComkerSpotDTO result) {
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getCode(), result.getCode());
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());

        Assert.assertThat(source.getModuleIds(), CoreMatchers.is(result.getModuleIds()));
    }
}
