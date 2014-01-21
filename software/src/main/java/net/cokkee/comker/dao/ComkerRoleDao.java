package net.cokkee.comker.dao;

import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.po.ComkerRole;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao {

    public static final String FIELD_CODE = "code";

    ComkerRole findWhere(Map<String,Object> params);

    List<ComkerRole> findAll();

    ComkerRole save(ComkerRole item);

    public static class Hibernate implements ComkerRoleDao {

        private SessionFactory sessionFactory;

        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerRole findWhere(Map<String, Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerRole.class);
            for(Map.Entry<String,Object> param : params.entrySet()) {
                c.add(Restrictions.eq(param.getKey(), param.getValue()));
            }
            List result = c.list();
            if (result.isEmpty()) return null;
            return (ComkerRole)result.get(0);
        }
        
        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List<ComkerRole> findAll() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerRole save(ComkerRole item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }
    }
}

