package net.cokkee.comker.dao;

import java.util.List;
import net.cokkee.comker.model.po.ComkerRole;
import org.hibernate.SessionFactory;

/**
 *
 * @author drupalex
 */
public interface ComkerRoleDao {

    List<ComkerRole> findAll();

    public static class Hibernate implements ComkerRoleDao {

        private SessionFactory sessionFactory;

        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        public List<ComkerRole> findAll() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

