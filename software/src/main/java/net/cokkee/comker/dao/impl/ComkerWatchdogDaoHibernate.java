package net.cokkee.comker.dao.impl;

import net.cokkee.comker.dao.ComkerAbstractDao;
import net.cokkee.comker.dao.ComkerWatchdogDao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.exception.ComkerFieldDuplicatedException;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerWatchdog;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public class ComkerWatchdogDaoHibernate extends ComkerAbstractDao.Hibernate
        implements ComkerWatchdogDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerWatchdog findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerWatchdog.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerWatchdog)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerPager filter) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerWatchdog.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerPager.apply(c, filter);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerWatchdog get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerWatchdog item = (ComkerWatchdog) session.get(ComkerWatchdog.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerWatchdog create(ComkerWatchdog item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerWatchdog update(ComkerWatchdog item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerWatchdog item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerWatchdog item = (ComkerWatchdog) session.get(ComkerWatchdog.class, id);
        session.delete(item);
    }
}
