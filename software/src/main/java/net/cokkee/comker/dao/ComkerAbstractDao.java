package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;
import net.cokkee.comker.model.ComkerPager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerAbstractDao {

    public static final String FIELD_AUTHORITY = "authority";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODE = "code";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_FULLNAME = "fullname";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_PARENT = "parent";

    Integer count(String query, Map<String,Object> params);
    
    List findAll(String query, Map<String,Object> params, ComkerPager filter);

    public static abstract class Hibernate implements ComkerAbstractDao {

        private SessionFactory sessionFactory;

        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Integer count(String query, Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Query q = session.createQuery("SELECT COUNT(*) " + query);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
            return ((Long) q.uniqueResult()).intValue();
        }
        
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List findAll(String query, Map<String,Object> params, ComkerPager filter) {
            Query q = getSessionFactory().getCurrentSession().createQuery(query);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                q.setParameter(param.getKey(), param.getValue());
            }
            ComkerPager.apply(q, filter);
            return q.list();
        }
    }
}
