package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import net.cokkee.comker.dao.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cokkee.comker.model.ComkerQueryPager;
import net.cokkee.comker.model.ComkerQuerySieve;
import net.cokkee.comker.model.po.ComkerCrew;
import net.cokkee.comker.model.po.ComkerUser;
import net.cokkee.comker.model.po.ComkerUserJoinCrew;
import net.cokkee.comker.model.po.ComkerUserJoinCrewPk;
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

    private ComkerCrewDao crewDao = null;

    public void setCrewDao(ComkerCrewDao crewDao) {
        this.crewDao = crewDao;
    }

    //----------------------------------------------------------------------

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count(ComkerQuerySieve sieve) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUser.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAll(ComkerQuerySieve sieve, ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUser.class);
        ComkerQueryPager.apply(c, pager);
        return c.list();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUser findWhere(Map<String, Object> params) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUser.class);
        for(Map.Entry<String,Object> param : params.entrySet()) {
            c.add(Restrictions.eq(param.getKey(), param.getValue()));
        }
        ComkerUser item = (ComkerUser)c.uniqueResult();
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List findAllWhere(Map<String,Object> params, ComkerQueryPager pager) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerUser.class);
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
                    new Object[] {ComkerUser.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUser get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerUser item = (ComkerUser) session.get(ComkerUser.class, id);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUser getByEmail(String email) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_EMAIL, email);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerUser getByUsername(String username) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(FIELD_USERNAME, username);
        return findWhere(params);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerUser create(ComkerUser item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerUser update(ComkerUser item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerUser item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        ComkerUser item = (ComkerUser) session.get(ComkerUser.class, id);
        session.delete(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void addCrew(ComkerUser user, ComkerCrew crew) {
        ComkerUserJoinCrewPk id = new ComkerUserJoinCrewPk(user, crew);
        ComkerUserJoinCrew item = new ComkerUserJoinCrew();
        item.setPk(id);
        Session session = this.getSessionFactory().getCurrentSession();
        if (session.get(ComkerUserJoinCrew.class, id) == null) {
            session.saveOrUpdate(item);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void removeCrew(ComkerUser user, ComkerCrew crew) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.delete(new ComkerUserJoinCrew(user, crew));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void collectCrew(Set<ComkerCrew> bag, ComkerUser user) {
        if (bag == null) return;
        Session session = this.getSessionFactory().getCurrentSession();
        session.buildLockRequest(LockOptions.NONE).lock(user);
        List<ComkerUserJoinCrew> list = user.getUserJoinCrewList();
        for(ComkerUserJoinCrew item:list) {
            bag.add(item.getCrew());
        }
    }
}

