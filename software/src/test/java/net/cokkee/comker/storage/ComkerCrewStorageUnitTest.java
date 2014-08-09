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
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
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

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;
import static org.junit.matchers.JUnitMatchers.*;
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

    private Map<String, ComkerCrew> crewMap = new HashMap<String, ComkerCrew>();
    private Map<String, ComkerSpot> spotMap = new HashMap<String, ComkerSpot>();
    private Map<String, ComkerRole> roleMap = new HashMap<String, ComkerRole>();
    private Map<String, ComkerPermission> permissionMap = new HashMap<String, ComkerPermission>();

    @InjectMocks
    private ComkerCrewStorageImpl crewStorage;

    @Mock
    private ComkerCrewDao crewDao;

    @Mock
    private ComkerRoleDao roleDao;

    @Mock
    private ComkerSpotDao spotDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        /*
         * Demo data for spotDao
         */
        ComkerSpot spot;

        spot = new ComkerSpot("SPOT_01", "Spot 01", "This is spot-01");
        spot.setId("spot-01");
        spotMap.put(spot.getId(), spot);

        spot = new ComkerSpot("SPOT_02", "Spot 02", "This is spot-02");
        spot.setId("spot-02");
        spotMap.put(spot.getId(), spot);

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
        ComkerPermission permission;

        permission = new ComkerPermission("PERMISSION_01");
        permission.setId("permission-01");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_02");
        permission.setId("permission-02");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_03");
        permission.setId("permission-03");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_04");
        permission.setId("permission-04");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_05");
        permission.setId("permission-05");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_06");
        permission.setId("permission-06");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_07");
        permission.setId("permission-07");
        permissionMap.put(permission.getId(), permission);

        permission = new ComkerPermission("PERMISSION_08");
        permission.setId("permission-08");
        permissionMap.put(permission.getId(), permission);

        /*
         * Demo data for roleDao
         */
        ComkerRole role;
        role = new ComkerRole("ROLE_01", "Role 01", "This is role-01");
        role.setId("role-01");
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-01")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-02")));
        roleMap.put(role.getId(), role);

        role = new ComkerRole("ROLE_02", "Role 02", "This is role-02");
        role.setId("role-02");
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-01")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-02")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-03")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-04")));
        roleMap.put(role.getId(), role);

        role = new ComkerRole("ROLE_03", "Role 03", "This is role-03");
        role.setId("role-03");
        roleMap.put(role.getId(), role);

        role = new ComkerRole("ROLE_04", "Role 04", "This is role-04");
        role.setId("role-04");
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-05")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-06")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-07")));
        roleMap.put(role.getId(), role);

        role = new ComkerRole("ROLE_05", "Role 05", "This is role-05");
        role.setId("role-05");
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-06")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-07")));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermission(role, permissionMap.get("permission-08")));
        roleMap.put(role.getId(), role);

        role = new ComkerRole("ROLE_06", "Role 06", "This is role-06");
        role.setId("role-06");
        roleMap.put(role.getId(), role);

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
        crew = new ComkerCrew("Crew 01", "This is crew-01");
        crew.setId("crew-01");
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-01")));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-02")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-04"), spotMap.get("spot-01")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-05"), spotMap.get("spot-01")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-04"), spotMap.get("spot-02")));
        crewMap.put(crew.getId(), crew);

        crew = new ComkerCrew("Crew 02", "This is crew-02");
        crew.setId("crew-02");
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-01")));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-03")));
        crewMap.put(crew.getId(), crew);

        crew = new ComkerCrew("Crew 03", "This is crew-03");
        crew.setId("crew-03");
        crewMap.put(crew.getId(), crew);

        crew = new ComkerCrew("Crew 04", "This is crew-04");
        crew.setId("crew-04");
        crewMap.put(crew.getId(), crew);

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
        ComkerCrewDTO result = crewStorage.get("crew-01");
        verify(crewDao).get("crew-01");
        assertArrayEquals(new String[]{"role-01", "role-02"}, result.getGlobalRoleIds());

        ComkerKeyAndValueSet[] limitedSpotRoleIds = result.getLimitedSpotRoleIds();
        assertEquals(limitedSpotRoleIds.length, 2);
        for (ComkerKeyAndValueSet item : limitedSpotRoleIds) {
            if (item.getKey().equals("spot-01")) {
                assertThat(item.getValueSet(), hasItems("role-04", "role-05"));
            } else {
                assertThat(item.getValueSet(), hasItems("role-04"));
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
        Set<String> result = crewStorage.getGlobalAuthorities("crew-01");
        assertThat(result, hasItems("PERMISSION_01", "PERMISSION_02",
                "PERMISSION_03", "PERMISSION_04"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_3_get_crew_global_authorities_by_wrong_id() {
        crewStorage.getGlobalAuthorities("crew-not-found");
    }

    @Test
    public void test_3_get_crew_spot_code_with_authorities() {
        Map<String, Set<String>> result = crewStorage.getSpotCodeWithAuthorities("crew-01");
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

        param.setGlobalRoleIds(new String[] {"role-01", "role-02"});

        Set<ComkerKeyAndValueSet> spotAndRoles = new HashSet<ComkerKeyAndValueSet>();
        spotAndRoles.add(new ComkerKeyAndValueSet("spot-01", new String[]{"role-04", "role-06"}));
        param.setLimitedSpotRoleIds(spotAndRoles.toArray(new ComkerKeyAndValueSet[0]));

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

        for (ComkerKeyAndValueSet item : result.getLimitedSpotRoleIds()) {
            if (item.getKey().equals("spot-01")) {
                assertThat(item.getValueSet(), hasItems("role-04", "role-06"));
            } else {
                assertTrue("Cannot found:" + item.getKey(), true);
            }
        }
        assertEquals(result.getLimitedSpotRoleIds().length, 1);
    }

    @Test
    public void test_5_update_crew_object() {
        ComkerCrewDTO param = new ComkerCrewDTO("Crew 02 - updated", "Update the crew-02");
        param.setId("crew-02");

        param.setGlobalRoleIds(new String[] {"role-02"});

        Set<ComkerKeyAndValueSet> spotAndRoles = new HashSet<ComkerKeyAndValueSet>();
        spotAndRoles.add(new ComkerKeyAndValueSet("spot-01", new String[]{"role-04", "role-06"}));
        spotAndRoles.add(new ComkerKeyAndValueSet("spot-02", new String[]{"role-05", "role-06"}));
        param.setLimitedSpotRoleIds(spotAndRoles.toArray(new ComkerKeyAndValueSet[0]));

        crewStorage.update(param);

        ComkerCrewDTO result = crewStorage.get("crew-02");

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

        for (ComkerKeyAndValueSet item : result.getLimitedSpotRoleIds()) {
            if (item.getKey().equals("spot-01")) {
                assertThat(item.getValueSet(), hasItems("role-04", "role-06"));
            } else if (item.getKey().equals("spot-02")) {
                assertThat(item.getValueSet(), hasItems("role-05", "role-06"));
            } else {
                assertTrue("Cannot found:" + item.getKey(), true);
            }
        }
        assertEquals(result.getLimitedSpotRoleIds().length, 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_5_update_crew_object_with_wrong_id() {
        ComkerCrewDTO param = new ComkerCrewDTO("Crew 01 - modified", null);
        param.setId("crew-not-found");
        crewStorage.update(param);
    }

    @Test
    public void test_6_delete_crew_object_by_id() {
        crewStorage.delete("crew-04");
        assertNull(crewMap.get("crew-04"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_6_delete_crew_object_by_wrong_id() {
        crewStorage.delete("crew-not-found");
    }
}
