package net.cokkee.comker.test.unit.controller;

import net.cokkee.comker.controller.ComkerRegistrationController;
import net.cokkee.comker.exception.ComkerValidationFailedException;
import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.model.error.ComkerResolvableMessage;
import net.cokkee.comker.model.msg.ComkerInformationResponse;
import net.cokkee.comker.storage.ComkerRegistrationStorage;
import net.cokkee.comker.util.ComkerDataUtil;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;
        
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("classpath:/test/unit/controller/ComkerRegistrationControllerUnitTest.xml")
public class ComkerRegistrationControllerUnitTest {
    
    @Autowired
    @InjectMocks
    private ComkerRegistrationController controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    @Mock
    private ComkerRegistrationStorage registrationStorage;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void doRegistration_ok() throws Exception {
        final ComkerRegistrationDTO data = new ComkerRegistrationDTO()
                .setEmail("pnhung177@gmail.com")
                .setPassword("12345678");
        
        String defaultMsg = "Your registration is success with email:" + data.getEmail();
        
        final ComkerInformationResponse info = new ComkerInformationResponse()
                .setMessage(defaultMsg)
                .setMessageOrigin(new ComkerResolvableMessage(
                        "msg.registration_successful_with__email__",
                        new Object[] {data.getEmail()}, defaultMsg));
        
        Mockito.when(registrationStorage.register(Mockito.any(ComkerRegistrationDTO.class)))
                .thenAnswer(new Answer<ComkerInformationResponse>() {
                    @Override
                    public ComkerInformationResponse answer(InvocationOnMock invocation) 
                            throws Throwable {
                        return info;
                    }
                });
        
        ResultActions result = mockMvc.perform(post("/comker/registration/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ComkerDataUtil.convertObjectToJson(data)));
        
        //result.andDo(print());
        
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result.andExpect(jsonPath("$.message", 
                        Matchers.notNullValue()))
                .andExpect(jsonPath("$.messageOrigin.code", 
                        Matchers.is("msg.registration_successful_with__email__")))
                .andExpect(jsonPath("$.messageOrigin.arguments[0]", 
                        Matchers.is(data.getEmail())));
        
        Mockito.verify(registrationStorage, Mockito.times(1))
                .register(Mockito.any(ComkerRegistrationDTO.class));
        Mockito.verifyNoMoreInteractions(registrationStorage);
    }
    
    @Test
    public void doRegistration_email_already_exists() throws Exception {
        ComkerRegistrationDTO data = new ComkerRegistrationDTO()
                .setEmail("pnhung177@gmail.com")
                .setPassword("12345678");
        
        Mockito.when(registrationStorage.register(Mockito.any(ComkerRegistrationDTO.class)))
                .thenAnswer(new Answer<ComkerInformationResponse>() {
                    @Override
                    public ComkerInformationResponse answer(InvocationOnMock invocation) 
                            throws Throwable {
                        ComkerRegistrationDTO entity = (ComkerRegistrationDTO)invocation.getArguments()[0];
                        ComkerValidationFailedException errors =
                                new ComkerValidationFailedException(entity, entity.getClass().getName());

                        errors.rejectValue("email",
                                    "msg.registration_fail__email__already_exists",
                                    new Object[] {"msg.field_email"},
                                    "This Email address has already existed");

                        throw errors;
                    }
                });
        
        ResultActions result = mockMvc.perform(post("/comker/registration/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ComkerDataUtil.convertObjectToJson(data)));
        
        //result.andDo(print());
        
        result.andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
        result.andExpect(jsonPath("$.fieldErrors", 
                        Matchers.hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].code", 
                        Matchers.is("msg.registration_fail__email__already_exists")))
                .andExpect(jsonPath("$.fieldErrors[0].arguments[0]", 
                        Matchers.is("msg.field_email")))
                .andExpect(jsonPath("$.clazz", 
                        Matchers.is("ComkerValidationFailedException")));
        
        Mockito.verify(registrationStorage, Mockito.times(1))
                .register(Mockito.any(ComkerRegistrationDTO.class));
        Mockito.verifyNoMoreInteractions(registrationStorage);
    }
    
    @Test
    public void doConfirmation_ok() throws Exception {
        String code = "should-change-to-valid_registration-code";
        
        Mockito.when(registrationStorage.confirm(code)).thenReturn(0);
        
        ResultActions result = mockMvc.perform(get("/comker/registration/confirm/{code}", code));
        
        //result.andDo(print());
        
        result.andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html#/dashboard"));
        
        Mockito.verify(registrationStorage, Mockito.times(1)).confirm(code);
        Mockito.verifyNoMoreInteractions(registrationStorage);
    }
    
    @Test
    public void doConfirmation_with_invalid_code_should_be_failure() throws Exception {
        String code = "invalid_registration_code";
        
        Mockito.when(registrationStorage.confirm(code)).thenReturn(1);
        
        ResultActions result = mockMvc.perform(get("/comker/registration/confirm/{code}", code));
        
        //result.andDo(print());
        
        result.andExpect(status().isFound())
                .andExpect(redirectedUrl("index.html#/registrationfailed"));
        
        Mockito.verify(registrationStorage, Mockito.times(1)).confirm(code);
        Mockito.verifyNoMoreInteractions(registrationStorage);
    }
}
