package net.cokkee.comker.validation;

import net.cokkee.comker.dao.ComkerModuleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.dto.ComkerSpotDTO;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author drupalex
 */
public class ComkerSpotValidator extends ComkerAbstractValidator {

    private ComkerSpotDao spotDao = null;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }

    private ComkerModuleDao moduleDao = null;

    public void setModuleDao(ComkerModuleDao moduleDao) {
        this.moduleDao = moduleDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerSpotDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerSpotDTO item = (ComkerSpotDTO) o;
        
        ValidationUtils.rejectIfEmpty(e, "name",
                "msg.__field__should_be_not_null",
                new Object[] {"msg.field_name"});
        
        if (item.getCode() == null) {
            e.rejectValue("code",
                    "msg.__field__should_be_not_null",
                    new Object[] {"msg.field_code"},
                    "Code value should not be null");
        } else if (!ComkerDataUtil.verifyCode(item.getCode())) {
            e.rejectValue("code",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_code"},
                    "Code value is invalid format");
        } else {
            ComkerSpotDPO dupItem = spotDao.getByCode(item.getCode());
            if (dupItem != null && !dupItem.getId().equals(item.getId())) {
                e.rejectValue("code",
                        "msg.__field__has_duplicated_value",
                        new Object[] {"msg.field_code"},
                        "Code value is duplicated");
            }
        }

        if (item.getModuleIds() != null) {
            for(String moduleId:item.getModuleIds()) {
                if (!moduleDao.exists(moduleId)) {
                    e.rejectValue("moduleIds",
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_module_id", moduleId},
                            "Module[" + moduleId + "] do not exists");
                }
            }
        }
    }
}
