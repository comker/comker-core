package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cokkee.comker.dao.ComkerSpotDao;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerSpotDPO;

import org.hibernate.Criteria;
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
public class ComkerSpotDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerSpotDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSpotDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve,ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSpotDPO.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSpotDPO findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSpotDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerSpotDPO)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerSpotDPO.class);
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
                    new Object[] {ComkerSpotDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSpotDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerSpotDPO item = (ComkerSpotDPO) session.get(ComkerSpotDPO.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerSpotDPO getByCode(String code) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_CODE, code);
        ComkerSpotDPO item = findWhere(params);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String create(ComkerSpotDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        return String.valueOf(session.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerSpotDPO update(ComkerSpotDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerSpotDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }
}
