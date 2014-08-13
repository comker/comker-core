package net.cokkee.comker.validation;

import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author drupalex
 */
public class ComkerCrewValidator extends ComkerAbstractValidator {

    private ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    private ComkerRoleDao roleDao = null;

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    private ComkerSpotDao spotDao = null;

    public void setSpotDao(ComkerSpotDao spotDao) {
        this.spotDao = spotDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerCrewDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerCrewDTO item = (ComkerCrewDTO) o;
        
        ValidationUtils.rejectIfEmpty(e, "name",
                "msg.__field__should_be_not_null",
                new Object[] {"msg.field_name"});
        
        if (item.getGlobalRoleIds() != null) {
            for(String roleId:item.getGlobalRoleIds()) {
                if (!roleDao.exists(roleId)) {
                    e.rejectValue("globalRoleIds",
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_role_id", roleId},
                            "GlobalRole[" + roleId + "] do not exists");
                }
            }
        }

        if (item.getLimitedSpotRoleIds() != null) {
            
        }
    }

}
