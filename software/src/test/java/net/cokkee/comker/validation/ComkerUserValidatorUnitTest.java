package net.cokkee.comker.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import org.hamcrest.CoreMatchers;
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
public class ComkerUserValidatorUnitTest {

    private List<ComkerUserDPO> userList = new ArrayList<ComkerUserDPO>();

    private List<String> crewIds = new ArrayList<String>();
    
    @InjectMocks
    private ComkerUserValidator validator;

    @Mock
    private ComkerUserDao userDao;

    @Mock
    private ComkerCrewDao crewDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<3; i++) {
            ComkerUserDPO user = new ComkerUserDPO(
                    "user" + i + "@gmail.com",
                    "username" + i, 
                    "password",
                    "My Fullname Is" + i);
            user.setId(UUID.randomUUID().toString());
            userList.add(user);
        }

        for(int i=0; i<10; i++) {
            crewIds.add(UUID.randomUUID().toString());
        }

        Mockito.when(userDao.getByUsername(Mockito.anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerUserDPO user:userList) {
                    if (user.getUsername().equals(id)) {
                        return user;
                    }
                }
                return null;
            }
        });

        Mockito.when(userDao.getByEmail(Mockito.anyString())).thenAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerUserDPO user:userList) {
                    if (user.getEmail().equals(id)) {
                        return user;
                    }
                }
                return null;
            }
        });

        Mockito.when(crewDao.exists(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return crewIds.contains(id);
            }
        });
    }

    @Test
    public void test_supports() {
        Assert.assertTrue(validator.supports(ComkerUserDTO.class));
        Assert.assertFalse(validator.supports(ComkerAbstractDTO.class));
    }

    //--------------------------------------------------------------------------

    @Test
    public void test_username_field_is_null() {
        ComkerUserDTO item = new ComkerUserDTO();
        item.setUsername(null);

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount("username"));
        Assert.assertEquals("msg.__field__should_be_not_null",
                errors.getFieldError("username").getCode());
        Assert.assertThat(errors.getFieldError("username").getArguments(),
                CoreMatchers.is(new Object[] {"msg.field_username"}));
    }
    
    @Test
    public void test_username_field_is_invalid() {
        String[] samples = new String[] {
            "", "#ABCDEF$", "Wrong Username",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setUsername(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(1, errors.getFieldErrorCount("username"));
            Assert.assertEquals("msg.__field__has_invalid_format",
                    errors.getFieldError("username").getCode());
            Assert.assertThat(errors.getFieldError("username").getArguments(),
                    CoreMatchers.is(new Object[] {"msg.field_username", sample}));
        }
    }

    @Test
    public void test_username_field_is_valid() {
        String[] samples = new String[] {
            "abc", "hung012", "abcdefghijklmnop"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setUsername(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(0, errors.getFieldErrorCount("username"));
        }
    }
    
    @Test
    public void test_username_field_with_id_and_username_are_identical() {
        ComkerUserDTO item = new ComkerUserDTO();
        item.setId(userList.get(1).getId());
        item.setUsername(userList.get(1).getUsername());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("username"));
    }

    @Test
    public void test_username_field_with_username_is_duplicated() {
        ComkerUserDTO item = new ComkerUserDTO();
        item.setId(null);
        item.setUsername(userList.get(1).getUsername());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(1, errors.getFieldErrorCount("username"));
        Assert.assertEquals("msg.__field__has_duplicated_value",
                errors.getFieldError("username").getCode());
        Assert.assertThat(errors.getFieldError("username").getArguments(),
                CoreMatchers.is(new Object[] {"msg.field_username", item.getUsername()}));
    }

    //--------------------------------------------------------------------------

    @Test
    public void test_password_field_is_invalid() {
        String[] samples = new String[] {
            "toankytu", "12345678", "TOANCHUHOA",
            "Ab1$", "A123456#", "%$#$@^%&%$$"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setPassword(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(1, errors.getFieldErrorCount("password"));
            Assert.assertEquals("msg.__field__has_invalid_format",
                    errors.getFieldError("password").getCode());
            Assert.assertThat(errors.getFieldError("password").getArguments(),
                    CoreMatchers.is(new Object[] {"msg.field_password"}));
        }
    }

    @Test
    public void test_password_field_is_valid() {
        String[] samples = new String[] {"Matkhau#1", "hopLe$12", "ABCDEFg%2"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setPassword(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(0, errors.getFieldErrorCount("password"));
        }
    }

    //--------------------------------------------------------------------------

    @Test
    public void test_fullname_field_is_invalid() {
        String[] samples = new String[] {
            "", "#ABCDEF$", "Wrong.Fullname",
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setFullname(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(1, errors.getFieldErrorCount("fullname"));
            Assert.assertEquals("msg.__field__has_invalid_format",
                    errors.getFieldError("fullname").getCode());
            Assert.assertThat(errors.getFieldError("fullname").getArguments(),
                    CoreMatchers.is(new Object[] {"msg.field_fullname", sample}));
        }
    }

    @Test
    public void test_fullname_field_is_valid() {
        String[] samples = new String[] {
            "Donald Knuth", "Phạm Ngọc Hùng", "Lê Thị Chiều Xuân"};

        ComkerUserDTO item = new ComkerUserDTO();

        for(String sample:samples) {
            item.setFullname(sample);
            Errors errors = new BeanPropertyBindingResult(item, "net.drupalex.comker");
            ValidationUtils.invokeValidator(validator, item, errors);
            Assert.assertEquals(0, errors.getFieldErrorCount("fullname"));
        }
    }

    //--------------------------------------------------------------------------
    
    @Test
    public void test_valid_crew_ids() {
        ComkerUserDTO item = new ComkerUserDTO();
        item.setCrewIds(new String[] {
                crewIds.get(1),
                crewIds.get(3),
                crewIds.get(5),
                crewIds.get(7) });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("crewIds"));
    }

    @Test
    public void test_invalid_crew_ids() {
        ComkerUserDTO item = new ComkerUserDTO();
        item.setCrewIds(new String[] {
                crewIds.get(0), crewIds.get(2), crewIds.get(4),
                "crew_id_1", "crew_id_3", "crew_id_5" });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        for(int i=3; i<=5; i++) {
            String crewIdsItem = "crewIds[" + i + "]";
            Assert.assertEquals(1, errors.getFieldErrorCount(crewIdsItem));
            Assert.assertEquals("msg.__reftype__with__id__does_not_exists",
                errors.getFieldError(crewIdsItem).getCode());
            Assert.assertThat(errors.getFieldError(crewIdsItem).getArguments(),
                CoreMatchers.is(new Object[] {"msg.field_crew_id", item.getCrewIds()[i]}));
        }
    }
}
