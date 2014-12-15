package net.cokkee.comker.test.unit.controller;

import net.cokkee.comker.controller.ComkerExceptionRaiser;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.hamcrest.Matchers;

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
@ContextConfiguration("classpath:/test/unit/controller/ComkerExceptionRaiserUnitTest.xml")
public class ComkerExceptionRaiserUnitTest {
    
    @Autowired
    private ComkerExceptionRaiser controller;
    
    @Autowired
    private WebApplicationContext wac;
    
    protected MockMvc mockMvc;
    
    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.wac)
                .build();
    }
    
    @Test
    public void throwComkerObjectNotFoundException() throws Exception {
 
        ResultActions result = mockMvc.perform(get("/comker/exception/notfound"));
        
        result.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerObjectNotFoundException")))
                .andExpect(jsonPath("$.label", Matchers.is("role_with__id__not_found")));
        //result.andDo(print());
    }
    
    @Test
    public void throwComkerValidationFailedException() throws Exception {
 
        ResultActions result = mockMvc.perform(get("/comker/exception/validationfailed"));
        
        result.andExpect(status().isNotAcceptable())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors", Matchers.hasSize(1)))
                .andExpect(jsonPath("$.fieldErrors[0].code", Matchers.is("msg.__field__should_be_not_null")))
                .andExpect(jsonPath("$.fieldErrors[0].arguments[0]", Matchers.is("msg.field_code")))
                .andExpect(jsonPath("$.fieldErrors[0].defaultMessage", Matchers.is("Code value should not be null")))
                .andExpect(jsonPath("$.clazz", Matchers.is("ComkerValidationFailedException")));
        //result.andDo(print());
    }
}
