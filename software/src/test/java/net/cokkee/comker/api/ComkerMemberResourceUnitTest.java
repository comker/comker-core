 package net.cokkee.comker.api;

import javax.ws.rs.core.Response;
import net.cokkee.comker.api.impl.ComkerMemberResourceImpl;
import net.cokkee.comker.exception.ComkerInvalidParameterException;
import net.cokkee.comker.storage.ComkerUserStorage;
import net.cokkee.comker.model.error.ComkerExceptionResponse;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.service.ComkerSecurityService;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

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
@ContextConfiguration(locations = {"classpath:/api/ComkerMemberResourceUnitTest.xml"})
public class ComkerMemberResourceUnitTest {

    @Autowired
    protected ComkerMemberResource memberClient;

    @Autowired
    @InjectMocks
    private ComkerMemberResourceImpl memberServer;

    @Mock
    private ComkerSecurityService securityService;

    @Mock
    private ComkerUserStorage userStorage;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_get_user_member_profile() {
        /*
        final ComkerUserDTO user = new ComkerUserDTO(
                "pnhung177@gmail.com",
                "pnhung177",
                "123456",
                "Pham Ngoc Hung");
        Mockito.when(userStorage.getByUsername(Mockito.anyString())).thenAnswer(new Answer<ComkerUserDTO>() {

            @Override
            public ComkerUserDTO answer(InvocationOnMock invocation) throws Throwable {
                String username = (String) invocation.getArguments()[0];
                if (user.getUsername().equals(username)) {
                    return user;
                }
                return null;
            }
        });
        Response resp = memberClient.getUserProfile("pnhung177");
        Assert.assertTrue(resp.getStatus() == 200);

        ComkerUserDTO result = resp.readEntity(ComkerUserDTO.class);
        Assert.assertEquals(result.getUsername(), user.getUsername());
        Assert.assertEquals(result.getEmail(), user.getEmail());
        Assert.assertEquals(result.getFullname(), user.getFullname());
        */
    }
    
    @Test
    public void test_load_member_profile_for_editing() {
        System.out.println("------------- Not supported yet -------------");
    }

    @Test
    public void test_save_member_profile_changed() {
        System.out.println("------------- Not supported yet -------------");
    }

    @Test
    public void test_commit_change_password() {
        /*
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                String oldPassword = (String) invocation.getArguments()[0];
                String newPassword = (String) invocation.getArguments()[1];
                return null;
            }
        }).when(securityService).changePassword(Mockito.anyString(), Mockito.anyString());

        Response resp = memberClient.changePassword("12345678", "87654321");
        Mockito.verify(securityService).changePassword("12345678", "87654321");
        Assert.assertTrue(resp.getStatus() == 200);
        */
    }

    @Test
    public void test_request_reset_password() {
        /*
        Response resp = memberClient.resetPassword("pnhung177");
        Assert.assertTrue(resp.getStatus() == 200);

        Response resp2 = memberClient.resetPassword("pnhung177@drupalex.net");
        Assert.assertTrue(resp2.getStatus() == 200);
        */
    }

    @Test
    public void test_request_reset_password_with_invalide_username() {
        //Response resp = memberClient.resetPassword("invalid-username");
        //Assert.assertTrue(resp.getStatus() == ComkerInvalidParameterException.CODE);
    }
    
    @Test
    public void test_request_reset_password_with_invalid_email() {
        //Response resp = memberClient.resetPassword("invalid-email");
        //Assert.assertTrue(resp.getStatus() == ComkerInvalidParameterException.CODE);
    }

    @Test
    public void test_confirm_reset_password() {
        //Response resp = memberClient.resetPasswordConfirmation("confirmation-code");
        //Assert.assertTrue(resp.getStatus() == 200);
    }

    @Test
    public void test_confirm_reset_password_with_invalid_code() {
        //Response resp = memberClient.resetPasswordConfirmation("invalid-confirmation-code");
        //Assert.assertTrue(resp.getStatus() == ComkerInvalidParameterException.CODE);

        //ComkerExceptionResponse entity = resp.readEntity(ComkerExceptionResponse.class);
        //Assert.assertEquals(entity.getMessage(), "invalid_confirmation_code");
    }
}
