package net.cokkee.comker.service;

import java.util.HashMap;
import java.util.Map;

import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerInitializationService {

    void initDefaultRolesAndPermissions();
    
    public static class Impl implements ComkerInitializationService {

        private ComkerRoleDao roleDao = null;

        public ComkerRoleDao getRoleDao() {
            return roleDao;
        }

        public void setRoleDao(ComkerRoleDao roleDao) {
            this.roleDao = roleDao;
        }

        private ComkerPermissionDao permissionDao = null;

        public ComkerPermissionDao getPermissionDao() {
            return permissionDao;
        }

        public void setPermissionDao(ComkerPermissionDao permissionDao) {
            this.permissionDao = permissionDao;
        }


        //----------------------------------------------------------------------

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void initDefaultRolesAndPermissions() {
            ComkerPermission permission1 = getOrCreatePermission("ROLE_SYSA");
            ComkerPermission permission2 = getOrCreatePermission("ROLE_USER");
            ComkerPermission permission3 = getOrCreatePermission("ROLE_TEST");

            ComkerRole role1 = getOrCreateRole("Administrator", "Quản trị", "Quản lý hệ thống");
            role1.addPermission(permission1);
            role1.addPermission(permission2);
            getRoleDao().save(role1);

            getOrCreateRole("Manager", "Quản lý site", "Người quản lý một site");
            getOrCreateRole("Member", "Thành viên", "Người dùng thông thường");
        }

        private ComkerRole getOrCreateRole(String code, String name, String description) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(ComkerRoleDao.FIELD_CODE, code);

            ComkerRole role = getRoleDao().findWhere(params);
            if (role == null) {
                role = new ComkerRole(code, name, description);
                role = getRoleDao().save(role);
            }

            return role;
        }

        private ComkerPermission getOrCreatePermission(String name) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(ComkerPermissionDao.FIELD_AUTHORITY, name);

            ComkerPermission permission = getPermissionDao().findWhere(params);
            if (permission == null) {
                permission = new ComkerPermission(name);
                permission = getPermissionDao().save(permission);
            }
            
            return permission;
        }
    }
}
