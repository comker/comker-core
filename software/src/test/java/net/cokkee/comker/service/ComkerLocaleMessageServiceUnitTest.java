package net.cokkee.comker.service;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/service/ComkerLocaleMessageServiceUnitTest.xml"})
public class ComkerLocaleMessageServiceUnitTest {

    @Autowired
    private ComkerLocaleMessageService testLocaleMessageService = null;

    @Before
    public void init() {
    }

    @Test
    public void test_message_without_arguments() {
        String msg1 = testLocaleMessageService.getMessage("message_without_arguments",
                "Message without arguments");
        Assert.assertEquals("This is a message without arguments", msg1);
    }

    @Test
    public void test_message_with_arguments() {
        String msg1 = testLocaleMessageService.getMessage("message_without_arguments", null,
                "Message without arguments");
        Assert.assertEquals("This is a message without arguments", msg1);
    }
}
