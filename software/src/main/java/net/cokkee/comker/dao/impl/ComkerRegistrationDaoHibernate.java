package net.cokkee.comker.dao.impl;

import java.util.List;

import net.cokkee.comker.dao.ComkerRegistrationDao;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerRegistrationDPO;

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
public class ComkerRegistrationDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerRegistrationDao {

        @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRegistrationDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List list(ComkerQuerySieve sieve,ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerRegistrationDPO.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerRegistrationDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerRegistrationDPO item = (ComkerRegistrationDPO) session.get(ComkerRegistrationDPO.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public String create(ComkerRegistrationDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        return String.valueOf(session.save(item));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerRegistrationDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerRegistrationDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }
}
