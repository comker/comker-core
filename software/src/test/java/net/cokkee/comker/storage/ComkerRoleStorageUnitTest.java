package net.cokkee.comker.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.storage.impl.ComkerRoleStorageImpl;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
import net.cokkee.comker.util.ComkerDataUtil;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

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
public class ComkerRoleStorageUnitTest {

    private List<String> roleIdx = new ArrayList<String>();

    private Map<String, ComkerRole> roleMap = new HashMap<String, ComkerRole>();

    private List<String> permissionIdx = new ArrayList<String>();

    private Map<String, ComkerPermission> permissionMap = new HashMap<String, ComkerPermission>();
    
    @InjectMocks
    private ComkerRoleStorageImpl roleStorage;

    @Mock
    private ComkerRoleDao roleDao;

    @Mock
    private ComkerPermissionDao permissionDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        for(int i=1; i<=6; i++) {
            ComkerPermission permission = new ComkerPermission("PERMISSION_0" + i);
            permission.setId("permission-0" + i);
            permissionMap.put(permission.getId(), permission);
        }

        when(permissionDao.get(anyString())).thenAnswer(new Answer<ComkerPermission>() {
            @Override
            public ComkerPermission answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                if (!permissionMap.containsKey(id)) return null;
                return permissionMap.get(id);
            }
        });

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

        /*
         * mocks for count() method
         */
        when(roleDao.count()).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return roleMap.size();
            }
        });

        when(roleDao.findAll(any(ComkerPager.class))).thenAnswer(new Answer<List<ComkerRole>>() {
            @Override
            public List<ComkerRole> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerRole> result = new ArrayList<ComkerRole>();
                for(Map.Entry<String,ComkerRole> roleEntry:roleMap.entrySet()) {
                    result.add(roleEntry.getValue());
                }
                return result;
            }
        });

        when(roleDao.get(anyString())).thenAnswer(new Answer<ComkerRole>() {
            @Override
            public ComkerRole answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleMap.get(id);
            }
        });

        when(roleDao.getByCode(anyString())).thenAnswer(new Answer<ComkerRole>() {
            @Override
            public ComkerRole answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(Map.Entry<String, ComkerRole> roleEntry:roleMap.entrySet()) {
                    if (roleEntry.getValue().getCode().equals(id)) {
                        return roleEntry.getValue();
                    }
                }
                return null;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerPermission> bag = (Set<ComkerPermission>) invocation.getArguments()[0];
                ComkerRole role = (ComkerRole) invocation.getArguments()[1];
                List<ComkerRoleJoinPermission> list = role.getRoleJoinPermissionList();
                for(ComkerRoleJoinPermission item:list) {
                    bag.add(item.getPermission());
                }
                return null;
            }
        }).when(roleDao).collectPermission(anySet(), any(ComkerRole.class));

        doAnswer(new Answer<ComkerRole>() {
            @Override
            public ComkerRole answer(InvocationOnMock invocation) throws Throwable {
                ComkerRole role = (ComkerRole) invocation.getArguments()[0];
                role.setId(UUID.randomUUID().toString());
                roleMap.put(role.getId(), role);
                return role;
            }
        }).when(roleDao).create(any(ComkerRole.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRole role = (ComkerRole) invocation.getArguments()[0];
                ComkerDataUtil.copyProperties(role, roleMap.get(role.getId()));
                return null;
            }
        }).when(roleDao).update(any(ComkerRole.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRole role = (ComkerRole) invocation.getArguments()[0];
                if (role != null) {
                    roleMap.remove(role.getId());
                }
                return null;
            }
        }).when(roleDao).delete(any(ComkerRole.class));
    }

    @Test
    public void test_count() {
        Integer result = roleStorage.count();
        assertEquals(result.intValue(), 3);
    }

    @Test
    public void test_find_all_role_objects() {
        List<ComkerRoleDTO> result = roleStorage.findAll(null);
        assertEquals(result.size(), 3);
    }

    @Test
    public void test_get_role_object_by_id() {
        ComkerRoleDTO result = roleStorage.get("role-01");
        verify(roleDao).get("role-01");
        assertArrayEquals(new String[] {"permission-01", "permission-02"}, result.getPermissionIds());
        
        result = roleStorage.get("role-02");
        assertEquals(result.getPermissionIds().length, 4);

        result = roleStorage.get("role-03");
        verify(roleDao).get("role-03");
        assertEquals(result.getPermissionIds().length, 0);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_role_object_by_invalid_id() {
        ComkerRoleDTO result = roleStorage.get("role-not-found");
        verify(roleDao).get("role-not-found");
    }

    @Test
    public void test_get_role_object_by_code() {
        ComkerRoleDTO result = roleStorage.getByCode("ROLE_01");
        verify(roleDao).getByCode("ROLE_01");
        assertEquals(result.getPermissionIds().length, 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_role_object_by_wrong_code() {
        ComkerRoleDTO result = roleStorage.get("ROLE_NOT_FOUND");
    }

    @Test
    public void test_get_authorities() {
        Set<String> result = roleStorage.getAuthorities("role-01");
        assertArrayEquals(new String[] {"PERMISSION_01", "PERMISSION_02"}, result.toArray(new String[0]));
    }

    @Test(expected = ComkerFieldDuplicatedException.class)
    public void test_create_role_object_with_duplicated_code() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_02", "A new role", null);
        roleStorage.create(param);
    }

    @Test
    public void test_create_role_object_with_valid_code() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_04", "Role 04", "This is role 4");
        param.setPermissionIds(new String[] {"permission-01", "permission-06"});
        ComkerRoleDTO result = roleStorage.create(param);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getCode(), param.getCode());
        assertEquals(result.getName(), param.getName());
        assertEquals(result.getDescription(), param.getDescription());
        assertArrayEquals(new String[] {"permission-01", "permission-06"}, result.getPermissionIds());
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_update_role_object_with_invalid_id() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_01", "Role 01 - modified", null);
        param.setId("role-not-found");
        roleStorage.update(param);
    }

    @Test
    public void test_update_role_object_with_duplicated_both_id_and_code() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_02", "Role 02 - modified", null);
        param.setId("role-02");
        roleStorage.update(param);
        ComkerRoleDTO result = roleStorage.get("role-02");
        assertEquals(result.getName(), param.getName());
    }

    @Test(expected = ComkerFieldDuplicatedException.class)
    public void test_update_role_object_with_duplicated_code() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_02", "Role 01 - modified", null);
        param.setId("role-01");
        roleStorage.update(param);
    }

    @Test
    public void test_update_role_object_with_null_permissionIds() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_01", "Role 01 - modified", null);
        param.setId("role-01");
        roleStorage.update(param);
        assertEquals(roleMap.get("role-01").getCode(), param.getCode());
        assertEquals(roleMap.get("role-01").getName(), param.getName());
        assertNull(roleMap.get("role-01").getDescription());
    }

    @Test
    public void test_update_role_object_with_valid_permissionIds() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_01", "Role 01 - modified", "Role 01 description");
        param.setId("role-01");
        param.setPermissionIds(new String[] {"permission-02", "permission-05", "permission-06"});
        roleStorage.update(param);
        ComkerRoleDTO result = roleStorage.get("role-01");
        assertEquals(roleMap.get("role-01").getDescription(), param.getDescription());
        assertArrayEquals(new String[] {
            "permission-02", "permission-05", "permission-06"}, result.getPermissionIds());
    }

    @Test
    public void test_update_role_object_with_invalid_permissionIds() {
        ComkerRoleDTO param = new ComkerRoleDTO("ROLE_01", "Role 01 - modified", "Role 01 description");
        param.setId("role-01");
        param.setPermissionIds(new String[] {"permission-01", "permission-02", "permission-not-found"});
        roleStorage.update(param);
        ComkerRoleDTO result = roleStorage.get("role-01");
        assertArrayEquals(new String[] {
            "permission-01", "permission-02"}, result.getPermissionIds());
    }

    @Test
    public void test_delete_role_object_by_id() {
        roleStorage.delete("role-03");
        assertNull(roleMap.get("role-03"));
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_delete_role_object_by_invalid_id() {
        roleStorage.delete("role-not-found");
    }
}
