package net.cokkee.comker.validation;

import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dto.ComkerRegistrationDTO;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;

/**
 *
 * @author drupalex
 */
public class ComkerRegistrationValidator extends ComkerAbstractValidator {

    private ComkerUserDao userDao = null;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerRegistrationDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerRegistrationDTO item = (ComkerRegistrationDTO) o;

        if (item.getEmail() == null) {
            e.rejectValue("email",
                    "msg.registration__field__should_be_not_null",
                    new Object[] {"msg.field_email"},
                    "Email value should not be null");
        } else if (!ComkerDataUtil.verifyEmail(item.getEmail())) {
            e.rejectValue("email",
                    "msg.registration__field__has_invalid_format",
                    new Object[] {"msg.field_email"},
                    "Email value is invalid format");
        } else {
            ComkerUserDPO dupItem = userDao.getByEmail(item.getEmail());
            if (dupItem != null) {
                e.rejectValue("email",
                        "msg.registration__field__has_duplicated_value",
                        new Object[] {"msg.field_email"},
                        "Email value is duplicated");
            }
        }
        
        if (item.getPassword() != null &&
                !ComkerDataUtil.verifyPassword(item.getPassword())) {
            e.rejectValue("password",
                    "msg.registration__field__has_invalid_format",
                    new Object[] {"msg.field_password"},
                    "Password value is invalid format");
        }
    }

}
