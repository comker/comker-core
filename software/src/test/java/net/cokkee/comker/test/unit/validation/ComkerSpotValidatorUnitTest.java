package net.cokkee.comker.test.unit.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.validation.ComkerSpotValidator;
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
public class ComkerSpotValidatorUnitTest {

    private List<ComkerSpotDPO> spotList = new ArrayList<ComkerSpotDPO>();

    private List<String> moduleIds = new ArrayList<String>();
    
    @InjectMocks
    private ComkerSpotValidator validator;

    @Mock
    private ComkerSpotDao spotDao;

    @Mock
    private ComkerModuleDao moduleDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<3; i++) {
            ComkerSpotDPO spot = new ComkerSpotDPO("SPOT_" + i, "Spot " + i, "This is spot " + i);
            spot.setId(UUID.randomUUID().toString());
            spotList.add(spot);
        }

        for(int i=0; i<10; i++) {
            moduleIds.add(UUID.randomUUID().toString());
        }

        Mockito.when(spotDao.getByCode(Mockito.anyString())).thenAnswer(new Answer<ComkerSpotDPO>() {
            @Override
            public ComkerSpotDPO answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                for(ComkerSpotDPO spot:spotList) {
                    if (spot.getCode().equals(id)) {
                        return spot;
                    }
                }
                return null;
            }
        });

        Mockito.when(moduleDao.exists(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return moduleIds.contains(id);
            }
        });
    }

    @Test
    public void test_supports() {
        Assert.assertTrue(validator.supports(ComkerSpotDTO.class));
        Assert.assertFalse(validator.supports(ComkerAbstractDTO.class));
    }

    @Test 
    public void test_code_field_is_valid() {
        String[] samples = new String[] {
            "A", "_", "ABC", "012", "ABCDEFGHIJKLMNOPQRSTUVWXYZ123456"};

        ComkerSpotDTO item = new ComkerSpotDTO();

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

        ComkerSpotDTO item = new ComkerSpotDTO();

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
        ComkerSpotDTO item = new ComkerSpotDTO();
        item.setCode(null);

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertTrue(errors.hasErrors());
        Assert.assertEquals(1, errors.getFieldErrorCount("code"));
        Assert.assertEquals("msg.__field__should_be_not_null", errors.getFieldError("code").getCode());
    }

    @Test
    public void test_code_field_id_and_code_ok() {
        ComkerSpotDTO item = new ComkerSpotDTO();
        item.setId(spotList.get(1).getId());
        item.setCode(spotList.get(1).getCode());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("code"));
    }

    @Test
    public void test_code_field_code_is_duplicated_but_id_is_different() {
        ComkerSpotDTO item = new ComkerSpotDTO();
        item.setId(null);
        item.setCode(spotList.get(1).getCode());

        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(1, errors.getFieldErrorCount("code"));
        Assert.assertEquals("msg.__field__has_duplicated_value", errors.getFieldError("code").getCode());
    }

    @Test
    public void test_valid_module_ids() {
        ComkerSpotDTO item = new ComkerSpotDTO();
        item.setModuleIds(new String[] {
                moduleIds.get(1),
                moduleIds.get(3),
                moduleIds.get(5),
                moduleIds.get(7) });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("moduleIds"));
    }

    @Test
    public void test_invalid_module_ids() {
        ComkerSpotDTO item = new ComkerSpotDTO();
        item.setModuleIds(new String[] {
                moduleIds.get(0), moduleIds.get(2), moduleIds.get(4),
                "module_id_1", "module_id_3", "module_id_5" });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(3, errors.getFieldErrorCount("moduleIds"));
        
        List<FieldError> fes = errors.getFieldErrors("moduleIds");
        Assert.assertTrue(fes.size() == 3);
    }
}
