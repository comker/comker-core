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
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.impl.ComkerUserStorageImpl;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinGlobalRoleDPO;
import net.cokkee.comker.model.dpo.ComkerCrewJoinRoleWithSpotDPO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dpo.ComkerUserJoinCrewDPO;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerUserValidator;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;

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
public class ComkerUserStorageUnitTest {

    private List<String> userIdx = new ArrayList<String>();
    private List<String> crewIdx = new ArrayList<String>();
    private List<String> spotIdx = new ArrayList<String>();
    private List<String> roleIdx = new ArrayList<String>();
    private List<String> permissionIdx = new ArrayList<String>();

    private Map<String, ComkerUserDPO> userMap = new HashMap<String, ComkerUserDPO>();
    private Map<String, ComkerCrewDPO> crewMap = new HashMap<String, ComkerCrewDPO>();
    private Map<String, ComkerSpotDPO> spotMap = new HashMap<String, ComkerSpotDPO>();
    private Map<String, ComkerRoleDPO> roleMap = new HashMap<String, ComkerRoleDPO>();
    private Map<String, ComkerPermissionDPO> permissionMap = new HashMap<String, ComkerPermissionDPO>();

    @InjectMocks
    private ComkerUserStorageImpl userStorage;

    @InjectMocks
    private ComkerUserValidator userValidator;

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

        userStorage.setUserValidator(userValidator);
        
        /*
         * Demo data for spotDao
         */
        for(int i=0; i<3; i++) {
            ComkerSpotDPO spot = new ComkerSpotDPO("SPOT_0" + i, "Spot 0" + i, "This is spot-0" + i);
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

        when(spotDao.get(anyString())).thenAnswer(new Answer<ComkerSpotDPO>() {
            @Override
            public ComkerSpotDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return spotMap.get(id);
            }
        });

        /*
         * Demo data for permissionDao
         */
        for(int i=0; i<9; i++) {
            ComkerPermissionDPO permission = new ComkerPermissionDPO("PERMISSION_0" + i);
            permission.setId(UUID.randomUUID().toString());
            permissionMap.put(permission.getId(), permission);
            permissionIdx.add(permission.getId());
        }

        /*
         * Demo data for roleDao
         */
        ComkerRoleDPO role;

        for(int i=0; i<7; i++) {
            role = new ComkerRoleDPO("ROLE_0" + i, "Role 0" + i, "This is role-0" + i);
            role.setId(UUID.randomUUID().toString());
            roleMap.put(role.getId(), role);
            roleIdx.add(role.getId());
        }

        role = roleMap.get(roleIdx.get(1));
        for(int j=1; j<=2; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionMap.get(permissionIdx.get(j))));
        }

        role = roleMap.get(roleIdx.get(2));
        for(int j=1; j<=4; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionMap.get(permissionIdx.get(j))));
        }

        role = roleMap.get(roleIdx.get(4));
        for(int j=5; j<=7; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionMap.get(permissionIdx.get(j))));
        }

        role = roleMap.get(roleIdx.get(5));
        for(int j=6; j<=8; j++) {
            role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionMap.get(permissionIdx.get(j))));
        }

        when(roleDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleMap.containsKey(id);
            }
        });

        when(roleDao.get(anyString())).thenAnswer(new Answer<ComkerRoleDPO>() {
            @Override
            public ComkerRoleDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleMap.get(id);
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerPermissionDPO> bag = (Set<ComkerPermissionDPO>) invocation.getArguments()[0];
                ComkerRoleDPO role = (ComkerRoleDPO) invocation.getArguments()[1];
                List<ComkerRoleJoinPermissionDPO> list = role.getRoleJoinPermissionList();
                for (ComkerRoleJoinPermissionDPO item : list) {
                    bag.add(item.getPermission());
                }
                return null;
            }
        }).when(roleDao).collectPermission(anySet(), any(ComkerRoleDPO.class));

        /*
         * Demo data for crewDao
         */
        ComkerCrewDPO crew;

        for(int i=0; i<5; i++) {
            crew = new ComkerCrewDPO("Crew 0" + i, "This is crew-0" + i);
            crew.setId(UUID.randomUUID().toString());
            crewMap.put(crew.getId(), crew);
            crewIdx.add(crew.getId());
        }

        crew = crewMap.get(crewIdx.get(1));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crew, roleMap.get(roleIdx.get(1))));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crew, roleMap.get(roleIdx.get(2))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpotDPO(crew, roleMap.get(roleIdx.get(4)), spotMap.get(spotIdx.get(1))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpotDPO(crew, roleMap.get(roleIdx.get(4)), spotMap.get(spotIdx.get(2))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpotDPO(crew, roleMap.get(roleIdx.get(5)), spotMap.get(spotIdx.get(2))));

        crew = crewMap.get(crewIdx.get(2));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crew, roleMap.get(roleIdx.get(1))));
        crew.getCrewJoinGlobalRoleList().add(new ComkerCrewJoinGlobalRoleDPO(crew, roleMap.get(roleIdx.get(3))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpotDPO(crew, roleMap.get(roleIdx.get(5)), spotMap.get(spotIdx.get(1))));
        crew.getCrewJoinRoleWithSpotList().add(new ComkerCrewJoinRoleWithSpotDPO(crew, roleMap.get(roleIdx.get(6)), spotMap.get(spotIdx.get(1))));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerRoleDPO> bag = (Set<ComkerRoleDPO>) invocation.getArguments()[0];
                ComkerCrewDPO crew = (ComkerCrewDPO) invocation.getArguments()[1];
                List<ComkerCrewJoinGlobalRoleDPO> list = crew.getCrewJoinGlobalRoleList();
                for (ComkerCrewJoinGlobalRoleDPO item : list) {
                    bag.add(item.getRole());
                }
                return null;
            }
        }).when(crewDao).collectGlobalRole(anySet(), any(ComkerCrewDPO.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Map<ComkerSpotDPO, Set<ComkerRoleDPO>> bag = (Map<ComkerSpotDPO, Set<ComkerRoleDPO>>) invocation.getArguments()[0];
                ComkerCrewDPO crew = (ComkerCrewDPO) invocation.getArguments()[1];
                List<ComkerCrewJoinRoleWithSpotDPO> list = crew.getCrewJoinRoleWithSpotList();
                for (ComkerCrewJoinRoleWithSpotDPO item : list) {
                    ComkerSpotDPO spot = item.getSpot();
                    Set<ComkerRoleDPO> roleSet = bag.get(spot);
                    if (roleSet == null) {
                        roleSet = new HashSet<ComkerRoleDPO>();
                        bag.put(spot, roleSet);
                    }
                    roleSet.add(item.getRole());
                }
                return null;
            }
        }).when(crewDao).collectSpotWithRole(anyMap(), any(ComkerCrewDPO.class));

        when(crewDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return crewMap.containsKey(id);
            }
        });

        when(crewDao.get(anyString())).thenAnswer(new Answer<ComkerCrewDPO>() {
            @Override
            public ComkerCrewDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return crewMap.get(id);
            }
        });

        ComkerUserDPO user;

        user = new ComkerUserDPO("user0@comker.net", "user0", "Matkhau$1", "Phạm Ngọc Hùng");
        user.setId(UUID.randomUUID().toString());
        userMap.put(user.getId(), user);
        userIdx.add(user.getId());

        user = new ComkerUserDPO("user1@comker.net", "user1", "Matkhau#1", "Nguyễn Tuấn Anh");
        user.setId(UUID.randomUUID().toString());
        user.getUserJoinCrewList().add(new ComkerUserJoinCrewDPO(user, crewMap.get(crewIdx.get(1))));
        user.getUserJoinCrewList().add(new ComkerUserJoinCrewDPO(user, crewMap.get(crewIdx.get(2))));
        userMap.put(user.getId(), user);
        userIdx.add(user.getId());

        user = new ComkerUserDPO("user2@comker.net", "user2", "Matkhau$1", "Trương Minh Tuấn");
        user.setId(UUID.randomUUID().toString());
        userMap.put(user.getId(), user);
        userIdx.add(user.getId());

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerCrewDPO> bag = (Set<ComkerCrewDPO>) invocation.getArguments()[0];
                ComkerUserDPO user = (ComkerUserDPO) invocation.getArguments()[1];
                List<ComkerUserJoinCrewDPO> list = user.getUserJoinCrewList();
                for (ComkerUserJoinCrewDPO item : list) {
                    bag.add(item.getCrew());
                }
                return null;
            }
        }).when(userDao).collectCrew(anySet(), any(ComkerUserDPO.class));

        when(userDao.count(any(ComkerQuerySieve.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return userMap.size();
            }
        });

        when(userDao.findAll(any(ComkerQuerySieve.class), any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerUserDPO>>() {
            @Override
            public List<ComkerUserDPO> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerUserDPO> result = new ArrayList<ComkerUserDPO>();
                for(Map.Entry<String,ComkerUserDPO> userEntry:userMap.entrySet()) {
                    result.add(userEntry.getValue());
                }
                return result;
            }
        });

        when(userDao.get(anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return userMap.get(id);
            }
        });

        when(userDao.getByUsername(anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(Map.Entry<String, ComkerUserDPO> userEntry:userMap.entrySet()) {
                    if (userEntry.getValue().getUsername().equals(id)) {
                        return userEntry.getValue();
                    }
                }
                return null;
            }
        });

        when(userDao.getByEmail(anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(Map.Entry<String, ComkerUserDPO> userEntry:userMap.entrySet()) {
                    if (userEntry.getValue().getEmail().equals(id)) {
                        return userEntry.getValue();
                    }
                }
                return null;
            }
        });

        doAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDPO user = (ComkerUserDPO) invocation.getArguments()[0];
                user.setId(UUID.randomUUID().toString());
                userMap.put(user.getId(), user);
                userIdx.add(user.getId());
                return user;
            }
        }).when(userDao).create(any(ComkerUserDPO.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDPO user = (ComkerUserDPO) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(user, userMap.get(user.getId()));
                return null;
            }
        }).when(userDao).update(any(ComkerUserDPO.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDPO user = (ComkerUserDPO) invocation.getArguments()[0];
                if (user != null) {
                    userMap.remove(user.getId());
                    userIdx.remove(user.getId());
                }
                return null;
            }
        }).when(userDao).delete(any(ComkerUserDPO.class));
    }

    @Test
    public void test_count() {
        Integer result = userStorage.count();
        assertEquals(result.intValue(), userMap.size());
    }

    @Test
    public void test_find_all_user_objects() {
        List<ComkerUserDTO> result = userStorage.findAll(null);
        assertEquals(result.size(), userMap.size());
    }

    @Test
    public void test_get_user_object() {
        ComkerUserDTO result = userStorage.get(userIdx.get(1));
        verify(userDao).get(userIdx.get(1));
        assertThat(Arrays.asList(result.getCrewIds()), hasItems(crewIdx.get(1), crewIdx.get(2)));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_user_object_by_invalid_id() {
        ComkerUserDTO result = userStorage.get("user-not-found");
        verify(userDao).get("user-not-found");
    }

    @Test
    public void test_get_user_object_by_username() {
        ComkerUserDPO source = userMap.get(userIdx.get(1));
        ComkerUserDTO result = userStorage.getByUsername(source.getUsername());
        verify(userDao).getByUsername(source.getUsername());
        assertThat(Arrays.asList(result.getCrewIds()), hasItems(crewIdx.get(1), crewIdx.get(2)));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_user_object_by_invalid_username() {
        ComkerUserDTO result = userStorage.getByUsername("user-not-found");
        verify(userDao).getByUsername("user-not-found");
    }

    @Test
    public void test_get_user_object_by_email() {
        ComkerUserDPO source = userMap.get(userIdx.get(1));
        ComkerUserDTO result = userStorage.getByEmail(source.getEmail());
        verify(userDao).getByEmail(source.getEmail());
        assertThat(Arrays.asList(result.getCrewIds()), hasItems(crewIdx.get(1), crewIdx.get(2)));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_user_object_by_invalid_email() {
        ComkerUserDTO result = userStorage.getByEmail("user-not-found");
        verify(userDao).getByEmail("user-not-found");
    }

    @Test
    public void test_get_user_global_authorities() {
        Set<String> result = userStorage.getGlobalAuthorities(userIdx.get(1));
        assertThat(result, hasItems("PERMISSION_01", "PERMISSION_02",
                "PERMISSION_03", "PERMISSION_04"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_user_global_authorities_by_wrong_id() {
        userStorage.getGlobalAuthorities("user-not-found");
    }

    @Test
    public void test_get_user_spot_code_with_authorities() {
        Map<String, Set<String>> result = userStorage.getSpotCodeWithAuthorities(userIdx.get(1));
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
    public void test_get_user_spot_code_with_authorities_by_wrong_id() {
        userStorage.getSpotCodeWithAuthorities("user-not-found");
    }

    @Test
    public void test_create_user_object() {
        ComkerUserDTO param = new ComkerUserDTO(
                "user" + userIdx.size() + "9@comker.net",
                "user" + userIdx.size(),
                "Matkhau$1",
                "Duong Thi Trang");
        param.setCrewIds(new String[] {crewIdx.get(1), crewIdx.get(2)});

        ComkerUserDTO result = userStorage.create(param);

        Assert.assertNotNull(result);
        Assert.assertNotNull(result.getId());
        Assert.assertEquals(result.getEmail(), param.getEmail());
        Assert.assertEquals(result.getUsername(), param.getUsername());
        Assert.assertEquals(result.getFullname(), param.getFullname());

        Assert.assertThat(result.getCrewIds(), CoreMatchers.is(param.getCrewIds()));
        
        Set<String> newCrewIds = new HashSet<String>();
        if (param.getCrewIds() != null) {
            newCrewIds.addAll(Arrays.asList(param.getCrewIds()));
        }
        for (String globalRoleId : result.getCrewIds()) {
            if (newCrewIds.contains(globalRoleId)) {
                newCrewIds.remove(globalRoleId);
            }
        }
        assertEquals(newCrewIds.size(), 0);
    }

    @Test
    public void test_update_user_object_with_valid_crewIds() {
        ComkerUserDPO source = userMap.get(userIdx.get(1));

        ComkerUserDTO param = new ComkerUserDTO(
                "updated" + source.getEmail(),
                source.getUsername() + "abc",
                "Matkhau@9",
                source.getFullname() + " Updated");

        param.setId(source.getId());
        param.setCrewIds(new String[] { crewIdx.get(3), crewIdx.get(4)});

        userStorage.update(param);

        ComkerUserDTO result = userStorage.get(source.getId());
        Assert.assertEquals(source.getEmail(), result.getEmail());
        Assert.assertEquals(source.getUsername(), result.getUsername());
        Assert.assertEquals(source.getFullname(), result.getFullname());
        Assert.assertThat(
                new String[] { crewIdx.get(3), crewIdx.get(4)},
                CoreMatchers.is(result.getCrewIds()));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_update_user_object_with_wrong_id() {
        ComkerUserDTO param = new ComkerUserDTO("email@gmail.com", "user", "passwd", "Fullname");
        param.setId("user-not-found");
        userStorage.update(param);
    }

    @Test
    public void test_delete_user_object_by_id() {
        String id = userIdx.get(2);
        userStorage.delete(id);
        assertNull(userMap.get(id));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_delete_user_object_by_invalid_id() {
        userStorage.delete("user-not-found");
    }
}
