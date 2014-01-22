package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;

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
public interface ComkerPermissionDao {

    ComkerPermission find(String query, Map<String,Object> params);
    
    ComkerPermission findWhere(Map<String,Object> params);

    Integer count();

    Integer countWhere(Map<String,Object> params);

    List findAll(ComkerPager filter);

    List findAllWhere(Map<String,Object> params, ComkerPager filter);

    ComkerPermission get(String id);

    ComkerPermission getByAuthority(String authority);

    ComkerPermission save(ComkerPermission item);

    
    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerPermissionDao {

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerPermission find(String query, Map<String,Object> params) {
            Query q = getSessionFactory().getCurrentSession().createQuery(query);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
            return (ComkerPermission) q.uniqueResult();
        }
        
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerPermission findWhere(Map<String,Object> params) {
            List result = findAllWhere(params, ComkerPager.ONE);
            if (result.isEmpty()) return null;
            return (ComkerPermission)result.get(0);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Integer count() {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerPermission.class);
            c.setProjection(Projections.rowCount());
            return ((Long) c.uniqueResult()).intValue();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAll(ComkerPager filter) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerPermission.class);
            ComkerPager.apply(c, filter);
            return c.list();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Integer countWhere(Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerPermission.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            c.setProjection(Projections.rowCount());
            return ((Long) c.uniqueResult()).intValue();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAllWhere(Map<String,Object> params, ComkerPager filter) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerPermission.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            ComkerPager.apply(c, filter);
            return c.list();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerPermission get(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            return (ComkerPermission) session.get(ComkerPermission.class, id);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerPermission getByAuthority(String authority) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(FIELD_AUTHORITY, authority);
            return findWhere(params);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerPermission save(ComkerPermission item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }
    }
}
