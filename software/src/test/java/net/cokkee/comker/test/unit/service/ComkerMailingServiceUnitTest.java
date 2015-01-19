package net.cokkee.comker.test.unit.service;

import javax.mail.internet.MimeMessage;
import net.cokkee.comker.model.msg.ComkerMailAddress;
import net.cokkee.comker.model.msg.ComkerMailMessage;
import net.cokkee.comker.rule.Log4jConfigRule;
import net.cokkee.comker.rule.SmtpServerRule;
import net.cokkee.comker.rule.SmtpServerSupport;

import net.cokkee.comker.service.ComkerMailingService;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.ClassRule;
import org.junit.Rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/test/unit/service/ComkerMailingServiceUnitTest.xml"})
public class ComkerMailingServiceUnitTest {

    @ClassRule
    public static Log4jConfigRule log4j = new Log4jConfigRule();
    
    @Rule
    public SmtpServerRule smtp = new SmtpServerRule(new SmtpServerSupport() {
        @Override
        public int getPort() {
            return 12525;
        }

        @Override
        public String getHostname() {
            return "localhost";
        }
    });

    @Autowired
    @Qualifier(value = "comkerMailingService")
    private ComkerMailingService mailingService;

    @Test
    public void sendMail_with_empty_message() throws Exception {

        mailingService.sendMail(
                new ComkerMailAddress()
                        .setFrom("pnhung177@gmail.com")
                        .setTo("myfriends@comker.com"), 
                new ComkerMailMessage()
                        .setSubject("Here is a sample subject!")
                        .setContentEmptiable(true));
        
        Assert.assertTrue(smtp.getMessages().size() == 1);
        
        MimeMessage message = smtp.getMessages().iterator().next().getMimeMessage();
        Assert.assertEquals("Here is a sample subject!", message.getSubject());
    }
    
    @Test
    public void sendMail_with_empty_subject() throws Exception {

        mailingService.sendMail(
                new ComkerMailAddress()
                        .setFrom("pnhung177@gmail.com")
                        .setTo("myfriends@comker.com"), 
                new ComkerMailMessage()
                        .setContent("Here is a sample message!"));
        
        Assert.assertTrue(smtp.getMessages().size() == 1);
        
        MimeMessage message = smtp.getMessages().iterator().next().getMimeMessage();
        Assert.assertNull(message.getSubject());
        Assert.assertTrue(message.getContent().toString()
                .contains("Here is a sample message!"));
    }
}
