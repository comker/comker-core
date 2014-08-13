package net.cokkee.comker.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.model.dto.ComkerAbstractDTO;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.model.po.ComkerCrew;
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
public class ComkerCrewValidatorUnitTest {

    private List<ComkerCrew> crewList = new ArrayList<ComkerCrew>();

    private List<String> spotIds = new ArrayList<String>();

    private List<String> roleIds = new ArrayList<String>();
    
    @InjectMocks
    private ComkerCrewValidator validator;

    @Mock
    private ComkerCrewDao crewDao;

    @Mock
    private ComkerSpotDao spotDao;

    @Mock
    private ComkerRoleDao roleDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        for(int i=0; i<3; i++) {
            ComkerCrew crew = new ComkerCrew("Crew " + i, "This is crew " + i);
            crew.setId(UUID.randomUUID().toString());
            crewList.add(crew);
        }

        for(int i=0; i<10; i++) {
            spotIds.add(UUID.randomUUID().toString());
        }

        for(int i=0; i<30; i++) {
            roleIds.add(UUID.randomUUID().toString());
        }

        Mockito.when(spotDao.exists(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return spotIds.contains(id);
            }
        });

        Mockito.when(roleDao.exists(Mockito.anyString())).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                String id = (String) invocation.getArguments()[0];
                return roleIds.contains(id);
            }
        });
    }

    @Test
    public void test_supports() {
        Assert.assertTrue(validator.supports(ComkerCrewDTO.class));
        Assert.assertFalse(validator.supports(ComkerAbstractDTO.class));
    }

    @Test
    public void test_valid_role_ids() {
        ComkerCrewDTO item = new ComkerCrewDTO();
        item.setGlobalRoleIds(new String[] {
                roleIds.get(1),
                roleIds.get(3),
                roleIds.get(5),
                roleIds.get(7) });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(0, errors.getFieldErrorCount("globalRoleIds"));
    }

    @Test
    public void test_invalid_role_ids() {
        ComkerCrewDTO item = new ComkerCrewDTO();
        item.setGlobalRoleIds(new String[] {
                roleIds.get(0), roleIds.get(2), roleIds.get(4),
                "role_id_1", "role_id_3", "role_id_5" });
        Errors errors = new BindException(item, "net.drupalex.comker");
        ValidationUtils.invokeValidator(validator, item, errors);

        Assert.assertEquals(1, errors.getFieldErrorCount("globalRoleIds[3]"));
        Assert.assertEquals(1, errors.getFieldErrorCount("globalRoleIds[4]"));
        Assert.assertEquals(1, errors.getFieldErrorCount("globalRoleIds[5]"));
        
        //List<FieldError> fes = errors.getFieldErrors("globalRoleIds");
        //Assert.assertTrue(fes.size() == 3);
    }
}
