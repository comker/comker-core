 package net.cokkee.comker.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerSpotResourceImpl;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerSpotStorage;
import net.cokkee.comker.model.ComkerExceptionResponse;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.service.ComkerSessionService;
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

    private Map<String,ComkerSpotDTO> spotMap = new HashMap<String,ComkerSpotDTO>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<10; i++) {
            ComkerSpotDTO spot = new ComkerSpotDTO("SPOT_" + i, "Spot " + i, null);
            spot.setId("ID_" + spot.getCode());
            spotMap.put(spot.getId(), spot);
        }

        Mockito.when(spotStorage.findAll(Mockito.any(ComkerPager.class)))
                .thenAnswer(new Answer<List<ComkerSpotDTO>>() {
            @Override
            public List<ComkerSpotDTO> answer(InvocationOnMock invocation) throws Throwable {
                ComkerPager pager = (ComkerPager) invocation.getArguments()[0];
                return new ArrayList<ComkerSpotDTO>(spotMap.values());
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

        Mockito.when(sessionService.getSpotListPager())
                .thenAnswer(new Answer<ComkerPager>() {
            @Override
            public ComkerPager answer(InvocationOnMock invocation) throws Throwable {
                return new ComkerPager();
            }
        });
    }

    @Test
    public void test_find_all() {
        Response resp = spotClient.getSpotList();
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        final GenericType<List<ComkerSpotDTO>> genericType = new GenericType<List<ComkerSpotDTO>>() {};
        List<ComkerSpotDTO> list = resp.readEntity(genericType);
        Assert.assertTrue(list.size() == spotMap.size());
        System.out.println(list.get(0).getCode());
    }
    
    @Test
    public void test_get_spot_item() {
        Response resp = spotClient.getSpotItem("ID_SPOT_3");
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerSpotDTO spot = resp.readEntity(ComkerSpotDTO.class);
        Assert.assertEquals("SPOT_3", spot.getCode());
        Assert.assertEquals("Spot 3", spot.getName());
    }

    @Test
    public void test_get_spot_item_with_invalid_id() {
        Response resp = spotClient.getSpotItem("ID_INVALID");
        Assert.assertTrue(resp.getStatus() == ComkerObjectNotFoundException.CODE);
    }

    @Test
    public void test_create_spot() {
        ComkerSpotDTO spot = new ComkerSpotDTO("SPOT_6", "Spot 6", "Description spot 6");
        Response resp = spotClient.createSpotItem(spot);
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerSpotDTO result = resp.readEntity(ComkerSpotDTO.class);
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(spot.getCode(), result.getCode());
        Assert.assertEquals(spot.getName(), result.getName());
        Assert.assertEquals(spot.getDescription(), result.getDescription());
    }

    @Test
    public void test_create_spot_with_empty_code() {
        //TODO: implementation
    }
    
    @Test
    public void test_create_spot_with_duplicated_code() {
        //TODO: implementation
    }

    @Test
    public void test_update_spot() {
        //TODO: implementation
    }

    @Test
    public void test_update_spot_with_empty_code() {
        //TODO: implementation
    }

    @Test
    public void test_update_spot_with_duplicated_code() {
        //TODO: implementation
    }

    @Test
    public void test_delete_spot() {
        //TODO: implementation
    }

    @Test
    public void test_delete_spot_with_invalid_id() {
        //TODO: implementation
    }
}
