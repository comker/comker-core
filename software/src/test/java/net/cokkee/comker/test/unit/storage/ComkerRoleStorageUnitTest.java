package net.cokkee.comker.test.unit.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.exception.ComkerObjectNotFoundException;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.storage.impl.ComkerRoleStorageImpl;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO;
import net.cokkee.comker.util.ComkerDataUtil;
import net.cokkee.comker.validation.ComkerRoleValidator;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
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
public class ComkerRoleStorageUnitTest {

    private List<ComkerRoleDPO> roleIdx = new ArrayList<ComkerRoleDPO>();

    private List<ComkerPermissionDPO> permissionIdx = new ArrayList<ComkerPermissionDPO>();

    @InjectMocks
    private ComkerRoleStorageImpl roleStorage;

    @InjectMocks
    private ComkerRoleValidator roleValidator;
    
    @Mock
    private ComkerRoleDao roleDao;

    @Mock
    private ComkerPermissionDao permissionDao;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        roleStorage.setRoleValidator(roleValidator);
        
        for(int i=0; i<7; i++) {
            ComkerPermissionDPO permission = new ComkerPermissionDPO("PERMISSION_0" + i);
            permission.setId(UUID.randomUUID().toString());
            permissionIdx.add(permission);
        }

        when(permissionDao.exists(anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerPermissionDPO permission:permissionIdx) {
                    if(permission.getId().equals(id)) return true;
                }
                return false;
            }
        });

        when(permissionDao.get(anyString())).thenAnswer(new Answer<ComkerPermissionDPO>() {
            @Override
            public ComkerPermissionDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerPermissionDPO permission:permissionIdx) {
                    if(permission.getId().equals(id)) return permission;
                }
                return null;
            }
        });

        ComkerRoleDPO role;

        for(int i=0; i<4; i++) {
            role = new ComkerRoleDPO(
                    "ROLE_0" + i, 
                    "Role 0" + i, 
                    "This is role 0" + i,
                    Boolean.TRUE);
            role.setId(UUID.randomUUID().toString());
            roleIdx.add(role);
        }

        role = roleIdx.get(1);
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(1)));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(2)));

        role = roleIdx.get(2);
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(1)));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(2)));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(3)));
        role.getRoleJoinPermissionList().add(new ComkerRoleJoinPermissionDPO(role, permissionIdx.get(4)));

        /*
         * mocks for count() method
         */
        when(roleDao.count(any(ComkerQuerySieve.class))).thenAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return roleIdx.size();
            }
        });

        when(roleDao.findAll(any(ComkerQuerySieve.class), any(ComkerQueryPager.class)))
                .thenAnswer(new Answer<List<ComkerRoleDPO>>() {
            @Override
            public List<ComkerRoleDPO> answer(InvocationOnMock invocation) throws Throwable {
                List<ComkerRoleDPO> result = new ArrayList<ComkerRoleDPO>(roleIdx);
                return result;
            }
        });

        when(roleDao.get(anyString())).thenAnswer(new Answer<ComkerRoleDPO>() {
            @Override
            public ComkerRoleDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerRoleDPO role:roleIdx) {
                    if(role.getId().equals(id)) return role;
                }
                return null;
            }
        });

        when(roleDao.getByCode(anyString())).thenAnswer(new Answer<ComkerRoleDPO>() {
            @Override
            public ComkerRoleDPO answer(InvocationOnMock invocation) throws Throwable {
                String code = (String) invocation.getArguments()[0];
                for(ComkerRoleDPO role:roleIdx) {
                    if(role.getCode().equals(code)) return role;
                }
                return null;
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Set<ComkerPermissionDPO> bag = (Set<ComkerPermissionDPO>) invocation.getArguments()[0];
                ComkerRoleDPO role = (ComkerRoleDPO) invocation.getArguments()[1];
                List<ComkerRoleJoinPermissionDPO> list = role.getRoleJoinPermissionList();
                for(ComkerRoleJoinPermissionDPO item:list) {
                    bag.add(item.getPermission());
                }
                return null;
            }
        }).when(roleDao).collectPermission(anySet(), any(ComkerRoleDPO.class));

        doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                ComkerRoleDPO role = (ComkerRoleDPO) invocation.getArguments()[0];
                role.setId(UUID.randomUUID().toString());
                roleIdx.add(role);
                return role.getId();
            }
        }).when(roleDao).create(any(ComkerRoleDPO.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRoleDPO role = (ComkerRoleDPO) invocation.getArguments()[0];

                ComkerRoleDPO roleOnDB = null;
                for(ComkerRoleDPO item:roleIdx) {
                    if(item.getId().equals(role.getId())) {
                        roleOnDB = item;
                        break;
                    }
                }

                if (roleOnDB != null) {
                    ComkerDataUtil.copyProperties(role, roleOnDB);
                }
                return null;
            }
        }).when(roleDao).update(any(ComkerRoleDPO.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRoleDPO role = (ComkerRoleDPO) invocation.getArguments()[0];

                ComkerRoleDPO roleOnDB = null;
                for(ComkerRoleDPO item:roleIdx) {
                    if(item.getId().equals(role.getId())) {
                        roleOnDB = item;
                        break;
                    }
                }

                if (roleOnDB != null) {
                    roleIdx.remove(roleOnDB);
                }
                return null;
            }
        }).when(roleDao).delete(any(ComkerRoleDPO.class));
    }

    @Test
    public void test_count() {
        Integer result = roleStorage.count();
        assertEquals(result.intValue(), roleIdx.size());
    }

    @Test
    public void test_find_all_role_objects() {
        List<ComkerRoleDTO> result = roleStorage.findAll(null);
        assertEquals(result.size(), roleIdx.size());
    }

    @Test
    public void test_get_role_object_by_id() {
        ComkerRoleDTO result = roleStorage.get(roleIdx.get(1).getId());
        verify(roleDao).get(roleIdx.get(1).getId());
        assertArrayEquals(
                new String[] {permissionIdx.get(1).getId(), permissionIdx.get(2).getId()},
                result.getPermissionIds());
        
        result = roleStorage.get(roleIdx.get(2).getId());
        assertEquals(result.getPermissionIds().length, 4);

        result = roleStorage.get(roleIdx.get(3).getId());
        assertEquals(result.getPermissionIds().length, 0);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_role_object_by_invalid_id() {
        ComkerRoleDTO result = roleStorage.get("role-not-found");
        verify(roleDao).get("role-not-found");
    }

    @Test
    public void test_get_role_object_by_code() {
        ComkerRoleDTO result = roleStorage.getByCode(roleIdx.get(1).getCode());
        verify(roleDao).getByCode(roleIdx.get(1).getCode());
        assertEquals(result.getPermissionIds().length, 2);
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_get_role_object_by_wrong_code() {
        ComkerRoleDTO result = roleStorage.get("ROLE_NOT_FOUND");
    }

    @Test
    public void test_get_authorities() {
        Set<String> result = roleStorage.getAuthorities(roleIdx.get(1).getId());
        assertArrayEquals(
                new String[] {permissionIdx.get(1).getAuthority(), permissionIdx.get(2).getAuthority()},
                result.toArray(new String[0]));
    }

    @Test(expected = ComkerValidationFailedException.class)
    public void test_create_role_object_with_duplicated_code() {
        ComkerRoleDTO param = new ComkerRoleDTO(
                roleIdx.get(2).getCode(), 
                "A new role", 
                null,
                Boolean.TRUE);
        roleStorage.create(param);
    }

    @Test
    public void test_create_role_object_with_valid_code() {
        int count = roleIdx.size();
        ComkerRoleDTO param = new ComkerRoleDTO(
                "ROLE_0" + count,
                "Role 0" + count,
                "This is role " + count,
                Boolean.TRUE);
        param.setPermissionIds(new String[] {
            permissionIdx.get(1).getId(), permissionIdx.get(6).getId()});
        
        String resultId = roleStorage.create(param);
        ComkerRoleDTO result = roleStorage.get(resultId);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(result.getCode(), param.getCode());
        assertEquals(result.getName(), param.getName());
        assertEquals(result.getDescription(), param.getDescription());
        assertArrayEquals(
                new String[] {permissionIdx.get(1).getId(), permissionIdx.get(6).getId()},
                result.getPermissionIds());
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_update_role_object_with_invalid_id() {
        ComkerRoleDTO param = new ComkerRoleDTO(
                "role-not-found", 
                "SOMETHING", 
                "Something - modified", 
                "", 
                Boolean.TRUE);
        roleStorage.update(param);
    }

    @Test
    public void test_update_role_object_with_duplicated_both_id_and_code() {
        ComkerRoleDTO param = new ComkerRoleDTO(
                roleIdx.get(2).getId(),
                roleIdx.get(2).getCode(),
                roleIdx.get(2).getName() + " - modified", 
                roleIdx.get(2).getDescription(),
                Boolean.TRUE);
        roleStorage.update(param);
        ComkerRoleDTO result = roleStorage.get(roleIdx.get(2).getId());
        assertEquals(result.getName(), param.getName());
    }

    @Test(expected = ComkerValidationFailedException.class)
    public void test_update_role_object_with_duplicated_code() {
        ComkerRoleDTO param = new ComkerRoleDTO(
                roleIdx.get(1).getId(),
                roleIdx.get(2).getCode(),
                roleIdx.get(1).getName() + " - modified", 
                roleIdx.get(1).getDescription(),
                Boolean.TRUE);
        roleStorage.update(param);
    }

    @Test
    public void test_update_role_object_with_null_permissionIds() {
        ComkerRoleDPO source = roleIdx.get(1);
        ComkerRoleDTO param = new ComkerRoleDTO(
                source.getId(),
                source.getCode(), 
                source.getName() + " - modified", 
                null,
                Boolean.TRUE);
        roleStorage.update(param);
        assertEquals(source.getCode(), param.getCode());
        assertEquals(source.getName(), param.getName());
        assertNull(source.getDescription());
    }

    @Test
    public void test_update_role_object_with_valid_permissionIds() {
        ComkerRoleDPO source = roleIdx.get(1);

        ComkerRoleDTO param = new ComkerRoleDTO(
                source.getId(),
                source.getCode(),
                source.getName() + " - modified", 
                source.getDescription() + " - modified",
                Boolean.TRUE);
        param.setPermissionIds(new String[] {
            permissionIdx.get(2).getId(),
            permissionIdx.get(5).getId(),
            permissionIdx.get(6).getId()});

        roleStorage.update(param);

        ComkerRoleDTO result = roleStorage.get(source.getId());
        assertEquals(source.getCode(), result.getCode());
        assertEquals(source.getName(), result.getName());
        assertEquals(source.getDescription(), result.getDescription());
        assertArrayEquals(new String[] {
            permissionIdx.get(2).getId(),
            permissionIdx.get(5).getId(),
            permissionIdx.get(6).getId()}, result.getPermissionIds());
    }

    @Test(expected=ComkerValidationFailedException.class)
    public void test_update_role_object_with_invalid_permissionIds() {
        ComkerRoleDTO param = new ComkerRoleDTO(
                roleIdx.get(1).getId(),
                roleIdx.get(1).getCode(), 
                roleIdx.get(1).getName() + " - modified",
                "Role 01 description",
                Boolean.TRUE);
        param.setPermissionIds(new String[] {
            permissionIdx.get(1).getId(),
            permissionIdx.get(2).getId(), "permission-not-found"});
        roleStorage.update(param);
        ComkerRoleDTO result = roleStorage.get(roleIdx.get(1).getId());
        assertArrayEquals(new String[] {
            permissionIdx.get(1).getId(),
            permissionIdx.get(2).getId(),}, result.getPermissionIds());
    }

    @Test
    public void test_delete_role_object_by_id() {
        int total = roleIdx.size();
        roleStorage.delete(roleIdx.get(2).getId());
        assertEquals(total - 1, roleIdx.size());
    }

    @Test(expected = ComkerObjectNotFoundException.class)
    public void test_delete_role_object_by_invalid_id() {
        roleStorage.delete("role-not-found");
    }
}
