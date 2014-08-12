package net.cokkee.comker.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.po.ComkerRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerRoleValidatorUnitTest {

    private List<ComkerRole> roleList = new ArrayList<ComkerRole>();

    private List<String> permissionIds = new ArrayList<String>();
    
    @InjectMocks
    private ComkerRoleValidator validator;

    @Mock
    private ComkerRoleDao roleDao;

    @Mock
    private ComkerPermissionDao permissionDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<3; i++) {
            ComkerRole role = new ComkerRole("ROLE_" + i, "Role " + i, "This is role " + i);
            role.setId(UUID.randomUUID().toString());
            roleList.add(role);
        }

        for(int i=0; i<10; i++) {
            permissionIds.add(UUID.randomUUID().toString());
        }

        Mockito.when(roleDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerRole>() {
            @Override
            public ComkerRole answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerRole role:roleList) {
                    if (role.getCode().equals(id)) {
                        return role;
                    }
                }
                return null;
            }
        });

        Mockito.when(permissionDao.exists(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return permissionIds.contains(id);
            }
        });
    }

    @Test
    public void test_supports() {
        Assert.assertTrue(validator.supports(ComkerRoleDTO.class));
        Assert.assertFalse(validator.supports(ComkerAbstractDTO.class));
    }

    @Test 
    public void test_code_field_is_valid() {
        String[] samples = new String[] {
            "A", "_", "ABC", "012", "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"};

        ComkerRoleDTO item = new ComkerRoleDTO();

        for(String sample:samples) {
            item.setCode(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(0, errors.getFieldErrorCount("code"));
        }
    }

    @Test
    public void test_code_field_is_invalid() {
        String[] samples = new String[] {
            "", "#ABCDEF$", "Wrong Code",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567"};

        ComkerRoleDTO item = new ComkerRoleDTO();

        for(String sample:samples) {
            item.setCode(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(1, errors.getFieldErrorCount("code"));
            Assert.assertEquals("msg.__field__has_invalid_format", errors.getFieldError("code").getCode());
        }
    }

    @Test 
    public void test_code_field_is_null() {
        ComkerRoleDTO item = new ComkerRoleDTO();
        item.setCode(null);

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount("code"));
        Assert.assertEquals("msg.__field__should_be_not_null", errors.getFieldError("code").getCode());
    }

    @Test
    public void test_code_field_id_and_code_ok() {
        ComkerRoleDTO item = new ComkerRoleDTO();
        item.setId(roleList.get(1).getId());
        item.setCode(roleList.get(1).getCode());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("code"));
    }

    @Test
    public void test_code_field_code_is_duplicated_but_id_is_different() {
        ComkerRoleDTO item = new ComkerRoleDTO();
        item.setId(null);
        item.setCode(roleList.get(1).getCode());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(1, errors.getFieldErrorCount("code"));
        Assert.assertEquals("msg.__field__has_duplicated_value", errors.getFieldError("code").getCode());
    }

    @Test
    public void test_valid_permission_ids() {
        ComkerRoleDTO item = new ComkerRoleDTO();
        item.setPermissionIds(new String[] {
                permissionIds.get(1),
                permissionIds.get(3),
                permissionIds.get(5),
                permissionIds.get(7) });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("permissionIds"));
    }

    @Test
    public void test_invalid_permission_ids() {
        ComkerRoleDTO item = new ComkerRoleDTO();
        item.setPermissionIds(new String[] {
                permissionIds.get(0), permissionIds.get(2), permissionIds.get(4),
                "permission_id_1", "permission_id_3", "permission_id_5" });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(3, errors.getFieldErrorCount("permissionIds"));
        
        List<FieldError> fes = errors.getFieldErrors("permissionIds");
        Assert.assertTrue(fes.size() == 3);
    }
}
