package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.dpo.ComkerCrewDPO;
import net.cokkee.comker.model.dpo.ComkerUserDPO;
import net.cokkee.comker.model.dpo.ComkerUserJoinCrewDPO;
import net.cokkee.comker.model.dpo.ComkerUserJoinCrewPK;
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
public class ComkerUserDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerUserDao {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUserDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUserDPO.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDPO findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUserDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerUserDPO item = (ComkerUserDPO)c.uniqueResult();
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUserDPO.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerQueryPager.apply(c, pager);
        List list = c.list();
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean exists(String id) {
        Query query = this.getSessionFactory().getCurrentSession().
                createQuery(MessageFormat.format(
                    "select 1 from {0} t where t.id = :id",
                    new Object[] {ComkerUserDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerUserDPO item = (ComkerUserDPO) session.get(ComkerUserDPO.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDPO getByEmail(String email) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_EMAIL, email);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUserDPO getByUsername(String username) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_USERNAME, username);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerUserDPO create(ComkerUserDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerUserDPO update(ComkerUserDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerUserDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerUserDPO item = (ComkerUserDPO) session.get(ComkerUserDPO.class, id);
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addCrew(ComkerUserDPO user, ComkerCrewDPO crew) {
        ComkerUserJoinCrewPK id = new ComkerUserJoinCrewPK(user, crew);
        ComkerUserJoinCrewDPO item = new ComkerUserJoinCrewDPO();
        item.setPk(id);
        Session session = this.getSessionFactory().getCurrentSession();
        if (session.get(ComkerUserJoinCrewDPO.class, id) == null) {
            session.saveOrUpdate(item);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeCrew(ComkerUserDPO user, ComkerCrewDPO crew) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(new ComkerUserJoinCrewDPO(user, crew));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectCrew(Set<ComkerCrewDPO> bag, ComkerUserDPO user) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(user);
        List<ComkerUserJoinCrewDPO> list = user.getUserJoinCrewList();
        for(ComkerUserJoinCrewDPO item:list) {
            bag.add(item.getCrew());
        }
    }
}

