package net.cokkee.comker.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.impl.ComkerUserStorageImpl;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerCrewJoinGlobalRole;
import net.cokkee.comker.model.po.ComkerCrewJoinRoleWithSpot;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
import net.cokkee.comker.model.po.ComkerSpot;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
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
public class ComkerUserStorageUnitTest {

    private Map<String, ComkerUser> userMap = new HashMap<String, ComkerUser>();
    private Map<String, ComkerCrew> crewMap = new HashMap<String, ComkerCrew>();
    private Map<String, ComkerSpot> spotMap = new HashMap<String, ComkerSpot>();
    private Map<String, ComkerRole> roleMap = new HashMap<String, ComkerRole>();
    private Map<String, ComkerPermission> permissionMap = new HashMap<String, ComkerPermission>();

    @InjectMocks
    private ComkerUserStorageImpl userStorage;

    @Mock
    private ComkerUserDao userDao;

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
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-04"), spotMap.get("spot-02")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-05"), spotMap.get("spot-02")));
        crewMap.put(crew.getId(), crew);

        crew = new ComkerCrew("Crew 02", "This is crew-02");
        crew.setId("crew-02");
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-01")));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRole(crew, roleMap.get("role-03")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-05"), spotMap.get("spot-01")));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpot(crew, roleMap.get("role-06"), spotMap.get("spot-01")));
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

        when(crewDao.get(anyString())).thenAnswer(new Answer<ComkerCrew>() {

            @Override
            public ComkerCrew answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return crewMap.get(id);
            }
        });

        ComkerUser user;

        user = new ComkerUser("user-01@comker.net", "user01", "123456", "User 01");
        user.setId("user-01");
        user.getUserJoinCrewList().add(new ComkerUserJoinCrew(user, crewMap.get("crew-01")));
        user.getUserJoinCrewList().add(new ComkerUserJoinCrew(user, crewMap.get("crew-02")));
        userMap.put(user.getId(), user);

        user = new ComkerUser("user-03@comker.net", "user03", "123456", "User 03");
        user.setId("user-03");
        userMap.put(user.getId(), user);

        doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerCrew> bag = (Set<ComkerCrew>) invocation.getArguments()[0];
                ComkerUser user = (ComkerUser) invocation.getArguments()[1];
                List<ComkerUserJoinCrew> list = user.getUserJoinCrewList();
                for (ComkerUserJoinCrew item : list) {
                    bag.add(item.getCrew());
                }
                return null;
            }
        }).when(userDao).collectCrew(anySet(), any(ComkerUser.class));

        when(userDao.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return userMap.size();
            }
        });

        when(userDao.findAll(any(ComkerPager.class))).thenAnswer(new Answer<List<ComkerUser>>() {
            @Override
            public List<ComkerUser> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerUser> result = new ArrayList<ComkerUser>();
                for(Map.Entry<String,ComkerUser> userEntry:userMap.entrySet()) {
                    result.add(userEntry.getValue());
                }
                return result;
            }
        });

        when(userDao.get(anyString())).thenAnswer(new Answer<ComkerUser>() {
            @Override
            public ComkerUser answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return userMap.get(id);
            }
        });

        when(userDao.getByUsername(anyString())).thenAnswer(new Answer<ComkerUser>() {
            @Override
            public ComkerUser answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(Map.Entry<String, ComkerUser> userEntry:userMap.entrySet()) {
                    if (userEntry.getValue().getUsername().equals(id)) {
                        return userEntry.getValue();
                    }
                }
                return null;
            }
        });

        when(userDao.getByEmail(anyString())).thenAnswer(new Answer<ComkerUser>() {
            @Override
            public ComkerUser answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(Map.Entry<String, ComkerUser> userEntry:userMap.entrySet()) {
                    if (userEntry.getValue().getEmail().equals(id)) {
                        return userEntry.getValue();
                    }
                }
                return null;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerUser user = (ComkerUser) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(user, userMap.get(user.getId()));
                return null;
            }
        }).when(userDao).update(any(ComkerUser.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerUser user = (ComkerUser) invocation.getArguments()[0];
                if (user != null) {
                    userMap.remove(user.getId());
                }
                return null;
            }
        }).when(userDao).delete(any(ComkerUser.class));
    }

    @Test
    public void test_1_count() {
        Integer result = userStorage.count();
        assertEquals(result.intValue(), 2);
        assertEquals(result.intValue(), userMap.size());
    }

    @Test
    public void test_1_find_all_user_objects() {
        List<ComkerUserDTO> result = userStorage.findAll(null);
        assertEquals(result.size(), 2);
    }

    @Test
    public void test_2_get_user_object() {
        ComkerUserDTO result = userStorage.get("user-01");
        verify(userDao).get("user-01");
        assertThat(Arrays.asList(result.getCrewIds()), hasItems("crew-01", "crew-02"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_2_get_user_object_by_invalid_id() {
        ComkerUserDTO result = userStorage.get("user-not-found");
        verify(userDao).get("user-not-found");
    }

    @Test
    public void test_2_get_user_object_by_username() {
        ComkerUserDTO result = userStorage.getByUsername("user01");
        verify(userDao).getByUsername("user01");
        assertThat(Arrays.asList(result.getCrewIds()), hasItems("crew-01", "crew-02"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_2_get_user_object_by_invalid_username() {
        ComkerUserDTO result = userStorage.getByUsername("user-not-found");
        verify(userDao).getByUsername("user-not-found");
    }

    @Test
    public void test_2_get_user_object_by_email() {
        ComkerUserDTO result = userStorage.getByEmail("user-01@comker.net");
        verify(userDao).getByEmail("user-01@comker.net");
        assertThat(Arrays.asList(result.getCrewIds()), hasItems("crew-01", "crew-02"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_2_get_user_object_by_invalid_email() {
        ComkerUserDTO result = userStorage.getByEmail("user-not-found");
        verify(userDao).getByEmail("user-not-found");
    }

    @Test
    public void test_3_get_user_global_authorities() {
        Set<String> result = userStorage.getGlobalAuthorities("user-01");
        assertThat(result, hasItems("PERMISSION_01", "PERMISSION_02",
                "PERMISSION_03", "PERMISSION_04"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_3_get_user_global_authorities_by_wrong_id() {
        userStorage.getGlobalAuthorities("user-not-found");
    }

    @Test
    public void test_3_get_user_spot_code_with_authorities() {
        Map<String, Set<String>> result = userStorage.getSpotCodeWithAuthorities("user-01");
        assertThat(result.keySet(), hasItems("SPOT_01", "SPOT_02"));

        assertNotNull(result.get("SPOT_01"));
        assertEquals(result.get("SPOT_01").size(), 4);
        assertThat(result.get("SPOT_01"),
                hasItems("PERMISSION_05", "PERMISSION_06", "PERMISSION_07", "PERMISSION_08"));

        assertNotNull(result.get("SPOT_02"));
        assertEquals(result.get("SPOT_02").size(), 4);
        assertThat(result.get("SPOT_02"),
                hasItems("PERMISSION_05", "PERMISSION_06", "PERMISSION_07", "PERMISSION_08"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_3_get_user_spot_code_with_authorities_by_wrong_id() {
        userStorage.getSpotCodeWithAuthorities("user-not-found");
    }

    @Test
    public void test_6_delete_user_object_by_id() {
        userStorage.delete("user-03");
        assertNull(userMap.get("user-03"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_6_delete_user_object_by_invalid_id() {
        userStorage.delete("user-not-found");
    }
}
