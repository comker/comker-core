package net.cokkee.comker.test.unit.storage;

import java.util.UUID;

import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.model.msg.ComkerInformationResponse;
import net.cokkee.comker.storage.impl.ComkerRegistrationStorageImpl;
import net.cokkee.comker.validation.ComkerRegistrationValidator;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author drupalex
 */
@RunWith(MockitoJUnitRunner.class)
public class ComkerRegistrationStorageUnitTest {

    @Autowired
    @InjectMocks
    private ComkerRegistrationStorageImpl registrationStorage;

    @InjectMocks
    private ComkerRegistrationValidator registrationValidator;
    
    @Mock
    private ComkerRegistrationDao registrationDao;
    
    @Mock
    private ComkerUserDao userDao;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        registrationStorage.setRegistrationValidator(registrationValidator);
    }

    @Test
    public void register_ok() {
        final ComkerRegistrationDTO dto = new ComkerRegistrationDTO()
                .setEmail("demo@comker.com")
                .setPassword("#Dobietday#123456");
        
        Mockito.doAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                ComkerRegistrationDPO item = (ComkerRegistrationDPO) invocation.getArguments()[0];
                
                Assert.assertEquals(item.getEmail(), dto.getEmail());
                
                return null;
            }
        }).when(registrationDao).create(Mockito.any(ComkerRegistrationDPO.class));
        
        ComkerInformationResponse resp = registrationStorage.register(dto);
        
        Assert.assertEquals("msg.registration_successful_with__email__", resp.getMessageOrigin().getCode());
        Assert.assertEquals(dto.getEmail(), resp.getMessageOrigin().getArguments()[0]);
        
        Mockito.verify(userDao, Mockito.times(1)).getByEmail(dto.getEmail());
        
        Mockito.verify(registrationDao, Mockito.times(1)).create(Mockito.any(ComkerRegistrationDPO.class));
        
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(registrationDao);
    }

    @Test
    public void confirm_ok() {
        final ComkerRegistrationDPO data = new ComkerRegistrationDPO();
        data.setId(UUID.randomUUID().toString());
        data.setEmail("demo@comker.com");
        data.setEncodedPassword("abcdefghijklmnopqrstuvwxyz0123456789");
        
        Mockito.doAnswer(new Answer<ComkerRegistrationDPO>() {
            @Override
            public ComkerRegistrationDPO answer(InvocationOnMock invocation) throws Throwable {
                return data;
            }
        }).when(registrationDao).get(data.getId());
        
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ComkerRegistrationDPO item = (ComkerRegistrationDPO) invocation.getArguments()[0];
                Assert.assertEquals(item.getEmail(), data.getEmail());
                Assert.assertEquals(item.getEncodedPassword(), data.getEncodedPassword());
                Assert.assertTrue(item.isConfirmed());
                return null;
            }
        }).when(registrationDao).update(Mockito.any(ComkerRegistrationDPO.class));
        
        Mockito.doAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                ComkerUserDPO item = (ComkerUserDPO) invocation.getArguments()[0];
                
                Assert.assertEquals(item.getEmail(), data.getEmail());
                Assert.assertEquals(item.getPassword(), data.getEncodedPassword());
                
                return item;
            }
        }).when(userDao).create(Mockito.any(ComkerUserDPO.class));
        
        int result = registrationStorage.confirm(data.getId());
        
        Assert.assertTrue(result == ComkerRegistrationDTO.OK);
        
        Mockito.verify(registrationDao, Mockito.times(1)).get(data.getId());
        Mockito.verify(registrationDao, Mockito.times(1)).update(Mockito.any(ComkerRegistrationDPO.class));
        
        Mockito.verify(userDao, Mockito.times(1)).getByEmail(data.getEmail());
        Mockito.verify(userDao, Mockito.times(1)).create(Mockito.any(ComkerUserDPO.class));
        
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(registrationDao);
    }
    
    @Test
    public void confirm_registration_not_found() {
        String code = "not-found-registration-code";
        
        Mockito.doReturn(null).when(registrationDao).get(code);
        
        int result = registrationStorage.confirm(code);
        
        Assert.assertTrue(result == ComkerRegistrationDTO.CONFIRMATION_NOT_FOUND);

        Mockito.verify(registrationDao, Mockito.times(1)).get(code);
        
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(registrationDao);
    }
    
    @Test
    public void confirm_user_has_been_registered() {
        final ComkerRegistrationDPO data = new ComkerRegistrationDPO();
        data.setId(UUID.randomUUID().toString());
        data.setEmail("demo@comker.com");
        data.setEncodedPassword("abcdefghijklmnopqrstuvwxyz0123456789");
        
        Mockito.doAnswer(new Answer<ComkerRegistrationDPO>() {
            @Override
            public ComkerRegistrationDPO answer(InvocationOnMock invocation) throws Throwable {
                return data;
            }
        }).when(registrationDao).get(data.getId());
        
        Mockito.doAnswer(new Answer<ComkerUserDPO>() {
            @Override
            public ComkerUserDPO answer(InvocationOnMock invocation) throws Throwable {
                String email = (String) invocation.getArguments()[0];
                
                ComkerUserDPO item = new ComkerUserDPO(
                        data.getEmail(), data.getEmail(), 
                        data.getEncodedPassword(), data.getEmail());
                
                return item;
            }
        }).when(userDao).getByEmail(data.getEmail());
        
        int result = registrationStorage.confirm(data.getId());
        
        Assert.assertTrue(result == ComkerRegistrationDTO.USER_HAS_BEEN_REGISTED);
        
        Mockito.verify(registrationDao, Mockito.times(1)).get(data.getId());
        Mockito.verify(userDao, Mockito.times(1)).getByEmail(data.getEmail());
        
        Mockito.verifyNoMoreInteractions(userDao);
        Mockito.verifyNoMoreInteractions(registrationDao);
    }
}
