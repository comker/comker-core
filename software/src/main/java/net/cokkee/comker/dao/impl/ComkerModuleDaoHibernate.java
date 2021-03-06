package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.dpo.ComkerModuleDPO;

import org.hibernate.Criteria;
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
public class ComkerModuleDaoHibernate extends ComkerAbstractDaoHibernate
            implements ComkerModuleDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerModuleDPO find(String query, Map<String,Object> params) {
        Query q = getSessionFactory().getCurrentSession().createQuery(query);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            q.setParameter(param.getKey(), param.getValue());
        }
        return (ComkerModuleDPO) q.uniqueResult();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerModuleDPO findWhere(Map<String,Object> params) {
        List result = findAllWhere(params, ComkerQueryPager.ONE);
        if (result.isEmpty()) return null;
        return (ComkerModuleDPO)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count() {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerModuleDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerModuleDPO.class);
        ComkerQueryPager.apply(c, filter);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer countWhere(Map<String,Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerModuleDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerModuleDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerQueryPager.apply(c, filter);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean exists(String id) {
        Query query = this.getSessionFactory().getCurrentSession().
                createQuery(MessageFormat.format(
                    "select 1 from {0} t where t.id = :id",
                    new Object[] {ComkerModuleDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerModuleDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (ComkerModuleDPO) session.get(ComkerModuleDPO.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerModuleDPO getByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_CODE, code);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerModuleDPO save(ComkerModuleDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }
}
