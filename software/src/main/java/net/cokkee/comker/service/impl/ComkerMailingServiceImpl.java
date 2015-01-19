package net.cokkee.comker.service.impl;

import java.text.MessageFormat;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import net.cokkee.comker.exception.ComkerMailParsingException;
import net.cokkee.comker.exception.ComkerMailSendingException;
import net.cokkee.comker.model.error.ComkerExceptionExtension;
import net.cokkee.comker.model.msg.ComkerMailAddress;
import net.cokkee.comker.model.msg.ComkerMailMessage;
import net.cokkee.comker.service.ComkerMailingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

/**
 *
 * @author drupalex
 */
public class ComkerMailingServiceImpl implements ComkerMailingService {

    private static final Logger log = LoggerFactory.getLogger(ComkerMailingServiceImpl.class);
    
    private JavaMailSender mailSender;

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    @Override
    public void sendMail(ComkerMailAddress address, ComkerMailMessage message) {
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try{
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            helper.setFrom(address.getFrom());
            helper.setTo(address.getTo());
            if (address.getCc() != null) {
                helper.setCc(address.getCc());
            }
            if (address.getBcc() != null) {
                helper.setBcc(address.getBcc());
            }
            
            if (message.getSubject() != null) {
                helper.setSubject(message.getSubject());
            }
            
            if (message.getContent() != null) {
                helper.setText(message.getContent(), message.isHtmlContentType());
            }

            mailSender.send(mimeMessage);
        } catch (MailException e) {
            String defaultMessage = MessageFormat.format("Exception[{0}] => Message: {1}", 
                        new Object[]{e.getClass().getSimpleName(), e.getMessage()});
            
            if (log.isErrorEnabled()) {
                log.error(defaultMessage);
            }
            
            throw new ComkerMailSendingException(
                "comker_mail_sending_error", e, new ComkerExceptionExtension()
                    .setCode("error.comker_mail_sending_with__from__to__address")
                    .setArguments(new Object[] {
                        address.getFrom(), 
                        StringUtils.arrayToCommaDelimitedString(address.getTo())})
                    .setDefaultMessage(defaultMessage));
        } catch (MessagingException e) {
            String defaultMessage = MessageFormat.format("Exception[{0}] => Message: {1}", 
                        new Object[]{e.getClass().getSimpleName(), e.getMessage()});
            
            if (log.isErrorEnabled()) {
                log.error(MessageFormat.format("Exception[{0}] => Message: {1}", 
                        new Object[]{e.getClass().getSimpleName(), e.getMessage()}));
            }
            
            throw new ComkerMailParsingException(
                "comker_mail_parsing_error", e, new ComkerExceptionExtension()
                    .setCode("error.comker_mail_parsing_with__from__to__address")
                    .setArguments(new Object[] {
                        address.getFrom(), 
                        StringUtils.arrayToCommaDelimitedString(address.getTo())})
                    .setDefaultMessage(defaultMessage));
        }
    }
}
