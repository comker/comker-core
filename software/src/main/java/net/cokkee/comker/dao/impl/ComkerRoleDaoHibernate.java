package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerPermissionDPO;

import net.cokkee.comker.model.dpo.ComkerRoleDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionDPO;
import net.cokkee.comker.model.dpo.ComkerRoleJoinPermissionPK;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
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
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRoleDPO.class);
        applyCriteria(c, sieve);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRoleDPO.class);
        applyCriteria(c, sieve);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    private void applyCriteria(Criteria c, ComkerQuerySieve sieve) {
        if (sieve == null) return;
        if(Boolean.FALSE.equals(sieve.getCriterion(FIELD_GLOBAL))) {
            c.add(Restrictions.eq(FIELD_GLOBAL, Boolean.FALSE));
        } else if(Boolean.TRUE.equals(sieve.getCriterion(FIELD_GLOBAL))) {
            c.add(Restrictions.eq(FIELD_GLOBAL, Boolean.TRUE));
        }
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDPO findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRoleDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerRoleDPO)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRoleDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerQueryPager.apply(c, filter);
        List list = c.list();

        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean exists(String id) {
        Query query = this.getSessionFactory().getCurrentSession().
                createQuery(MessageFormat.format(
                    "select 1 from {0} t where t.id = :id",
                    new Object[] {ComkerRoleDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (ComkerRoleDPO) session.get(ComkerRoleDPO.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRoleDPO getByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_CODE, code);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerRoleDPO create(ComkerRoleDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerRoleDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerRoleDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addPermission(ComkerRoleDPO role, ComkerPermissionDPO permission) {
        ComkerRoleJoinPermissionPK id = new ComkerRoleJoinPermissionPK(role, permission);
        ComkerRoleJoinPermissionDPO item = new ComkerRoleJoinPermissionDPO();
        item.setPk(id);
        Session session = this.getSessionFactory().getCurrentSession();
        if (session.get(ComkerRoleJoinPermissionDPO.class, id) == null) {
            session.saveOrUpdate(item);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removePermission(ComkerRoleDPO role, ComkerPermissionDPO permission) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(new ComkerRoleJoinPermissionDPO(role, permission));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectPermission(Set<ComkerPermissionDPO> bag, ComkerRoleDPO role) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(role);
        List<ComkerRoleJoinPermissionDPO> list = role.getRoleJoinPermissionList();
        for(ComkerRoleJoinPermissionDPO item:list) {
            bag.add(item.getPermission());
        }
    }
}
