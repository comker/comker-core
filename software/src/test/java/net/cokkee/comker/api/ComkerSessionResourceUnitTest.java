package net.cokkee.comker.api;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerSessionResourceImpl;
import net.cokkee.comker.model.ComkerSessionInfo;
import net.cokkee.comker.model.ComkerUserDetails;
import net.cokkee.comker.service.ComkerSecurityService;
import net.cokkee.comker.service.ComkerSessionService;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/api/ComkerSessionResourceUnitTest.xml"})
public class ComkerSessionResourceUnitTest {

    @Autowired
    protected ComkerSessionResource sessionClient;

    @Autowired
    @InjectMocks
    private ComkerSessionResourceImpl sessionServer;

    @Mock
    private ComkerSessionService sessionService;

    @Mock
    private ComkerSecurityService securityService;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_get_information_on_logined_session() {

        Date time1 = Calendar.getInstance().getTime();

        Mockito.when(securityService.getUserDetails()).thenAnswer(new Answer<ComkerUserDetails>() {

            @Override
            public ComkerUserDetails answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDetails userDetails = new ComkerUserDetails(
                        "username",
                        "password",
                        "SPOT_01",
                        new HashSet<String>(Arrays.asList(new String[]{"PERMISSION_01", "PERMISSION_02"})));
                return userDetails;
            }
        });
        Response resp = sessionClient.getInformation();

        Date time2 = Calendar.getInstance().getTime();
        
        assertTrue(resp.getStatus() == 200);

        ComkerSessionInfo result = resp.readEntity(ComkerSessionInfo.class);
        Assert.assertTrue(time1.before(result.getTimestamp()));
        Assert.assertTrue(time2.after(result.getTimestamp()));
        assertThat(result.getPermissions(), CoreMatchers.hasItems("PERMISSION_01", "PERMISSION_02"));
    }

    @Test
    public void test_get_information_on_anonymous_session() {
        
    }
}
