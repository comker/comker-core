package net.cokkee.comker.validation;

import java.text.MessageFormat;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.dto.ComkerCrewDTO;
import net.cokkee.comker.structure.ComkerKeyAndValueSet;
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
            for(int i=0; i<item.getGlobalRoleIds().length; i++) {
                String roleId = item.getGlobalRoleIds()[i];
                if (!roleDao.exists(roleId)) {
                    e.rejectValue(
                            MessageFormat.format("globalRoleIds[{0}]", new Object[] {i}),
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_role_id", roleId},
                            "GlobalRole[" + roleId + "] does not exists");
                }
            }
        }

        if (item.getScopedRoleIds() != null) {
            ComkerKeyAndValueSet[] set = item.getScopedRoleIds();
            for(int i=0; i<set.length; i++) {
                e.pushNestedPath(MessageFormat.format("scopedRoleIds[{0}]", new Object[] {i}));

                String spotId = set[i].getKey();
                if (!spotDao.exists(spotId)) {
                    e.rejectValue("key",
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_spot_id", spotId},
                            "Spot[" + spotId + "] does not exists");
                }

                String[] values = set[i].getValues();
                for(int j=0; j<values.length; j++) {
                    String roleId = values[i];
                    if (!roleDao.exists(roleId)) {
                        e.rejectValue(
                                MessageFormat.format("values[{0}]", new Object[] {i}),
                                "msg.__reftype__with__id__does_not_exists",
                                new Object[] {"msg.field_role_id", roleId},
                                "ScopedRole[" + roleId + "] does not exists");
                    }
                }

                e.popNestedPath();
            }
        }
    }
}
