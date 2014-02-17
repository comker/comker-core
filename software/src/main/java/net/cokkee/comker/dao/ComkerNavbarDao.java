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
import org.hibernate.Session;
import org.hibernate.criterion.Order;
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
    public static final String FILTER_EXCLUDE_ID = "excludeId";

    public static final String FIELD_TREE_ID = "treeId";
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
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);

            if (params == null) params = new HashMap<String,Object>();

            Object id = params.get(FILTER_ID);
            Object excludeNodeId = params.get(FILTER_EXCLUDE_ID);

            if (id == null) {
                c.add(Restrictions.isNull(FIELD_PARENT));
            } else {
                c.add(Restrictions.idEq(id));
            }

            Queue<ComkerNavbarNode> queue = new LinkedList<ComkerNavbarNode>();

            ComkerNavbarNode root = (ComkerNavbarNode) c.uniqueResult();
            if (root != null) queue.add(root);

            ComkerNavbarNode excludeNodeParent = null;
            ComkerNavbarNode excludeNode = null;

            while(!queue.isEmpty()) {
                ComkerNavbarNode node = queue.remove();
                
                Set<ComkerNavbarNode> children = node.getChildren();
                if (children == null) continue;
                for(ComkerNavbarNode child:children) {
                    if (excludeNodeId != null && excludeNodeId.equals(child.getId())) {
                        excludeNodeParent = node;
                        excludeNode = child;
                        excludeNodeId = null;
                    }
                    queue.add(child);
                }
            }

            session.clear();

            if (excludeNodeParent != null && excludeNode != null) {
                if (log.isDebugEnabled()) {
                    log.debug(MessageFormat.format(
                            "getNavbarTree() - ExceptNode.id: {0}; ParentNode.id: {1}",
                            new Object[]{excludeNode.getId(), excludeNodeParent.getId()}));
                }
                excludeNodeParent.getChildren().remove(excludeNode);
            }
            
            return root;
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List getNavbarList() {
            return getNavbarList(null);
        }

        @Override
        @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
        public List getNavbarList(Map<String,Object> params) {
            Session session = this.getSessionFactory().getCurrentSession();
            Criteria c = session.createCriteria(ComkerNavbarNode.class);

            if (params == null) params = new HashMap<String,Object>();

            // retrieve the root
            Object id = params.get(FILTER_ID);
            if (id == null) {
                c.add(Restrictions.isNull(FIELD_PARENT));
            } else {
                c.add(Restrictions.idEq(id));
            }
            ComkerNavbarNode root = (ComkerNavbarNode) c.uniqueResult();
            if (root == null) return null;

            // retrieve the excluded node
            ComkerNavbarNode excludeNode = null;
            Object excludeNodeId = params.get(FILTER_EXCLUDE_ID);
            if (excludeNodeId != null) {
                excludeNode = (ComkerNavbarNode) session.get(
                        ComkerNavbarNode.class, excludeNodeId.toString());
            }

            // use Criteria query the nodes which are children of 'node' and not
            // be excludeNode or descendants of excludeNode
            Criteria l = session.createCriteria(ComkerNavbarNode.class);
            l.add(Restrictions.like(FIELD_TREE_ID, root.getTreeId() + "%"));
            if (excludeNode != null) {
                l.add(Restrictions.not(Restrictions.like(FIELD_TREE_ID, excludeNode.getTreeId() + "%")));
            }
            l.addOrder(Order.asc(FIELD_TREE_ID));

            // retrieve the list
            List<ComkerNavbarNode> result = l.list();

            // evict and remove the children property.
            session.clear();
            for(ComkerNavbarNode node:result) {
                node.setChildren(null);
            }

            return result;
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
