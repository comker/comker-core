package net.cokkee.comker.service;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.model.po.ComkerPermission;
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

        public void initDefaultRolesAndPermissions() {
            getOrCreatePermission("ROLE_SYSA");
            getOrCreatePermission("ROLE_TEST");
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
