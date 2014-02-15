package net.cokkee.comker.dao;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.cokkee.comker.model.po.ComkerNavbarNode;

import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
public interface ComkerNavbarDao {

    public static final String FILTER_ID = "id";
    public static final String FILTER_MODE = "mode";
    public static final String FILTER_MODE_INSIDE = "inside";
    public static final String FILTER_MODE_EXCEPT = "except";

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

        private static Logger log = LoggerFactory.getLogger(ComkerNavbarDao.Hibernate.class);

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
            return getNavbarTree(null);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public ComkerNavbarNode getNavbarTree(Map<String,Object> params) {
            if (params == null) params = new HashMap<String,Object>();

            Object id = params.get(FILTER_ID);
            Object mode = params.get(FILTER_MODE);

            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);

            Object exceptNodeId = null;
            if (id == null) {
                c.add(Restrictions.isNull(FIELD_PARENT));
            } else {
                if (FILTER_MODE_INSIDE.equals(mode)) {
                    c.add(Restrictions.idEq(id));
                }
                if (FILTER_MODE_EXCEPT.equals(mode)) {
                    c.add(Restrictions.isNull(FIELD_PARENT));
                    exceptNodeId = id;
                }
            }

            Queue<ComkerNavbarNode> queue = new LinkedList<ComkerNavbarNode>();

            ComkerNavbarNode root = (ComkerNavbarNode) c.uniqueResult();
            if (root != null) queue.add(root);

            ComkerNavbarNode exceptParentNode = null;
            ComkerNavbarNode exceptNode = null;

            while(!queue.isEmpty()) {
                ComkerNavbarNode node = queue.remove();
                
                Set<ComkerNavbarNode> children = node.getChildren();
                if (children == null) continue;
                for(ComkerNavbarNode child:children) {
                    if (exceptNodeId != null && exceptNodeId.equals(child.getId())) {
                        exceptParentNode = node;
                        exceptNode = child;
                        exceptNodeId = null;
                    }
                    queue.add(child);
                }
            }

            session.clear();

            if (exceptParentNode != null && exceptNode != null) {
                if (log.isDebugEnabled()) {
                    log.debug(MessageFormat.format(
                            "getNavbarTree() - ExceptNode.id: {0}; ParentNode.id: {1}",
                            new Object[]{exceptNode.getId(), exceptParentNode.getId()}));
                }
                exceptParentNode.getChildren().remove(exceptNode);
            }
            
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
