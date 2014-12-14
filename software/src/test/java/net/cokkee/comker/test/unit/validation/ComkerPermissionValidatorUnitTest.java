package net.cokkee.comker.test.unit.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;
import net.cokkee.comker.validation.ComkerPermissionValidator;
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
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerPermissionValidatorUnitTest {

    private List<ComkerPermissionDPO> permissionList = new ArrayList<ComkerPermissionDPO>();

    private List<String> permissionIds = new ArrayList<String>();
    
    @InjectMocks
    private ComkerPermissionValidator validator;

    @Mock
    private ComkerPermissionDao permissionDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<3; i++) {
            ComkerPermissionDPO permission = new ComkerPermissionDPO("ROLE_" + i);
            permission.setId(UUID.randomUUID().toString());
            permissionList.add(permission);
        }

        for(int i=0; i<10; i++) {
            permissionIds.add(UUID.randomUUID().toString());
        }

        Mockito.when(permissionDao.getByAuthority(Mockito.anyString()))
                .thenAnswer(new Answer<ComkerPermissionDPO>() {
            @Override
            public ComkerPermissionDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerPermissionDPO permission:permissionList) {
                    if (permission.getAuthority().equals(id)) {
                        return permission;
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
        Assert.assertTrue(validator.supports(ComkerPermissionDTO.class));
        Assert.assertFalse(validator.supports(ComkerAbstractDTO.class));
    }

    @Test 
    public void test_code_field_is_valid() {
        String[] samples = new String[] {
            "A", "ABC", "A012", "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"};

        ComkerPermissionDTO item = new ComkerPermissionDTO();

        for(String sample:samples) {
            item.setAuthority(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(0, errors.getFieldErrorCount("authority"));
        }
    }

    @Test
    public void test_code_field_is_invalid() {
        String[] samples = new String[] {
            "", "#ABCDEF$", "Wrong Authority",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567"};

        ComkerPermissionDTO item = new ComkerPermissionDTO();

        for(String sample:samples) {
            item.setAuthority(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(1, errors.getFieldErrorCount("authority"));
            Assert.assertEquals("msg.__field__has_invalid_format", errors.getFieldError("authority").getCode());
        }
    }

    @Test 
    public void test_code_field_is_null() {
        ComkerPermissionDTO item = new ComkerPermissionDTO();
        item.setAuthority(null);

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount("authority"));
        Assert.assertEquals("msg.__field__should_be_not_null", errors.getFieldError("authority").getCode());
    }

    @Test
    public void test_code_field_with_id_and_authority_are_identical() {
        ComkerPermissionDTO item = new ComkerPermissionDTO();
        item.setId(permissionList.get(1).getId());
        item.setAuthority(permissionList.get(1).getAuthority());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("authority"));
    }

    @Test
    public void test_code_field_with_authority_is_duplicated() {
        ComkerPermissionDTO item = new ComkerPermissionDTO();
        item.setId(null);
        item.setAuthority(permissionList.get(1).getAuthority());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(1, errors.getFieldErrorCount("authority"));
        Assert.assertEquals("msg.__field__has_duplicated_value", errors.getFieldError("authority").getCode());
    }

}
