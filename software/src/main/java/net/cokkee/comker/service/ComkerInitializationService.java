package net.cokkee.comker.service;

import java.util.HashMap;
import java.util.Map;
import net.cokkee.comker.dao.ComkerCrewDao;
import net.cokkee.comker.dao.ComkerPermissionDao;
import net.cokkee.comker.dao.ComkerRoleDao;
import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerPermission;
import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerSpot;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerInitializationService {

    void initSampleData();

    public static class Impl implements ComkerInitializationService {

        private ComkerSpotDao spotDao = null;

        public ComkerSpotDao getSpotDao() {
            return spotDao;
        }

        public void setSpotDao(ComkerSpotDao spotDao) {
            this.spotDao = spotDao;
        }

        private ComkerCrewDao crewDao = null;

        public ComkerCrewDao getCrewDao() {
            return crewDao;
        }

        public void setCrewDao(ComkerCrewDao crewDao) {
            this.crewDao = crewDao;
        }

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

        private Map<String,ComkerSpot> sampleSpots = new HashMap<String,ComkerSpot>();
        private Map<String,ComkerCrew> sampleCrews = new HashMap<String,ComkerCrew>();
        private Map<String,ComkerRole> sampleRoles = new HashMap<String,ComkerRole>();
        private Map<String,ComkerPermission> samplePermissions = new HashMap<String,ComkerPermission>();

        public Impl() {
            ComkerSpot spot;
            spot = new ComkerSpot("buocnho.com", "Buocnho Training & Technology", "");
            sampleSpots.put(spot.getCode(), spot);
            spot = new ComkerSpot("pctu.edu.vn", "Đại học Phan Châu Trinh - Hội An", "");
            sampleSpots.put(spot.getCode(), spot);

            ComkerCrew crew;
            crew = new ComkerCrew("Administrators (buocnho.com)", "Nhóm quản trị của buocnho.com");
            sampleCrews.put(crew.getName(), crew);
            crew = new ComkerCrew("Administrators (pctu.edu.vn)", "Nhóm quản trị của pctu.edu.vn");
            sampleCrews.put(crew.getName(), crew);
            crew = new ComkerCrew("Members (pctu.edu.vn)", "Nhóm thành viên của pctu.edu.vn");
            sampleCrews.put(crew.getName(), crew);
            crew = new ComkerCrew("Members (buocnho.com)", "Nhóm thành viên của buocnho.com");
            sampleCrews.put(crew.getName(), crew);
            crew = new ComkerCrew("Public", "Nhóm thành viên công cộng");
            sampleCrews.put(crew.getName(), crew);

            ComkerRole role;
            role = new ComkerRole("Administrator", "Quản trị", "Quản lý hệ thống");
            sampleRoles.put(role.getCode(), role);
            role = new ComkerRole("Manager", "Quản lý site", "Người quản lý một site");
            sampleRoles.put(role.getCode(), role);
            role = new ComkerRole("Member", "Thành viên", "Người dùng thông thường");
            sampleRoles.put(role.getCode(), role);

            ComkerPermission permission;
            permission = new ComkerPermission("ROLE_SYSA");
            samplePermissions.put(permission.getAuthority(), permission);
            permission = new ComkerPermission("ROLE_USER");
            samplePermissions.put(permission.getAuthority(), permission);
            permission = new ComkerPermission("ROLE_TEST");
            samplePermissions.put(permission.getAuthority(), permission);
        }

        //----------------------------------------------------------------------

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void initSampleData() {
            initSamplePermissions();
            initSampleRoles();
            initSampleSpots();
            initSampleCrews();
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private void initSampleSpots() {
            for(String code: sampleSpots.keySet()) {
                getOrCreateSpot(code);
            }
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerSpot getOrCreateSpot(String code) {
            ComkerSpot item = getSpotDao().getByCode(code);
            if (item == null) {
                item = sampleSpots.get(code);
                if (item == null) return null;
                getSpotDao().save(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private void initSampleCrews() {
            for(String code: sampleCrews.keySet()) {
                getOrCreateCrew(code);
            }

            getCrewDao().addRoleWithSpot(
                    getCrewDao().getByName("Administrators (buocnho.com)"),
                    getRoleDao().getByCode("Administrator"),
                    getSpotDao().getByCode("buocnho.com"));
            getCrewDao().addRoleWithSpot(
                    getCrewDao().getByName("Administrators (pctu.edu.vn)"),
                    getRoleDao().getByCode("Administrator"),
                    getSpotDao().getByCode("pctu.edu.vn"));

            getCrewDao().addRoleWithSpot(
                    getCrewDao().getByName("Members (buocnho.com)"),
                    getRoleDao().getByCode("Member"),
                    getSpotDao().getByCode("buocnho.com"));
            getCrewDao().addRoleWithSpot(
                    getCrewDao().getByName("Members (pctu.edu.vn)"),
                    getRoleDao().getByCode("Member"),
                    getSpotDao().getByCode("pctu.edu.vn"));

            getCrewDao().addGlobalRole(
                    getCrewDao().getByName("Public"),
                    getRoleDao().getByCode("Member"));
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerCrew getOrCreateCrew(String name) {
            ComkerCrew item = getCrewDao().getByName(name);
            if (item == null) {
                item = sampleCrews.get(name);
                if (item == null) return null;
                getCrewDao().save(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private void initSampleRoles() {
            for(String code: sampleRoles.keySet()) {
                getOrCreateRole(code);
            }
            getRoleDao().addPermission(
                    getRoleDao().getByCode("Administrator"),
                    getPermissionDao().getByAuthority("ROLE_SYSA"));
            getRoleDao().addPermission(
                    getRoleDao().getByCode("Administrator"),
                    getPermissionDao().getByAuthority("ROLE_USER"));

            getRoleDao().addPermission(
                    getRoleDao().getByCode("Manager"),
                    getPermissionDao().getByAuthority("ROLE_USER"));

            getRoleDao().addPermission(
                    getRoleDao().getByCode("Member"),
                    getPermissionDao().getByAuthority("ROLE_USER"));
            getRoleDao().addPermission(
                    getRoleDao().getByCode("Member"),
                    getPermissionDao().getByAuthority("ROLE_TEST"));
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerRole getOrCreateRole(String code) {
            ComkerRole item = getRoleDao().getByCode(code);
            if (item == null) {
                item = sampleRoles.get(code);
                if (item == null) return null;
                getRoleDao().save(item);
            }
            return item;
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private void initSamplePermissions() {
            for(String code: samplePermissions.keySet()) {
                getOrCreatePermission(code);
            }
        }

        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        private ComkerPermission getOrCreatePermission(String authority) {
            ComkerPermission item = getPermissionDao().getByAuthority(authority);
            if (item == null) {
                item = samplePermissions.get(authority);
                if (item == null) return null;
                getPermissionDao().save(item);
            }
            return item;
        }
    }
}
