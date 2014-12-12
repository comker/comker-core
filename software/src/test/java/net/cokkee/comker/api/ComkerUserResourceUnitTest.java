 package net.cokkee.comker.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerUserResourceImpl;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dto.ComkerUserDTO;
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
@ContextConfiguration(locations = {"classpath:/api/ComkerUserResourceUnitTest.xml"})
public class ComkerUserResourceUnitTest {

    @Autowired
    protected ComkerUserResource userClient;

    @Autowired
    @InjectMocks
    private ComkerUserResourceImpl userServer;

    @Mock
    private ComkerSessionService sessionService;

    @Mock
    private ComkerUserStorage userStorage;

    private List<String> userIdx = new ArrayList<String>();

    private Map<String,ComkerUserDTO> userMap = new HashMap<String,ComkerUserDTO>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<10; i++) {
            ComkerUserDTO user = new ComkerUserDTO(
                    "email" + i + "@gmail.com",
                    "username" + i,
                    "password",
                    "User with name " + i);
            user.setId(UUID.randomUUID().toString());
            userMap.put(user.getId(), user);
            userIdx.add(user.getId());
        }

        Mockito.when(userStorage.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return userMap.size();
            }
        });

        Mockito.when(userStorage.findAll(Mockito.any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerUserDTO>>() {
            @Override
            public List<ComkerUserDTO> answer(InvocationOnMock invocation) throws Throwable {
                ComkerQueryPager p = (ComkerQueryPager) invocation.getArguments()[0];
                List<ComkerUserDTO> result = new ArrayList<ComkerUserDTO>(userMap.values());

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

        Mockito.when(userStorage.get(Mockito.anyString()))
                .thenAnswer(new Answer<ComkerUserDTO>() {
            @Override
            public ComkerUserDTO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                if (userMap.containsKey(id)) {
                    return userMap.get(id);
                }
                throw new ComkerObjectNotFoundException("user_not_found");
            }
        });

        Mockito.doAnswer(new Answer<ComkerUserDTO>() {
            @Override
            public ComkerUserDTO answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDTO user = (ComkerUserDTO) invocation.getArguments()[0];
                user.setId(UUID.randomUUID().toString());
                userMap.put(user.getId(), user);
                userIdx.add(user.getId());
                return user;
            }
        }).when(userStorage).create(Mockito.any(ComkerUserDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDTO user = (ComkerUserDTO) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(user, userMap.get(user.getId()));
                return null;
            }
        }).when(userStorage).update(Mockito.any(ComkerUserDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                userMap.remove(id);
                userIdx.remove(id);
                return null;
            }
        }).when(userStorage).delete(Mockito.anyString());

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
        Response resp = userClient.getUserList(null, null);
        Mockito.verify(sessionService).getPager(ComkerUserDTO.class, null, null);
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerUserDTO.Pack pack = resp.readEntity(ComkerUserDTO.Pack.class);
        Assert.assertTrue(pack.getTotal() == userMap.size());
        Assert.assertTrue(pack.getCollection().size() <= userMap.size());
        Assert.assertTrue(pack.getCollection().size() <= ComkerQueryPager.DEFAULT_LIMIT);

        List<String> resultIds = new ArrayList<String>();
        List<ComkerUserDTO> collection = pack.getCollection();
        for(ComkerUserDTO item:collection) {
            resultIds.add(item.getId());
            Assert.assertEquals(item.getUsername(), userMap.get(item.getId()).getUsername());
        }

        List<String> sourceIds = new ArrayList<String>(userIdx);
        Collections.sort(sourceIds);
        Collections.sort(resultIds);

        Assert.assertThat(sourceIds, CoreMatchers.is(resultIds));
    }
    
    @Test
    public void test_get_user_item() {
        int position = 3;
        Response resp = userClient.getUserItem(userIdx.get(position));
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerUserDTO user = resp.readEntity(ComkerUserDTO.class);
        Assert.assertEquals("email" + position + "@gmail.com", user.getEmail());
        Assert.assertEquals("username" + position, user.getUsername());
    }

    @Test
    public void test_get_user_item_with_invalid_id() {
        Response resp = userClient.getUserItem("ID_INVALID");
        Assert.assertTrue(resp.getStatus() == ComkerObjectNotFoundException.CODE);
    }

    @Test
    public void test_create_user() {
        int position = userMap.size();
        ComkerUserDTO source = new ComkerUserDTO(
                "email" + position + "@gmail.com",
                "username" + position,
                "password",
                "Username " + position);

        Response resp = userClient.createUserItem(source);
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerUserDTO result = resp.readEntity(ComkerUserDTO.class);
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getEmail(), result.getEmail());
        Assert.assertEquals(source.getUsername(), result.getUsername());
        Assert.assertEquals(source.getPassword(), result.getPassword());
        Assert.assertEquals(source.getFullname(), result.getFullname());
    }

    @Test
    public void test_update_user() {
        ComkerUserDTO source = userMap.get(userIdx.get(5));

        ComkerUserDTO actual = new ComkerUserDTO(
                "updated_" + source.getEmail(),
                "updated_" + source.getUsername(),
                "newpassword",
                source.getFullname());
        actual.setId(source.getId());
        actual.setCrewIds(new String[] {
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()});

        Response resp = userClient.updateUserItem(source.getId(), actual);
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerUserDTO result = resp.readEntity(ComkerUserDTO.class);

        assertEquals(actual, result);
    }

    @Test
    public void test_delete_user() {
        ComkerUserDTO source = userMap.get(userIdx.get(5));

        Response resp = userClient.deleteUserItem(source.getId());
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerUserDTO result = resp.readEntity(ComkerUserDTO.class);

        assertEquals(source, result);
    }

    private void assertEquals(ComkerUserDTO source, ComkerUserDTO result) {
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getEmail(), result.getEmail());
        Assert.assertEquals(source.getUsername(), result.getUsername());
        Assert.assertEquals(source.getPassword(), result.getPassword());
        Assert.assertEquals(source.getFullname(), result.getFullname());

        Assert.assertThat(source.getCrewIds(), CoreMatchers.is(result.getCrewIds()));
    }
}
