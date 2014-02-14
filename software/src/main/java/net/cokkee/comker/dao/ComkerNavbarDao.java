package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cokkee.comker.model.po.ComkerNavbarNode;

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
public interface ComkerNavbarDao {

    Integer count();

    ComkerNavbarNode getNavbarTree();

    ComkerNavbarNode getNavbarTree(Map<String,Object> params);

    List getNavbarList();
    
    List getNavbarList(Map<String,Object> params);

    ComkerNavbarNode get(String id);

    ComkerNavbarNode update(ComkerNavbarNode item);

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    
    public static class Hibernate extends ComkerAbstractDao.Hibernate
            implements ComkerNavbarDao {

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public Integer count() {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);
            c.setProjection(Projections.rowCount());
            return ((Long) c.uniqueResult()).intValue();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerNavbarNode getNavbarTree() {
            return getNavbarTree(new HashMap<String,Object>());
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerNavbarNode getNavbarTree(Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);
            c.add(Restrictions.isNull(FIELD_PARENT));
            ComkerNavbarNode root = (ComkerNavbarNode) c.uniqueResult();

            

            return root;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List getNavbarList() {
            return getNavbarList(new HashMap<String,Object>());
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List getNavbarList(Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);
            return c.list();
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerNavbarNode get(String id) {
            Session session = this.getSessionFactory().getCurrentSession();
            return (ComkerNavbarNode) session.get(ComkerNavbarNode.class, id);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
        public ComkerNavbarNode update(ComkerNavbarNode item) {
            Session session = this.getSessionFactory().getCurrentSession();
            session.saveOrUpdate(item);
            return item;
        }
    }
}
