package net.cokkee.comker.validation;

import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.model.dto.ComkerPermissionDTO;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.util.ComkerDataUtil;
import org.springframework.validation.Errors;

/**
 *
 * @author drupalex
 */
public class ComkerPermissionValidator extends ComkerAbstractValidator {

    private ComkerPermissionDao permissionDao = null;

    public void setPermissionDao(ComkerPermissionDao permissionDao) {
        this.permissionDao = permissionDao;
    }

    @Override
    public boolean supports(Class<?> type) {
        return ComkerPermissionDTO.class.isAssignableFrom(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ComkerPermissionDTO item = (ComkerPermissionDTO) o;
        
        if (item.getAuthority() == null) {
            e.rejectValue("authority",
                    "msg.__field__should_be_not_null",
                    new Object[] {"msg.field_authority"},
                    "Authority value should not be null");
        } else if (!ComkerDataUtil.verifyAuthority(item.getAuthority())) {
            e.rejectValue("authority",
                    "msg.__field__has_invalid_format",
                    new Object[] {"msg.field_authority"},
                    "Authority value is invalid format");
        } else {
            ComkerPermission dupItem = permissionDao.getByAuthority(item.getAuthority());
            if (dupItem != null && !dupItem.getId().equals(item.getId())) {
                e.rejectValue("authority",
                        "msg.__field__has_duplicated_value",
                        new Object[] {"msg.field_authority"},
                        "Authority value is duplicated");
            }
        }
    }
}
