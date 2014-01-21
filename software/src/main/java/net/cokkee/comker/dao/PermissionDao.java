package net.cokkee.comker.dao;

import org.hibernate.SessionFactory;

/**
 *
 * @author drupalex
 */
public interface PermissionDao {
    

    public static class Hibernate implements PermissionDao {

        private SessionFactory sessionFactory;

        public SessionFactory getSessionFactory() {
            return sessionFactory;
        }

        public void setSessionFactory(SessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }
    }
}
