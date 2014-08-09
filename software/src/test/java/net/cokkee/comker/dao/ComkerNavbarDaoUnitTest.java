package net.cokkee.comker.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import net.cokkee.comker.model.po.ComkerNavbarNode;
import org.hamcrest.CoreMatchers;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author drupalex
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/dao/ComkerNavbarDaoUnitTest.xml"})
@Transactional
public class ComkerNavbarDaoUnitTest {

    @Autowired
    private SessionFactory testSessionFactory = null;

    @Autowired
    private ComkerNavbarDao testNavbarDao = null;
    
    @Before
    public void init() {
        Session session = testSessionFactory.getCurrentSession();
        
        ComkerNavbarNode navbarRootDPO = null;
        ComkerNavbarNode item = null;
        ComkerNavbarNode subitem = null;
        ComkerNavbarNode subsubitem = null;

        navbarRootDPO = new ComkerNavbarNode(
                "NAVBAR_ROOT",
                "http://comker.net/",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        navbarRootDPO.setParent(null);
        session.saveOrUpdate(navbarRootDPO);

        item = new ComkerNavbarNode(
                "NAVBAR_BRANCH01",
                "http://comker.net/branch01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        session.saveOrUpdate(item);

        subitem = new ComkerNavbarNode(
                item.getCode() + "_SUB01",
                item.getUrl() + "/sub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setParent(item);
        item.getChildren().add(subitem);
        session.saveOrUpdate(subitem);

        subitem = new ComkerNavbarNode(
                item.getCode() + "_SUB02",
                item.getUrl() + "/sub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subitem.setParent(item);
        item.getChildren().add(subitem);
        session.saveOrUpdate(subitem);

        subsubitem = new ComkerNavbarNode(
                subitem.getCode() + "_SUBSUB01",
                subitem.getUrl() + "/subsub01",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        subsubitem = new ComkerNavbarNode(
                subitem.getCode() + "_SUBSUB02",
                subitem.getUrl() + "/subsub02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        subsubitem = new ComkerNavbarNode(
                subitem.getCode() + "_SUBSUB03",
                subitem.getUrl() + "/subsub03",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        session.saveOrUpdate(subsubitem);

        item = new ComkerNavbarNode(
                "NAVBAR_BRANCH02",
                "http://comker.net/branch02",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        item.setParent(navbarRootDPO);
        navbarRootDPO.getChildren().add(item);
        session.saveOrUpdate(item);

        ComkerNavbarNode root = testNavbarDao.getByCode("NAVBAR_ROOT");
        testNavbarDao.update(root);
    }

    @Test
    public void test_database() {
        Session session = testSessionFactory.getCurrentSession();
        Criteria c = session.createCriteria(ComkerNavbarNode.class);
        List all = c.list();
        Assert.assertTrue(all.size() == 8);
    }

    @Test
    public void test_navbar_count() {
        Assert.assertTrue(testNavbarDao.count() == 8);
    }

    @Test
    public void test_navbar_get_tree() {
        ComkerNavbarNode root = testNavbarDao.getTree();
        Map<String, Object> info = extractTreeInformation(root);
        Assert.assertTrue((Integer)info.get("depth")== 4);
        Assert.assertTrue((Integer)info.get("total")== 8);
    }

    @Test
    public void test_navbar_get_node_by_code() {
        ComkerNavbarNode root = testNavbarDao.getByCode("NAVBAR_ROOT");
        Assert.assertNotNull(root);

        ComkerNavbarNode node = testNavbarDao.getByCode("NAVBAR_BRANCH02");
        Assert.assertNotNull(node);
        Assert.assertEquals(node.getUrl(), "http://comker.net/branch02");
    }

    @Test
    public void test_navbar_get_node_by_code_with_invalid_code() {
        ComkerNavbarNode root = testNavbarDao.getByCode("NAVBAR_NOT_FOUND");
        Assert.assertNull(root);
    }

    @Test
    public void test_navbar_get_node_by_id() {
        ComkerNavbarNode node1 = testNavbarDao.getByCode("NAVBAR_BRANCH01");
        ComkerNavbarNode node2 = testNavbarDao.get(node1.getId());
        Assert.assertTrue(node1 == node2);
    }

    @Test
    public void test_navbar_get_node_by_id_with_invalid_id() {
        Assert.assertNull(testNavbarDao.get("NODE_NOT_FOUND"));
    }

    @Test
    public void test_navbar_create_node() {
        ComkerNavbarNode subitem = null;
        ComkerNavbarNode subsubitem = null;

        subitem = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        Assert.assertNotNull(subitem);

        subsubitem = new ComkerNavbarNode(
                subitem.getCode() + "_SUBSUB04",
                subitem.getUrl() + "/subsub04",
                new String[] {"PERMISSION_01", "PERMISSION_02"});
        subsubitem.setParent(subitem);
        subitem.getChildren().add(subsubitem);
        ComkerNavbarNode result = testNavbarDao.create(subsubitem);

        Assert.assertNotNull(result);
        Assert.assertEquals(subsubitem.getCode(), result.getCode());
        Assert.assertEquals(subsubitem.getUrl(), result.getUrl());
        Assert.assertEquals(subsubitem.getLabel(), result.getLabel());
        Assert.assertEquals(subsubitem.getDescription(), result.getDescription());

        ComkerNavbarNode node = result;
        String treeId = result.getId();
        while(node.getParent() != null) {
            node = node.getParent();
            treeId = node.getId() + ">" + treeId;
        }
        Assert.assertEquals(result.getTreeId(), treeId);
    }

    @Test
    public void test_navbar_update_node() {
        
        ComkerNavbarNode item = testNavbarDao.getByCode("NAVBAR_BRANCH02");
        Assert.assertNotNull(item);

        ComkerNavbarNode subitem = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        Assert.assertNotNull(subitem);

        item.getChildren().add(subitem);
        subitem.setParent(item);
        testNavbarDao.update(subitem);

        // assert the relationship between parent and new child
        ComkerNavbarNode result = testNavbarDao.getByCode("NAVBAR_BRANCH01_SUB02");
        item = result.getParent();
        Assert.assertEquals("NAVBAR_BRANCH02", item.getCode());
        Assert.assertThat(item.getChildren(), CoreMatchers.hasItem(result));

        // assert the new treeId
        ComkerNavbarNode node = result;
        String treeId = result.getId();
        while(node.getParent() != null) {
            node = node.getParent();
            treeId = node.getId() + ">" + treeId;
        }
        Assert.assertEquals(result.getTreeId(), treeId);
    }

    @Test
    public void test_navbar_delete_node() {
        ComkerNavbarNode node = testNavbarDao.getByCode("NAVBAR_BRANCH01");
        testNavbarDao.delete(node);

        ComkerNavbarNode root = testNavbarDao.getTree();
        Map<String, Object> info = extractTreeInformation(root);
        Assert.assertTrue((Integer)info.get("depth")== 2);
        Assert.assertTrue((Integer)info.get("total")== 2);
    }

    @Test
    public void test_navbar_delete_root() {
        ComkerNavbarNode node = testNavbarDao.getByCode("NAVBAR_ROOT");
        testNavbarDao.delete(node);

        ComkerNavbarNode root = testNavbarDao.getTree();
        Assert.assertNull(root);
    }

    private Map<String, Object> extractTreeInformation(ComkerNavbarNode root) {
        int depth = 0;
        int count = 0;

        Queue<ComkerNavbarNode> queue = new LinkedList<ComkerNavbarNode>();
        Queue<ComkerNavbarNode> subqueue = new LinkedList<ComkerNavbarNode>();
        Queue<ComkerNavbarNode> tempo = null;

        if (root != null) {
            queue.add(root);
        }

        while (!queue.isEmpty()) {
            depth++;
            subqueue.clear();
            while(!queue.isEmpty()) {
                count++;
                ComkerNavbarNode node = queue.remove();
                subqueue.addAll(node.getChildren());
            }
            tempo = subqueue;
            subqueue = queue;
            queue = tempo;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("depth", depth);
        result.put("total", count);

        return result;
    }
}
