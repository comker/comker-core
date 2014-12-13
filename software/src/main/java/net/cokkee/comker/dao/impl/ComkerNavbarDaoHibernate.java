package net.cokkee.comker.dao.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.cokkee.comker.dao.ComkerNavbarDao;
import net.cokkee.comker.model.dpo.ComkerNavbarNodeDPO;

import org.hibernate.Criteria;
import org.hibernate.EmptyInterceptor;
import org.hibernate.LockOptions;
import org.hibernate.Query;
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
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

public class ComkerNavbarDaoHibernate extends ComkerAbstractDaoHibernate
        implements ComkerNavbarDao {

    private static Logger log = LoggerFactory.getLogger(ComkerNavbarDaoHibernate.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Integer count() {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNodeDPO.class);
        c.setProjection(Projections.rowCount());
        return ((Long) c.uniqueResult()).intValue();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNodeDPO getTree() {
        return getTree(null, null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNodeDPO getTree(String fromNodeId, String excludeNodeId) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNodeDPO.class);

        if (fromNodeId == null) {
            c.add(Restrictions.isNull(FIELD_PARENT));
        } else {
            c.add(Restrictions.idEq(fromNodeId));
        }

        Queue<ComkerNavbarNodeDPO> queue = new LinkedList<ComkerNavbarNodeDPO>();

        ComkerNavbarNodeDPO root = (ComkerNavbarNodeDPO) c.uniqueResult();
        if (root != null) queue.add(root);

        ComkerNavbarNodeDPO excludeNodeParent = null;
        ComkerNavbarNodeDPO excludeNode = null;

        while(!queue.isEmpty()) {
            ComkerNavbarNodeDPO node = queue.remove();
            Set<ComkerNavbarNodeDPO> children = node.getChildren();
            if (children == null) continue;
            for(ComkerNavbarNodeDPO child:children) {
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
        Criteria c = session.createCriteria(ComkerNavbarNodeDPO.class);

        if (fromNodeId == null) {
            c.add(Restrictions.isNull(FIELD_PARENT));
        } else {
            c.add(Restrictions.idEq(fromNodeId));
        }
        ComkerNavbarNodeDPO root = (ComkerNavbarNodeDPO) c.uniqueResult();
        if (root == null) return null;

        // retrieve the excluded node
        ComkerNavbarNodeDPO excludedNode = null;
        if (excludedNodeId != null) {
            excludedNode = (ComkerNavbarNodeDPO) session.get(
                    ComkerNavbarNodeDPO.class, excludedNodeId.toString());
        }

        // use Criteria query the nodes which are children of 'node' and not
        // be excludedNode or descendants of excludedNode
        Criteria l = session.createCriteria(ComkerNavbarNodeDPO.class);
        l.add(Restrictions.like(FIELD_TREE_ID, root.getTreeId() + "%"));
        if (excludedNode != null) {
            l.add(Restrictions.not(Restrictions.like(FIELD_TREE_ID, excludedNode.getTreeId() + "%")));
        }
        l.addOrder(Order.asc(FIELD_TREE_ID));

        // retrieve the list
        List<ComkerNavbarNodeDPO> result = l.list();

        // evict and remove the children property.
        session.clear();
        for(ComkerNavbarNodeDPO node:result) {
            node.setChildren(null);
        }

        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Boolean exists(String id) {
        Query query = this.getSessionFactory().getCurrentSession().
                createQuery(MessageFormat.format(
                    "select 1 from {0} t where t.id = :id",
                    new Object[] {ComkerNavbarNodeDPO.class.getSimpleName()}));
        query.setString("id", id);
        return (query.uniqueResult() != null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNodeDPO get(String id) {
        Session session = this.getSessionFactory().getCurrentSession();
        return (ComkerNavbarNodeDPO) session.get(ComkerNavbarNodeDPO.class, id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public ComkerNavbarNodeDPO getByCode(String code) {
        Session session = this.getSessionFactory().getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNodeDPO.class);
        c.add(Restrictions.eq(FIELD_CODE, code));
        List result = c.list();
        if (result.isEmpty()) return null;
        return (ComkerNavbarNodeDPO)result.get(0);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public ComkerNavbarNodeDPO create(ComkerNavbarNodeDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.save(item);
        propagationTreeData(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(ComkerNavbarNodeDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();
        session.saveOrUpdate(item);
        propagationTreeData(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void delete(ComkerNavbarNodeDPO item) {
        if (item == null) return;

        Session session = this.getSessionFactory().getCurrentSession();
        ComkerNavbarNodeDPO parent = item.getParent();
        if (parent != null) {
            parent.getChildren().remove(item);
            item.setParent(null);
            session.saveOrUpdate(parent);
        } else {
            session.delete(item);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void propagationTreeData(ComkerNavbarNodeDPO item) {
        Session session = this.getSessionFactory().getCurrentSession();

        Queue<ComkerNavbarNodeDPO> queue = new LinkedList<ComkerNavbarNodeDPO>();
        ComkerNavbarNodeDPO parent = null;

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
            ComkerNavbarNodeDPO node = queue.remove();
            parent = node.getParent();
            node.setTreeId(parent.getTreeId() + ">" + node.getId());
            node.setTreeIndent("----" + parent.getTreeIndent());
            queue.addAll(node.getChildren());
        }

        // save the change
        session.saveOrUpdate(item);
    }
}
