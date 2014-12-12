 package net.cokkee.comker.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerRoleResourceImpl;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.ComkerRoleStorage;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
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
@ContextConfiguration(locations = {"classpath:/api/ComkerRoleResourceUnitTest.xml"})
public class ComkerRoleResourceUnitTest {

    @Autowired
    protected ComkerRoleResource roleClient;

    @Autowired
    @InjectMocks
    private ComkerRoleResourceImpl roleServer;

    @Mock
    private ComkerSessionService sessionService;

    @Mock
    private ComkerRoleStorage roleStorage;

    private List<String> roleIdx = new ArrayList<String>();

    private Map<String,ComkerRoleDTO> roleMap = new HashMap<String,ComkerRoleDTO>();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<10; i++) {
            ComkerRoleDTO role = new ComkerRoleDTO("ROLE_" + i, "Role " + i, null);
            role.setId(UUID.randomUUID().toString());
            roleMap.put(role.getId(), role);
            roleIdx.add(role.getId());
        }

        Mockito.when(roleStorage.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return roleMap.size();
            }
        });

        Mockito.when(roleStorage.findAll(Mockito.any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerRoleDTO>>() {
            @Override
            public List<ComkerRoleDTO> answer(InvocationOnMock invocation) throws Throwable {
                ComkerQueryPager p = (ComkerQueryPager) invocation.getArguments()[0];
                List<ComkerRoleDTO> result = new ArrayList<ComkerRoleDTO>(roleMap.values());

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

        Mockito.when(roleStorage.get(Mockito.anyString()))
                .thenAnswer(new Answer<ComkerRoleDTO>() {
            @Override
            public ComkerRoleDTO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                if (roleMap.containsKey(id)) {
                    return roleMap.get(id);
                }
                throw new ComkerObjectNotFoundException("role_not_found");
            }
        });

        Mockito.doAnswer(new Answer<ComkerRoleDTO>() {
            @Override
            public ComkerRoleDTO answer(InvocationOnMock invocation) throws Throwable {
                ComkerRoleDTO role = (ComkerRoleDTO) invocation.getArguments()[0];
                role.setId(UUID.randomUUID().toString());
                roleMap.put(role.getId(), role);
                roleIdx.add(role.getId());
                return role;
            }
        }).when(roleStorage).create(Mockito.any(ComkerRoleDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRoleDTO role = (ComkerRoleDTO) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(role, roleMap.get(role.getId()));
                return null;
            }
        }).when(roleStorage).update(Mockito.any(ComkerRoleDTO.class));

        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                roleMap.remove(id);
                roleIdx.remove(id);
                return null;
            }
        }).when(roleStorage).delete(Mockito.anyString());

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
        Response resp = roleClient.getRoleList(null, null, null);
        Mockito.verify(sessionService).getPager(ComkerRoleDTO.class, null, null);
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerRoleDTO.Pack pack = resp.readEntity(ComkerRoleDTO.Pack.class);
        Assert.assertTrue(pack.getTotal() == roleMap.size());
        Assert.assertTrue(pack.getCollection().size() <= roleMap.size());
        Assert.assertTrue(pack.getCollection().size() <= ComkerQueryPager.DEFAULT_LIMIT);

        List<String> resultIds = new ArrayList<String>();
        List<ComkerRoleDTO> collection = pack.getCollection();
        for(ComkerRoleDTO item:collection) {
            resultIds.add(item.getId());
            Assert.assertEquals(item.getCode(), roleMap.get(item.getId()).getCode());
        }

        List<String> sourceIds = new ArrayList<String>(roleIdx);
        Collections.sort(sourceIds);
        Collections.sort(resultIds);

        Assert.assertThat(sourceIds, CoreMatchers.is(resultIds));
    }
    
    @Test
    public void test_get_role_item() {
        int position = 3;
        Response resp = roleClient.getRoleItem(roleIdx.get(position));
        Assert.assertTrue(resp.getStatus() == 200);
        Assert.assertTrue(resp.hasEntity());

        ComkerRoleDTO role = resp.readEntity(ComkerRoleDTO.class);
        Assert.assertEquals("ROLE_" + position, role.getCode());
        Assert.assertEquals("Role " + position, role.getName());
    }

    @Test
    public void test_get_role_item_with_invalid_id() {
        Response resp = roleClient.getRoleItem("ID_INVALID");
        Assert.assertTrue(resp.getStatus() == ComkerObjectNotFoundException.CODE);
    }

    @Test
    public void test_create_role() {
        int position = roleMap.size();
        ComkerRoleDTO source = new ComkerRoleDTO(
                "ROLE_" + position,
                "Role " + position,
                null);

        Response resp = roleClient.createRoleItem(source);
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerRoleDTO result = resp.readEntity(ComkerRoleDTO.class);
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getCode(), result.getCode());
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());
    }

    @Test
    public void test_update_role() {
        ComkerRoleDTO source = roleMap.get(roleIdx.get(5));

        ComkerRoleDTO actual = new ComkerRoleDTO(
                source.getCode() + "_UPDATED",
                source.getName() + " - updated",
                source.getDescription());
        actual.setId(source.getId());
        actual.setPermissionIds(new String[] {
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()});

        Response resp = roleClient.updateRoleItem(source.getId(), actual);
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerRoleDTO result = resp.readEntity(ComkerRoleDTO.class);

        assertEquals(actual, result);
    }

    @Test
    public void test_delete_role() {
        ComkerRoleDTO source = roleMap.get(roleIdx.get(5));

        Response resp = roleClient.deleteRoleItem(source.getId());
        Assert.assertTrue(resp.getStatus() == 200);
        ComkerRoleDTO result = resp.readEntity(ComkerRoleDTO.class);

        assertEquals(source, result);
    }

    private void assertEquals(ComkerRoleDTO source, ComkerRoleDTO result) {
        Assert.assertTrue(result.getId().length() > 0);
        Assert.assertEquals(source.getCode(), result.getCode());
        Assert.assertEquals(source.getName(), result.getName());
        Assert.assertEquals(source.getDescription(), result.getDescription());

        Assert.assertThat(source.getPermissionIds(), CoreMatchers.is(result.getPermissionIds()));
    }
}
