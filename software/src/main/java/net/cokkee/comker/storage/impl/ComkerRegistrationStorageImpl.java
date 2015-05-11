package net.cokkee.comker.storage.impl;

import java.util.UUID;

import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.model.error.ComkerResolvableMessage;
import net.cokkee.comker.model.msg.ComkerInformationResponse;
import net.cokkee.comker.msg.model.ComkerMsgMailAddress;
import net.cokkee.comker.msg.model.ComkerMsgMailMessage;
import net.cokkee.comker.msg.service.ComkerMsgSendmailService;
import net.cokkee.comker.service.ComkerLocalizationService;
import net.cokkee.comker.storage.ComkerRegistrationStorage;
import net.cokkee.comker.validation.ComkerRegistrationValidator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerRegistrationStorageImpl extends ComkerAbstractStorageImpl
            implements ComkerRegistrationStorage {

    private ComkerRegistrationValidator registrationValidator;

    public void setRegistrationValidator(ComkerRegistrationValidator registrationValidator) {
        this.registrationValidator = registrationValidator;
    }
    
    private ComkerRegistrationDao registrationDao;

    public void setRegistrationDao(ComkerRegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }
    
    private ComkerUserDao userDao;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }
    
    private ComkerLocalizationService localizationService = null;

    public void setLocalizationService(ComkerLocalizationService localizationService) {
        this.localizationService = localizationService;
    }
    
    private PasswordEncoder passwordEncoder = null;

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
    private ComkerMsgSendmailService msgSendmailService = null;

    public void setMsgSendmailService(ComkerMsgSendmailService msgSendmailService) {
        this.msgSendmailService = msgSendmailService;
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerInformationResponse register(ComkerRegistrationDTO form) {
        invokeValidator(registrationValidator, form);
        
        ComkerRegistrationDPO dpo = new ComkerRegistrationDPO(
                form.getEmail(),
                encodePassword(form.getPassword()),
                generateConfirmationCode(form));
        
        String id = registrationDao.create(dpo);
        
        // create the confirmation email content
        StringBuilder sb = new StringBuilder();
        sb.append("<html>")
                .append("<body>")
                .append("<div>")
                .append("Click to this link <a href='")
                .append("http://localhost:7777/comker-app/ws/comker/registration/confirm/")
                .append(dpo.getConfirmationCode())
                .append("'>here</a> to confirm the registration.")
                .append("</div>")
                .append("</body>")
                .append("</html>");
        
        // create the address and message
        ComkerMsgMailAddress address = new ComkerMsgMailAddress();
        address.setFrom("contact@comker.com").setTo(form.getEmail());
        
        ComkerMsgMailMessage message = new ComkerMsgMailMessage();
        message.setSubject("Please confirm your registration")
                .setContent(sb.toString())
                .setHtmlContentType(true);
        
        // send email to confirm the registration
        msgSendmailService.sendMail(address, message);
        
        String defaultMsg = "A registration email was sent to " + form.getEmail() + 
                ". follow the instructions contained in the email to complete registration.";
        return new ComkerInformationResponse()
                .setMessage(defaultMsg)
                .setMessageOrigin(new ComkerResolvableMessage(
                        "msg.registration_successful_with__email__",
                        new Object[] {form.getEmail()}, defaultMsg));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int confirm(String code) {
        ComkerRegistrationDPO regDPO = registrationDao.getByCode(code);
        if (regDPO == null) return ComkerRegistrationDTO.CONFIRMATION_NOT_FOUND;
        
        ComkerUserDPO oldUsr = userDao.getByEmail(regDPO.getEmail());
        if (oldUsr != null) return ComkerRegistrationDTO.USER_HAS_BEEN_REGISTED;
        
        ComkerUserDPO newUsr = new ComkerUserDPO();
        
        newUsr.setEmail(regDPO.getEmail());
        newUsr.setFullname(regDPO.getEmail());
        newUsr.setUsername(regDPO.getEmail());
        newUsr.setPassword(regDPO.getEncodedPassword());
        
        userDao.create(newUsr);
        
        regDPO.setConfirmed(Boolean.TRUE);
        registrationDao.update(regDPO);
        
        return ComkerRegistrationDTO.OK;
    }
    
    private String encodePassword(String password) {
        return this.passwordEncoder.encode(password);
    }
    
    private String generateConfirmationCode(ComkerRegistrationDTO form) {
        return UUID.randomUUID().toString();
    }
}
