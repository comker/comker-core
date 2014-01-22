package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.po.ComkerPermission;

import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
import net.cokkee.comker.model.po.ComkerRoleJoinPermissionPk;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao extends ComkerAbstractDao {

    public static final String FIELD_CODE = "code";

    ComkerRole findWhere(Map<String,Object> params);

    Set<String> getAuthorities(String id);

    Set<String> getAuthorities(ComkerRole role);

    ComkerRole getByCode(String code);

    ComkerRole save(ComkerRole item);

    void addPermission(ComkerRole role, ComkerPermission permission);

    void removePermission(ComkerRole role, ComkerPermission permission);

    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerRoleDao {

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerRole findWhere(Map<String, Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerRole.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            List result = c.list();
            if (result.isEmpty()) return null;
            return (ComkerRole)result.get(0);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Set<String> getAuthorities(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            ComkerRole role = (ComkerRole) session.load(ComkerRole.class, id);
            Set<String> authorities = new HashSet<String>();
            for(ComkerRoleJoinPermission rjp:role.getRoleJoinPermissionList()) {
                authorities.add(rjp.getPermission().getAuthority());
            }
            return authorities;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Set<String> getAuthorities(ComkerRole role) {
            Session session = this.getSessionFactory().getCurrentSession();
            Set<String> authorities = new HashSet<String>();
            for(ComkerRoleJoinPermission rjp:role.getRoleJoinPermissionList()) {
                authorities.add(rjp.getPermission().getAuthority());
            }
            return authorities;
        }
        
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerRole getByCode(String code) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_CODE, code);
            return findWhere(params);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerRole save(ComkerRole item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void addPermission(ComkerRole role, ComkerPermission permission) {
            ComkerRoleJoinPermissionPk id = new ComkerRoleJoinPermissionPk(role, permission);
            ComkerRoleJoinPermission item = new ComkerRoleJoinPermission();
            item.setPk(id);
            Session session = this.getSessionFactory().getCurrentSession();
            if (session.get(ComkerRoleJoinPermission.class, id) == null) {
                session.saveOrUpdate(item);
            }
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public void removePermission(ComkerRole role, ComkerPermission permission) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.delete(new ComkerRoleJoinPermission(role, permission));
        }
    }
}

