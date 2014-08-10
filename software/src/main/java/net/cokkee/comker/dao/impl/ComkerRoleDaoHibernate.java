package net.cokkee.comker.dao.impl;

import net.cokkee.comker.dao.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;

import net.cokkee.comker.model.po.ComkerRole;
import net.cokkee.comker.model.po.ComkerRoleJoinPermission;
import net.cokkee.comker.model.po.ComkerRoleJoinPermissionPk;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerRoleDaoHibernate extends ComkerAbstractDaoHibernate
            implements ComkerRoleDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count() {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRole.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRole.class);
        ComkerPager.apply(c, filter);
        return c.list();
    }

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
    public List findAllWhere(Map<String,Object> params, ComkerPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRole.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerPager.apply(c, filter);
        List list = c.list();

        return list;
    }

    @Deprecated
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getAuthorities(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return getAuthorities((ComkerRole) session.load(ComkerRole.class, id));
    }

    @Deprecated
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Set<String> getAuthorities(ComkerRole role) {
        Session session = this.getSessionFactory().getCurrentSession();
        Set<String> authorities = new HashSet<String>();
        for(ComkerRoleJoinPermission item:role.getRoleJoinPermissionList()) {
            authorities.add(item.getPermission().getAuthority());
        }
        return authorities;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRole get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (ComkerRole) session.get(ComkerRole.class, id);
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
    public ComkerRole create(ComkerRole item) {
        Session session = this.getSessionFactory().getCurrentSession();

        if (getByCode(item.getCode()) != null) {
            throw new ComkerFieldDuplicatedException(400, "Role has already existed");
        }

        session.save(item);

        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerRole item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerRole item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectPermission(Set<ComkerPermission> bag, ComkerRole role) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(role);
        List<ComkerRoleJoinPermission> list = role.getRoleJoinPermissionList();
        for(ComkerRoleJoinPermission item:list) {
            bag.add(item.getPermission());
        }
    }
}
