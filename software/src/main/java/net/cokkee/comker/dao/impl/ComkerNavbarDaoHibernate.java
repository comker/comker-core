package net.cokkee.comker.dao.impl;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.model.po.ComkerNavbarNode;

import org.hibernate.Criteria;
import org.hibernate.EmptyInterceptor;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public class ComkerNavbarDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerNavbarDao {

    private static Logger log = LoggerFactory.getLogger(ComkerNavbarDaoHibernate.class);

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
    public ComkerNavbarNode getTree() {
        return getTree(null, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNode getTree(String fromNodeId, String excludeNodeId) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNode.class);

        if (fromNodeId == null) {
            c.add(Restrictions.isNull(FIELD_PARENT));
        } else {
            c.add(Restrictions.idEq(fromNodeId));
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

        // evict and remove the children property.
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
    public List getList() {
        return getList(null, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List getList(String fromNodeId, String excludedNodeId) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNode.class);

        if (fromNodeId == null) {
            c.add(Restrictions.isNull(FIELD_PARENT));
        } else {
            c.add(Restrictions.idEq(fromNodeId));
        }
        ComkerNavbarNode root = (ComkerNavbarNode) c.uniqueResult();
        if (root == null) return null;

        // retrieve the excluded node
        ComkerNavbarNode excludedNode = null;
        if (excludedNodeId != null) {
            excludedNode = (ComkerNavbarNode) session.get(
                    ComkerNavbarNode.class, excludedNodeId.toString());
        }

        // use Criteria query the nodes which are children of 'node' and not
        // be excludedNode or descendants of excludedNode
        Criteria l = session.createCriteria(ComkerNavbarNode.class);
        l.add(Restrictions.like(FIELD_TREE_ID, root.getTreeId() + "%"));
        if (excludedNode != null) {
            l.add(Restrictions.not(Restrictions.like(FIELD_TREE_ID, excludedNode.getTreeId() + "%")));
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNode getByCode(String code) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNode.class);
        c.add(Restrictions.eq(FIELD_CODE, code));
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerNavbarNode)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerNavbarNode create(ComkerNavbarNode item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        propagationTreeData(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerNavbarNode item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        propagationTreeData(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerNavbarNode item) {
        if (item == null) return;

        Session session = this.getSessionFactory().getCurrentSession();
        ComkerNavbarNode parent = item.getParent();
        if (parent != null) {
            parent.getChildren().remove(item);
            item.setParent(null);
            session.saveOrUpdate(parent);
        } else {
            session.delete(item);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void propagationTreeData(ComkerNavbarNode item) {
        Session session = this.getSessionFactory().getCurrentSession();

        Queue<ComkerNavbarNode> queue = new LinkedList<ComkerNavbarNode>();
        ComkerNavbarNode parent = null;

        // update TreeData of item
        parent = item.getParent();
        if (parent == null) {
            item.setTreeId(item.getId());
            item.setTreeIndent("");
        } else {
            item.setTreeId(parent.getTreeId() + ">" + item.getId());
            item.setTreeIndent("----" + parent.getTreeIndent());
        }
        queue.addAll(item.getChildren());

        // use item as parent, update children's TreeData
        while(!queue.isEmpty()) {
            ComkerNavbarNode node = queue.remove();
            parent = node.getParent();
            node.setTreeId(parent.getTreeId() + ">" + node.getId());
            node.setTreeIndent("----" + parent.getTreeIndent());
            queue.addAll(node.getChildren());
        }

        // save the change
        session.saveOrUpdate(item);
    }
}
