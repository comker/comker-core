package net.cokkee.comker.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.impl.ComkerCrewStorageImpl;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerCrewDTO;

import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRole;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.structure.ComkerKeyAndValueSet;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerCrewValidator;
import org.hamcrest.CoreMatchers;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.hasItems;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ComkerCrewStorageUnitTest {

    private List<String> crewIdx = new ArrayList<String>();
    private List<String> spotIdx = new ArrayList<String>();
    private List<String> roleIdx = new ArrayList<String>();
    private List<String> permissionIdx = new ArrayList<String>();

    private Map<String, ComkerCrew> crewMap = new HashMap<String, ComkerCrew>();
    private Map<String, ComkerSpot> spotMap = new HashMap<String, ComkerSpot>();
    private Map<String, ComkerRole> roleMap = new HashMap<String, ComkerRole>();
    private Map<String, ComkerPermission> permissionMap = new HashMap<String, ComkerPermission>();

    @InjectMocks
    private ComkerCrewStorageImpl crewStorage;

    @InjectMocks
    private ComkerCrewValidator crewValidator;

    @Mock
    private ComkerCrewDao crewDao;

    @Mock
    private ComkerRoleDao roleDao;

    @Mock
    private ComkerSpotDao spotDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        crewStorage.setCrewValidator(crewValidator);
        
        /*
         * Demo data for spotDao
         */
        for(int i=0; i<3; i++) {
            ComkerSpot spot = new ComkerSpot("SPOT_0" + i, "Spot 0" + i, "This is spot-0" + i);
            spot.setId(UUID.randomUUID().toString());
            spotMap.put(spot.getId(), spot);
            spotIdx.add(spot.getId());
        }

        when(spotDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return spotMap.containsKey(id);
            }
        });

        when(spotDao.get(anyString())).thenAnswer(new Answer<ComkerSpot>() {
            @Override
            public ComkerSpot answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return spotMap.get(id);
            }
        });

        /*
         * Demo data for permissionDao
         */
        for(int i=0; i<9; i++) {
            ComkerPermission permission = new ComkerPermission("PERMISSION_0" + i);
            permission.setId(UUID.randomUUID().toString());
            permissionMap.put(permission.getId(), permission);
            permissionIdx.add(permission.getId());
        }

        /*
         * Demo data for roleDao
         */
        ComkerRole role;

        for(int i=0; i<7; i++) {
            role = new ComkerRole("ROLE_0" + i, "Role 0" + i, "This is role-0" + i);
            role.setId(UUID.randomUUID().toString());
            roleMap.put(role.getId(), role);
            roleIdx.add(role.getId());
        }

        role = roleMap.get(roleIdx.get(1));
        for(int j=1; j<=2; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get(permissionIdx.get(j))));
        }
        
        role = roleMap.get(roleIdx.get(2));
        for(int j=1; j<=4; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get(permissionIdx.get(j))));
        }

        role = roleMap.get(roleIdx.get(4));
        for(int j=5; j<=7; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get(permissionIdx.get(j))));
        }

        role = roleMap.get(roleIdx.get(5));
        for(int j=6; j<=8; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get(permissionIdx.get(j))));
        }

        when(roleDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleMap.containsKey(id);
            }
        });
        
        when(roleDao.get(anyString())).thenAnswer(new Answer<ComkerRole>() {
            @Override
            public ComkerRole answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleMap.get(id);
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerPermission> bag = (Set<ComkerPermission>) invocation.getArguments()[0];
                ComkerRole role = (ComkerRole) invocation.getArguments()[1];
                List<ComkerRoleJoinPermission> list = role.getRoleJoinPermissionList();
                for (ComkerRoleJoinPermission item : list) {
                    bag.add(item.getPermission());
                }
                return null;
            }
        }).when(roleDao).collectPermission(anySet(), any(ComkerRole.class));

        /*
         * Demo data for crewDao
         */
        ComkerCrew crew;

        for(int i=0; i<5; i++) {
            crew = new ComkerCrew("Crew 0" + i, "This is crew-0" + i);
            crew.setId(UUID.randomUUID().toString());
            crewMap.put(crew.getId(), crew);
            crewIdx.add(crew.getId());
        }

        crew = crewMap.get(crewIdx.get(1));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get(roleIdx.get(1))));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get(roleIdx.get(2))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get(roleIdx.get(4)), spotMap.get(spotIdx.get(1))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get(roleIdx.get(5)), spotMap.get(spotIdx.get(1))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get(roleIdx.get(4)), spotMap.get(spotIdx.get(2))));
        
        crew = crewMap.get(crewIdx.get(2));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get(roleIdx.get(1))));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get(roleIdx.get(3))));
        
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerRole> bag = (Set<ComkerRole>) invocation.getArguments()[0];
                ComkerCrew crew = (ComkerCrew) invocation.getArguments()[1];
                List<ComkerCrewJoinGlobalRole> list = crew.getCrewJoinGlobalRoleList();
                for (ComkerCrewJoinGlobalRole item : list) {
                    bag.add(item.getRole());
                }
                return null;
            }
        }).when(crewDao).collectGlobalRole(anySet(), any(ComkerCrew.class));

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<ComkerSpot, Set<ComkerRole>> bag = (Map<ComkerSpot, Set<ComkerRole>>) invocation.getArguments()[0];
                ComkerCrew crew = (ComkerCrew) invocation.getArguments()[1];
                List<ComkerCrewJoinRoleWithSpot> list = crew.getCrewJoinRoleWithSpotList();
                for (ComkerCrewJoinRoleWithSpot item : list) {
                    ComkerSpot spot = item.getSpot();
                    Set<ComkerRole> roleSet = bag.get(spot);
                    if (roleSet == null) {
                        roleSet = new HashSet<ComkerRole>();
                        bag.put(spot, roleSet);
                    }
                    roleSet.add(item.getRole());
                }
                return null;
            }
        }).when(crewDao).collectSpotWithRole(anyMap(), any(ComkerCrew.class));

        /*
         * mocks for count() method
         */
        when(crewDao.count()).thenAnswer(new Answer<Integer>() {

            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return crewMap.size();
            }
        });

        /*
         * mocks for findAll() method
         */
        when(crewDao.findAll(any(ComkerPager.class))).thenAnswer(new Answer<List<ComkerCrew>>() {

            @Override
            public List<ComkerCrew> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerCrew> result = new ArrayList<ComkerCrew>();
                for (Map.Entry<String, ComkerCrew> crewEntry : crewMap.entrySet()) {
                    result.add(crewEntry.getValue());
                }
                return result;
            }
        });

        /*
         * mocks for get() method
         */
        when(crewDao.get(anyString())).thenAnswer(new Answer<ComkerCrew>() {
            @Override
            public ComkerCrew answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return crewMap.get(id);
            }
        });

        doAnswer(new Answer<ComkerCrew>() {
            @Override
            public ComkerCrew answer(InvocationOnMock invocation) throws Throwable {
                ComkerCrew crew = (ComkerCrew) invocation.getArguments()[0];
                crew.setId(UUID.randomUUID().toString());
                crewMap.put(crew.getId(), crew);
                crewIdx.add(crew.getId());
                return crew;
            }
        }).when(crewDao).create(any(ComkerCrew.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerCrew crew = (ComkerCrew) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(crew, crewMap.get(crew.getId()));
                return null;
            }
        }).when(crewDao).update(any(ComkerCrew.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerCrew crew = (ComkerCrew) invocation.getArguments()[0];
                if (crew != null) {
                    crewMap.remove(crew.getId());
                    crewIdx.remove(crew.getId());
                }
                return null;
            }
        }).when(crewDao).delete(any(ComkerCrew.class));
    }

    @Test
    public void test_1_count() {
        Integer result = crewStorage.count();
        assertEquals(result.intValue(), crewMap.size());
    }

    @Test
    public void test_1_find_all_crew_objects() {
        List<ComkerCrewDTO> result = crewStorage.findAll(null);
        assertEquals(result.size(), crewMap.size());
    }

    @Test
    public void test_2_get_crew_object_by_id() {
        ComkerCrewDTO result = crewStorage.get(crewIdx.get(1));
        verify(crewDao).get(crewIdx.get(1));
        assertArrayEquals(new String[]{roleIdx.get(1), roleIdx.get(2)}, result.getGlobalRoleIds());

        ComkerKeyAndValueSet[] limitedSpotRoleIds = result.getScopedRoleIds();
        assertEquals(limitedSpotRoleIds.length, 2);
        for (ComkerKeyAndValueSet item : limitedSpotRoleIds) {
            if (item.getKey().equals(spotIdx.get(1))) {
                assertThat(item.getValues(), CoreMatchers.is(new String[] {roleIdx.get(4), roleIdx.get(5)}));
            } else {
                assertThat(item.getValues(), CoreMatchers.is(new String[] {roleIdx.get(4)}));
            }
        }
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_2_get_crew_object_by_wrong_id() {
        crewStorage.get("crew-not-found");
        verify(crewDao).get("crew-not-found");
    }

    @Test
    public void test_3_get_crew_global_authorities() {
        Set<String> result = crewStorage.getGlobalAuthorities(crewIdx.get(1));
        assertThat(result, hasItems("PERMISSION_01", "PERMISSION_02",
                "PERMISSION_03", "PERMISSION_04"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_3_get_crew_global_authorities_by_wrong_id() {
        crewStorage.getGlobalAuthorities("crew-not-found");
    }

    @Test
    public void test_3_get_crew_spot_code_with_authorities() {
        Map<String, Set<String>> result = crewStorage.getSpotCodeWithAuthorities(crewIdx.get(1));
        assertThat(result.keySet(), hasItems("SPOT_01", "SPOT_02"));

        assertNotNull(result.get("SPOT_01"));
        assertThat(result.get("SPOT_01"),
                hasItems("PERMISSION_05", "PERMISSION_06", "PERMISSION_07", "PERMISSION_08"));

        assertNotNull(result.get("SPOT_02"));
        assertThat(result.get("SPOT_02"),
                hasItems("PERMISSION_05", "PERMISSION_06", "PERMISSION_07"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_3_get_crew_spot_code_with_authorities_by_wrong_id() {
        crewStorage.getSpotCodeWithAuthorities("crew-not-found");
    }

    @Test
    public void test_4_create_crew_object() {
        ComkerCrewDTO param = new ComkerCrewDTO("A new crew", "A new crew with duplicated spot+role");

        param.setGlobalRoleIds(new String[] {roleIdx.get(1), roleIdx.get(2)});

        Set<ComkerKeyAndValueSet> spotAndRoles = new HashSet<ComkerKeyAndValueSet>();
        spotAndRoles.add(new ComkerKeyAndValueSet(spotIdx.get(1), new String[]{roleIdx.get(4), roleIdx.get(6)}));
        param.setScopedRoleIds(spotAndRoles.toArray(new ComkerKeyAndValueSet[0]));

        ComkerCrewDTO result = crewStorage.create(param);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getName(), param.getName());
        assertEquals(result.getDescription(), param.getDescription());

        Set<String> newGlobalRoleIds = new HashSet<String>();
        if (param.getGlobalRoleIds() != null) {
            newGlobalRoleIds.addAll(Arrays.asList(param.getGlobalRoleIds()));
        }
        for (String globalRoleId : result.getGlobalRoleIds()) {
            if (newGlobalRoleIds.contains(globalRoleId)) {
                newGlobalRoleIds.remove(globalRoleId);
            } else {
                assertTrue("Cannot found:" + globalRoleId, true);
            }
        }
        assertEquals(newGlobalRoleIds.size(), 0);

        for (ComkerKeyAndValueSet item : result.getScopedRoleIds()) {
            if (item.getKey().equals(spotIdx.get(1))) {
                assertThat(item.getValues(), CoreMatchers.is(new String[] {roleIdx.get(4), roleIdx.get(6)}));
            } else {
                assertTrue("Cannot found:" + item.getKey(), true);
            }
        }
        assertEquals(result.getScopedRoleIds().length, 1);
    }

    @Test
    public void test_5_update_crew_object() {
        ComkerCrewDTO param = new ComkerCrewDTO("Crew 02 - updated", "Update the crew-02");
        param.setId(crewIdx.get(2));

        param.setGlobalRoleIds(new String[] {roleIdx.get(2)});

        Set<ComkerKeyAndValueSet> spotAndRoles = new HashSet<ComkerKeyAndValueSet>();
        spotAndRoles.add(new ComkerKeyAndValueSet(spotIdx.get(1), new String[]{roleIdx.get(4), roleIdx.get(6)}));
        spotAndRoles.add(new ComkerKeyAndValueSet(spotIdx.get(2), new String[]{roleIdx.get(5), roleIdx.get(6)}));
        param.setScopedRoleIds(spotAndRoles.toArray(new ComkerKeyAndValueSet[0]));

        crewStorage.update(param);

        ComkerCrewDTO result = crewStorage.get(crewIdx.get(2));

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getName(), param.getName());
        assertEquals(result.getDescription(), param.getDescription());

        Set<String> newGlobalRoleIds = new HashSet<String>();
        if (param.getGlobalRoleIds() != null) {
            newGlobalRoleIds.addAll(Arrays.asList(param.getGlobalRoleIds()));
        }
        for (String globalRoleId : result.getGlobalRoleIds()) {
            if (newGlobalRoleIds.contains(globalRoleId)) {
                newGlobalRoleIds.remove(globalRoleId);
            } else {
                assertTrue("Cannot found:" + globalRoleId, true);
            }
        }
        assertEquals(newGlobalRoleIds.size(), 0);

        for (ComkerKeyAndValueSet item : result.getScopedRoleIds()) {
            if (item.getKey().equals(spotIdx.get(1))) {
                assertThat(item.getValues(), CoreMatchers.is(new String[] {roleIdx.get(4), roleIdx.get(6)}));
            } else if (item.getKey().equals(spotIdx.get(2))) {
                assertThat(item.getValues(), CoreMatchers.is(new String[] {roleIdx.get(5), roleIdx.get(6)}));
            } else {
                assertTrue("Cannot found:" + item.getKey(), true);
            }
        }
        assertEquals(result.getScopedRoleIds().length, 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_5_update_crew_object_with_wrong_id() {
        ComkerCrewDTO param = new ComkerCrewDTO("Crew 01 - modified", null);
        param.setId("crew-not-found");
        crewStorage.update(param);
    }

    @Test
    public void test_6_delete_crew_object_by_id() {
        String id = crewIdx.get(4);
        crewStorage.delete(id);
        assertNull(crewMap.get(id));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_6_delete_crew_object_by_wrong_id() {
        crewStorage.delete("crew-not-found");
    }
}
