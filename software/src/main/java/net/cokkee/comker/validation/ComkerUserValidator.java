package net.cokkee.comker.validation;

import java.text.MessageFormat;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerUserDao;
import net.cokkee.comker.model.dto.ComkerUserDTO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;

/**
 *
 * @author drupalex
 */
public class ComkerUserValidator extends ComkerAbstractValidator {

    private ComkerUserDao userDao = null;

    public void setUserDao(ComkerUserDao userDao) {
        this.userDao = userDao;
    }

    private ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerUserDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerUserDTO item = (ComkerUserDTO) o;

        if (item.getEmail() == null) {
            e.rejectValue("email",
                    "msg.__field__should_be_not_null",
                    new Object[] {"msg.field_email"},
                    "Email value should not be null");
        } else if (!ComkerDataUtil.verifyEmail(item.getEmail())) {
            e.rejectValue("email",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_email"},
                    "Email value is invalid format");
        } else {
            ComkerUserDPO dupItem = userDao.getByEmail(item.getEmail());
            if (dupItem != null && !dupItem.getId().equals(item.getId())) {
                e.rejectValue("email",
                        "msg.__field__has_duplicated_value",
                        new Object[] {"msg.field_email"},
                        "Email value is duplicated");
            }
        }
        
        if (item.getUsername() == null) {
            e.rejectValue("username",
                    "msg.__field__should_be_not_null",
                    new Object[] {"msg.field_username"},
                    "Username value should not be null");
        } else if (!ComkerDataUtil.verifyUsername(item.getUsername())) {
            e.rejectValue("username",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_username", item.getUsername()},
                    "Username value is invalid format");
        } else {
            ComkerUserDPO dupItem = userDao.getByUsername(item.getUsername());
            if (dupItem != null && !dupItem.getId().equals(item.getId())) {
                e.rejectValue("username",
                        "msg.__field__has_duplicated_value",
                        new Object[] {"msg.field_username", item.getUsername()},
                        "Username value is duplicated");
            }
        }

        if (item.getPassword() != null &&
                !ComkerDataUtil.verifyPassword(item.getPassword())) {
            e.rejectValue("password",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_password"},
                    "Password value is invalid format");
        }

        if (item.getFullname() != null &&
                !ComkerDataUtil.verifyFullname(item.getFullname())) {
            e.rejectValue("fullname",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_fullname", item.getFullname()},
                    "Fullname value is invalid format");
        }

        if (item.getCrewIds() != null) {
            for (int i=0; i<item.getCrewIds().length; i++) {
                String crewId = item.getCrewIds()[i];
                if (!crewDao.exists(crewId)) {
                    e.rejectValue(MessageFormat.format("crewIds[{0}]", new Object[] {i}),
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_crew_id", crewId},
                            "Crew[" + crewId + "] do not exists");
                }
            }
        }
    }

}
