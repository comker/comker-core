package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;
import net.cokkee.comker.model.po.ComkerPermission;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author drupalex
 */
public interface ComkerPermissionDao {

    public static final String FIELD_AUTHORITY = "authority";

    ComkerPermission findWhere(Map<String,Object> params);

    //Integer count();

    //Integer count(String query, Map params);

    //Integer countWhere(Map<String,Object> params);

    //Collection findAll(ComkerPermission.Filter filter);

    List findAll(String query, Map<String,Object> params, ComkerPager filter);

    //Collection findAllWhere(Map<String,Object> params, ComkerPermission.Filter filter);

    //ComkerPermission load(String id);

    ComkerPermission save(ComkerPermission item);
    
    //void create(ComkerPermission item);

    //void delete(String id);

    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerPermissionDao {

        private SessionFactory sessionFactory;

        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        public ComkerPermission findWhere(Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerPermission.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            List result = c.list();
            if (result.isEmpty()) return null;
            return (ComkerPermission)result.get(0);
        }

        @Override
        public List findAll(String query, Map<String,Object> params, ComkerPager filter) {
            Query q = getSessionFactory().getCurrentSession().createQuery(query);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
            ComkerPager.apply(q, filter);
            return q.list();
        }

        @Override
        public ComkerPermission save(ComkerPermission item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }
    }
}
