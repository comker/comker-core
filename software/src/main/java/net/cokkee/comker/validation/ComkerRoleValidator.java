package net.cokkee.comker.validation;

import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.model.dto.ComkerRoleDTO;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

/**
 *
 * @author drupalex
 */
public class ComkerRoleValidator extends ComkerAbstractValidator {

    private ComkerRoleDao roleDao = null;

    public void setRoleDao(ComkerRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    private ComkerPermissionDao permissionDao = null;

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerRoleDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerRoleDTO item = (ComkerRoleDTO) o;
        
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
            ComkerRole dupItem = roleDao.getByCode(item.getCode());
            if (dupItem != null && !dupItem.getId().equals(item.getId())) {
                e.rejectValue("code",
                        "msg.__field__has_duplicated_value",
                        new Object[] {"msg.field_code"},
                        "Code value is duplicated");
            }
        }

        if (item.getPermissionIds() != null) {
            for(String permissionId:item.getPermissionIds()) {
                if (!permissionDao.exists(permissionId)) {
                    e.rejectValue("permissionIds",
                            "msg.__reftype__with__id__does_not_exists",
                            new Object[] {"msg.field_permission_id", permissionId},
                            "Permission[" + permissionId + "] do not exists");
                }
            }
        }
    }

}
